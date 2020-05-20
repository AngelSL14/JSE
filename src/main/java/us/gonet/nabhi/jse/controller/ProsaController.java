package us.gonet.nabhi.jse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import us.gonet.nabhi.jse.business.IProsaBusiness;
import us.gonet.nabhi.jse.journal.JournalWriter;
import us.gonet.nabhi.jse.utils.DestinationAccount;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.exception.ServerException;
import us.gonet.nabhi.misc.model.adp.BillsModel;
import us.gonet.nabhi.misc.model.jse.request.*;
import us.gonet.nabhi.misc.model.jse.response.GenericProcess;

import java.util.Map;

@RestController
@RequestMapping( "athz" )
@CrossOrigin( origins = { "*" }, maxAge = 6000 )

public class ProsaController {

    private static final String TYPE = "Tipo de cuenta: ";
    private static final String ACCEPT_COMMISION = "El cliente acepta la comision por ";

    private static final String[] ALLOWED_FIELDS = new String[]{ "termId", "ip", "txCommission",
            "tipoCuenta", "nip", "track", "emv", "cashWithAmount", "newPin", "confirmNewPin", "telefono", "company" };

    private IProsaBusiness business;
    private JournalWriter journalWriter;

    @Autowired
    public ProsaController( IProsaBusiness business, JournalWriter journalWriter ) {
        this.business = business;
        this.journalWriter = journalWriter;
    }

    @InitBinder( "Generic" )
    public void initBinder( WebDataBinder binder ) {
        binder.setAllowedFields( ALLOWED_FIELDS );
    }

    @ResponseBody
    @PostMapping( "/wthdw" )
    public ResponseWrapper < BillsModel > cashWithAuth( @RequestBody CashWithdrawalModel generic ) {
        journalWriter.writeJournal( generic.getTermId(), TYPE + DestinationAccount.getTypeFromCode( generic.getTipoCuenta() ) );
        journalWriter.writeJournal( generic.getTermId(), "Operacion retiro de efectivo" );
        journalWriter.writeJournal( generic.getTermId(), ACCEPT_COMMISION + generic.getTxCommission() );
        ResponseWrapper < BillsModel > response = new ResponseWrapper <>();
        try {
            response = business.cashWithAuth( generic );
        } catch ( ServerException e ) {
            response.setCode( "-500" );
            response.addAllError( e.getErrors() );
        }
        return response;
    }

    @ResponseBody
    @PostMapping( "/blnInq" )
    public ResponseWrapper < Map < String, String > > balInquiryAuth( @RequestBody Generic generic ) {
        journalWriter.writeJournal( generic.getTermId(), TYPE + DestinationAccount.getTypeFromCode( generic.getTipoCuenta() ) );
        journalWriter.writeJournal( generic.getTermId(), "Operacion consulta de saldo" );
        journalWriter.writeJournal( generic.getTermId(), ACCEPT_COMMISION + generic.getTxCommission() );

        ResponseWrapper < Map < String, String > > response = new ResponseWrapper <>();
        try {
            response = business.balInquiryAuth( generic );
        } catch ( ServerException e ) {
            response.setCode( "-500" );
            response.addAllError( e.getErrors() );
        }

        return response;

    }

    @ResponseBody
    @PostMapping( "/cmmssn" )
    public ResponseWrapper < Generic > getCommission( @RequestBody AtmInfo atmInfo ) {
        ResponseWrapper < Generic > response = new ResponseWrapper <>();
        try {
            response = business.getCommission( atmInfo );
        } catch ( ServerException e ) {
            response.setCode( "-500" );
            response.addAllError( e.getErrors() );
        }
        return response;
    }

    @ResponseBody
    @PostMapping( "/lstTrx" )
    public ResponseWrapper < String > listTrx( @RequestBody Generic generic ) {
        journalWriter.writeJournal( generic.getTermId(), TYPE + DestinationAccount.getTypeFromCode( generic.getTipoCuenta() ) );
        journalWriter.writeJournal( generic.getTermId(), "Operacion listado de movimientos" );
        journalWriter.writeJournal( generic.getTermId(), ACCEPT_COMMISION + generic.getTxCommission() );
        ResponseWrapper < String > response = new ResponseWrapper <>();
        try {
            response = business.listTrx( generic );
        } catch ( ServerException e ) {
            response.setCode( "-500" );
            response.addAllError( e.getErrors() );
        }
        return response;
    }

    @ResponseBody
    @PostMapping( "/chngPNb" )
    public ResponseWrapper < GenericProcess > changeNip( @RequestBody ChangeNipModel generic ) {
        journalWriter.writeJournal( generic.getTermId(), TYPE + DestinationAccount.getTypeFromCode( generic.getTipoCuenta() ) );
        journalWriter.writeJournal( generic.getTermId(), "Operacion cambio de nip" );
        journalWriter.writeJournal( generic.getTermId(), ACCEPT_COMMISION + generic.getTxCommission() );
        ResponseWrapper < GenericProcess > response = new ResponseWrapper <>();
        try {
            response = business.changePin( generic );
        } catch ( ServerException e ) {
            response.setCode( "-500" );
            response.addAllError( e.getErrors() );
        }
        return response;
    }

    @ResponseBody
    @PostMapping( "/gnrcSl" )
    public ResponseWrapper < GenericProcess > genericSale( @RequestBody GenericSaleModel generic ) {
        journalWriter.writeJournal( generic.getTermId(), TYPE + DestinationAccount.getTypeFromCode( generic.getTipoCuenta() ) );
        journalWriter.writeJournal( generic.getTermId(), "Operacion compra de tiempo aire" );
        journalWriter.writeJournal( generic.getTermId(), ACCEPT_COMMISION + generic.getTxCommission() );
        ResponseWrapper < GenericProcess > response = new ResponseWrapper <>();
        try {
            response = business.genericSale( generic );
        } catch ( ServerException e ) {
            response.setCode( "-500" );
            response.addAllError( e.getErrors() );
        }
        return response;
    }
}
