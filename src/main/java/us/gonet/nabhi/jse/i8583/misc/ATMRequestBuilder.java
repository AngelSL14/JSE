package us.gonet.nabhi.jse.i8583.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.iso8583.constants.atm.ToAccount;
import us.gonet.nabhi.jse.utils.EntryMode;
import us.gonet.nabhi.jse.utils.TRAN_CDE;
import us.gonet.nabhi.misc.exception.ATMException;
import us.gonet.nabhi.misc.model.ATMRequestModel;
import us.gonet.nabhi.misc.model.jdbc.jdb.APC;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;
import us.gonet.nabhi.misc.model.jdbc.jdb.NodeProsa;
import us.gonet.nabhi.misc.model.jse.request.CashWithdrawalModel;
import us.gonet.nabhi.misc.model.jse.request.ChangeNipModel;
import us.gonet.nabhi.misc.model.jse.request.Generic;
import us.gonet.nabhi.misc.model.jse.request.GenericSaleModel;
import us.gonet.nabhi.misc.model.node.NodeSingleModel;
import us.gonet.token.emv.data.DataB4;
import us.gonet.utils.Utilities;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ATMRequestBuilder {


    private ISOUtils isoUtils;

    @Autowired
    public ATMRequestBuilder( ISOUtils isoUtils ) {
        this.isoUtils = isoUtils;
    }

    public ATMRequestModel build( Generic e, ATD atd, APC apc, String process ) throws ATMException {
        ATMRequestModel request = new ATMRequestModel();
        request.setFromAccount( e.getTipoCuenta() );
        request.setToAccount( ToAccount.NO_ACCOUNT.getValue() );
        if ( process.equals( TRAN_CDE.BALANCE_INQUIRY.getValue() )
                || process.equals( TRAN_CDE.PIN_CHANGE.getValue() )
                || process.equals( TRAN_CDE.STATEMENT_PRINT.getValue() ) ) {
            request.setAmount( "00" );
        } else {
            if ( process.equals( TRAN_CDE.WITHDRAWAL.getValue() ) ) {
                request.setAmount( ( ( CashWithdrawalModel ) e ).getCashWithAmount() );
            } else if ( process.equals( TRAN_CDE.GENERIC_SALE.getValue() ) ) {
                request.setAmount( ( ( GenericSaleModel ) e ).getCashWithAmount() );
            }
        }
        request.setSurcharge( isoUtils.surchargeValue( e.getIp(), e.getTrack(), process ) );
        request.setTrack2( e.getTrack() );
        NodeProsa node = atd.getNodeProsa();
        node.setTracerNumber( node.getTracerNumber() + 1 );
        String traceNumber = Utilities.leftPadding( "0", 6, String.valueOf( node.getTracerNumber() ) );
        int tSeq = Integer.parseInt( atd.getSequenceNumber() ) + 1;
        atd.setSequenceNumber( "" + tSeq );
        String sequenceNumber = Utilities.leftPadding( "0", 12, String.valueOf( atd.getSequenceNumber() ) );
        request.setTraceNumber( traceNumber );
        request.setSequenceNumber( sequenceNumber );
        request.setTermId( Utilities.rightPadding( " ", 16, atd.getTerminalId() ) );
        request.setTermOwnerName( Utilities.rightPadding( " ", 22, atd.getIdf().getName() ) );
        request.setTermCity( Utilities.rightPadding( " ", 13, atd.getCounty().getCountyName() ) );
        request.setTermState( Utilities.rightPadding( " ", 3, atd.getCounty().getState().getStateShortName() ) );
        request.setTermCountry( atd.getIdf().getCountry().getAlpha2() );
        request.setGroupAllow( isoUtils.allowGroup( atd, apc ) );
        request.setCurrencyCode( atd.getIdf().getCountry().getCountryCode() );
        request.setPinBlock( e.getNip() );
        if ( process.equals( TRAN_CDE.PIN_CHANGE.getValue() ) ) {
            request.setNewPinBlock( ( ( ChangeNipModel ) e ).getNewPin() );
            request.setNewPinBlock2( ( ( ChangeNipModel ) e ).getConfirmNewPin() );
        }
        request.setTermFiid( atd.getIdf().getFiid() );
        request.setlNet( atd.getIdf().getLogicalNet() );
        request.setTimeOffSet( isoUtils.verifyUTCDifference( atd.getCounty().getState().getZone() ) );
        String discretionary = e.getTrack().substring( e.getTrack().indexOf( '=' ) + 1 );
        request.setEntryMode( entryMode( discretionary, e.getEmv() ) + "1" );
        validateB4Data( e, request.getEntryMode() );
        request.setEmv( e.getEmv() );
        request.setTermType( atd.getDeviceType() );
        if ( process.equals( TRAN_CDE.GENERIC_SALE.getValue() ) ) {
            request.setCompany( ( ( GenericSaleModel ) e ).getCompany() );
            request.setPhoneNumber( ( ( GenericSaleModel ) e ).getTelefono() );
            request.setPhoneNumber2( ( ( GenericSaleModel ) e ).getTelefono() );
        }
        NodeSingleModel nodeSingle = new NodeSingleModel();
        nodeSingle.setNodeName( atd.getNodeProsa().getNodeName() );
        nodeSingle.setIdNode( atd.getNodeProsa().getIdNode() );
        request.setNodeSingle( nodeSingle );
        return request;
    }


    private String entryMode( String discretionary, Map < String, String > emv ) {
        String capabilities = discretionary.substring( 4, 5 );
        if ( capabilities.equals( "2" ) || capabilities.equals( "6" ) ) {
            if ( emv != null && emv.size() > 1 ) {
                return EntryMode.CHIP.getValue();
            } else {
                return EntryMode.CHIP_ERROR.getValue();
            }

        } else {
            return EntryMode.MAGNETIC_STRIPE.getValue();
        }
    }

    private void validateB4Data( Generic e, String entryMode ) {
        Map < String, String > emv;
        if ( e.getEmv() == null ) {
            emv = new LinkedHashMap <>();
            e.setEmv( emv );
        } else {
            emv = e.getEmv();
        }
        emv.put( DataB4.PT_SRV_ENTRY_MDE.getTag(), entryMode );
        emv.put( DataB4.TERM_ENTRY_CAP.getTag(), "5" );
        emv.put( DataB4.LAST_EMV_STAT.getTag(), ( entryMode.equals( "051" ) ) ? "1" : "0" );
        emv.put( DataB4.DATA_SUSPECT.getTag(), "1" );
        emv.put( DataB4.APPL_PAN_SEQ_NUM.getTag(), ( emv.get( "5F34" ) != null ) ? emv.get( "5F34" ) : "00" );
        emv.put( DataB4.APPRVD_RC.getTag(), "00" );
        emv.put( DataB4.UNUSED.getTag(), "0000" );
        emv.put( DataB4.RSN_ONL_CDE.getTag(), "1508" );
        emv.put( DataB4.ISO_RC_IND.getTag(), " " );
    }
}
