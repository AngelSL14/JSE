package us.gonet.nabhi.jse.business.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.business.IProsaBusiness;
import us.gonet.nabhi.jse.core.adp.Dispensed;
import us.gonet.nabhi.jse.core.srh.SurchargeFinder;
import us.gonet.nabhi.jse.i8583.manager.TransactionManagement;
import us.gonet.nabhi.jse.i8583.misc.ISOUtils;
import us.gonet.nabhi.jse.journal.JournalWriter;
import us.gonet.nabhi.jse.utils.UrlFilter;
import us.gonet.nabhi.misc.exception.*;
import us.gonet.nabhi.misc.model.ATMResponseModel;
import us.gonet.nabhi.misc.model.adp.BillsModel;
import us.gonet.nabhi.misc.model.jdbc.jdb.Surcharge;
import us.gonet.nabhi.misc.model.jse.request.*;
import us.gonet.nabhi.misc.model.jse.response.GenericProcess;
import us.gonet.nabhi.misc.model.srh.RequestSurcharge;
import us.gonet.utils.STMTDecoder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class ProsaBusinessImpl implements IProsaBusiness {


    private static final Logger LOG = LoggerFactory.getLogger( ProsaBusinessImpl.class );

    private static final String ERROR_4 = "Not Aproved";
    private static final String ERROR_3 = "Invalid Response";
    private static final String WITHD_ERROR = "Error en la autorizacion del retiro";
    private static final String INQ_ERROR = "Error en la autorizacion de la consulta de Saldo";
    private static final String LTRX_ERROR = "Error en la autorizacion de la consulta de Movimientos";
    private static final String NCH_ERROR = "Error en la autorizacion del cambio de NIP";
    private static final String GES_ERROR = "Error en la autorizacion de la recarga de saldo";
    private static final String COMMI_ERROR = "Error al obtener la comision";

    private TransactionManagement transactionManagement;
    private ISOUtils isoUtils;
    private JournalWriter journalWriter;
    private UrlFilter urlFilter;
    private SurchargeFinder finder;
    private Dispensed dispensed;

    @Autowired
    public ProsaBusinessImpl( TransactionManagement transactionManagement, ISOUtils isoUtils, JournalWriter journalWriter, UrlFilter urlFilter, SurchargeFinder finder, Dispensed dispensed ) {
        this.transactionManagement = transactionManagement;
        this.isoUtils = isoUtils;
        this.journalWriter = journalWriter;
        this.urlFilter = urlFilter;
        this.finder = finder;
        this.dispensed = dispensed;
    }

    @Override
    public ResponseWrapper < BillsModel > cashWithAuth( CashWithdrawalModel generic ) throws ServerException {
        ResponseWrapper < BillsModel > wrapper = new ResponseWrapper <>();
        try {
            journalWriter.writeJournal( generic.getTermId(), "Esperando Autorizacion del retiro de efectivo" );
            ATMResponseModel atmResponseModel = transactionManagement.performTransaction( generic, "01" );
            transactionManagement.saveAuthorizationInfo( atmResponseModel, generic );
            wrapper.setCode( "200" );
            wrapper.addBody( dispensed.dispenseFourUnits( generic.getIp(), Integer.parseInt( generic.getCashWithAmount() ) ) );
            journalWriter.writeJournal( generic.getTermId(), "Retiro de efectivo autorizado, BufferAmount: " + wrapper.getBody().get( 0 ).getBills() );
        } catch ( ServerException e ) {
            writeErrorJournal( e, generic, WITHD_ERROR );
            e.getErrors().add( new ErrorWS( "-JXI02", WITHD_ERROR ) );
            throw new ServerException( WITHD_ERROR, e.getErrors() );
        }

        return wrapper;
    }

    @Override
    public ResponseWrapper < Generic > getCommission( AtmInfo generic ) throws ServerException {
        ResponseWrapper < Generic > response = new ResponseWrapper <>();
        try {
            generic.setIp( urlFilter.sanitizeString( generic.getIp() ) );
            generic.setTrack( urlFilter.sanitizeString( generic.getTrack() ) );
            generic.setTransactionCode( urlFilter.sanitizeString( generic.getTransactionCode() ) );
            RequestSurcharge srh = new RequestSurcharge();
            srh.setIp( generic.getIp() );
            srh.setTrack( generic.getTrack() );
            srh.setTransactionCode( generic.getTransactionCode() );
            Surcharge surcharge = finder.getSurcharge( srh );
            response.setCode( "200" );
            response.addBody( Generic.builder().withTxCommission( surcharge.getSurcharges() ).build() );
        } catch ( ATMException | ServerException e ) {
            List < ErrorWS > errorWS = new ArrayList <>();
            errorWS.add( new ErrorWS( "-JXI03", COMMI_ERROR ) );
            errorWS.addAll( e.getErrors() );
            throw new ServerException( COMMI_ERROR, errorWS );
        } catch ( SurchargeException e ) {
            LOG.error( COMMI_ERROR );
            response.setCode( "200" );
            response.addBody( Generic.builder().withTxCommission( "00.00" ).build() );
            return response;
        }

        return response;
    }


    @Override
    public ResponseWrapper < Map < String, String > > balInquiryAuth( Generic generic ) throws ServerException {
        ResponseWrapper < Map < String, String > > response = new ResponseWrapper <>();
        try {
            journalWriter.writeJournal( generic.getTermId(), "Esperando Autorizacion de la consulta de saldo" );
            ATMResponseModel atmResponseModel = transactionManagement.performTransaction( generic, "31" );
            transactionManagement.saveAuthorizationInfo( atmResponseModel, generic );
            double oldBalance = Double.parseDouble( atmResponseModel.getMessage().getDataElements().get( 43 ).getContentField().substring( 13, 25 ) ) / 100;
            DecimalFormat f = new DecimalFormat( "$#,###,##0.00" );
            String cuenta = atmResponseModel.getMessage().getDataElements().get( 101 ).getContentField();
            String monto = f.format( oldBalance );
            SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yyyy" );
            String date = sdf.format( new Date() );
            Map < String, String > datos = new HashMap <>();
            String numCuenta = isoUtils.obfuscateCardNumber( cuenta );
            datos.put( "monto", monto );
            datos.put( "fecha", date );
            datos.put( "numCuenta", numCuenta );
            response.setCode( "200" );
            response.addBody( datos );
            journalWriter.writeJournal( generic.getTermId(), "Consulta de saldo autorizada" );

        } catch ( ServerException e ) {
            writeErrorJournal( e, generic, INQ_ERROR );
            List < ErrorWS > errorWS = e.getErrors();
            errorWS.add( new ErrorWS( "-JXI06", INQ_ERROR ) );
            throw new ServerException( INQ_ERROR, errorWS );
        }
        return response;
    }


    @Override
    public ResponseWrapper < String > listTrx( Generic generic ) throws ServerException {
        ResponseWrapper < String > response = new ResponseWrapper();
        try {
            ATMResponseModel atmResponseModel = transactionManagement.performTransaction( generic, "94" );
            transactionManagement.saveAuthorizationInfo( atmResponseModel, generic );
            List < String > trxList = new STMTDecoder().decode( atmResponseModel.getMessage().getDataElements().get( 124 ).getContentField() );
            response.setCode( "200" );
            response.setBody( trxList );
            journalWriter.writeJournal( generic.getTermId(), "Consulta de movimientos autorizada" );
        } catch ( ServerException e ) {
            writeErrorJournal( e, generic, LTRX_ERROR );
            List < ErrorWS > errorWS = e.getErrors();
            errorWS.add( new ErrorWS( "-JXI11", LTRX_ERROR ) );
            throw new ServerException( LTRX_ERROR, errorWS );
        }
        return response;
    }


    @Override
    public ResponseWrapper < GenericProcess > changePin( ChangeNipModel generic ) throws ServerException {
        ResponseWrapper < GenericProcess > wrapper = new ResponseWrapper <>();
        try {
            ATMResponseModel responseModel = transactionManagement.performTransaction( generic, "96" );
            wrapper.setCode( "200" );
            transactionManagement.saveAuthorizationInfo( responseModel, generic );
            journalWriter.writeJournal( generic.getTermId(), "Cambio de NIP autorizado" );
            return wrapper;
        } catch ( ServerException e ) {
            writeErrorJournal( e, generic, NCH_ERROR );
            List < ErrorWS > wsList = e.getErrors();
            wsList.add( new ErrorWS( "-JXI12", NCH_ERROR ) );
            throw new ServerException( NCH_ERROR, wsList );
        }
    }

    @Override
    public ResponseWrapper < GenericProcess > genericSale( GenericSaleModel generic ) throws ServerException {
        ResponseWrapper < GenericProcess > response = new ResponseWrapper <>();
        try {
            ATMResponseModel atmResponseModel = transactionManagement.performTransaction( generic, "65" );
            transactionManagement.saveAuthorizationInfo( atmResponseModel, generic );
            response.setCode( "200" );
            journalWriter.writeJournal( generic.getTermId(), "Recarga de tiempo aire autorizada" );

        } catch ( ServerException e ) {
            writeErrorJournal( e, generic, GES_ERROR );
            List < ErrorWS > errorWS = e.getErrors();
            errorWS.add( new ErrorWS( "-JXI13", GES_ERROR ) );
            throw new ServerException( GES_ERROR, errorWS );
        }
        return response;
    }


    private void writeErrorJournal( ServerException e, Generic generic, String operation ) {
        LOG.error( "Error en la peticion entre servicios" );

        if ( e.getMessage().equals( ERROR_4 ) ) {
            journalWriter.writeJournal( generic.getTermId(), operation + " : " + e.getErrors().get( 0 ).getErrorMessage() );
        } else if ( e.getMessage().equals( ERROR_3 ) ) {
            journalWriter.writeJournal( generic.getTermId(), operation + " : respuesta invalida del autorizador - " + e.getErrors().get( 0 ).getErrorMessage() );
        } else{
            journalWriter.writeJournal(generic.getTermId(), operation );
        }
    }
}