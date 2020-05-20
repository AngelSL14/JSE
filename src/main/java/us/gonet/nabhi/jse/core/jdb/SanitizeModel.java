package us.gonet.nabhi.jse.core.jdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.utils.StreamFilter;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.SanitazeException;
import us.gonet.nabhi.misc.exception.ServerException;
import us.gonet.nabhi.misc.model.devices.cdm.Cassette;
import us.gonet.nabhi.misc.model.devices.cdm.Dispenser;
import us.gonet.nabhi.misc.model.devices.ptr.Printer;
import us.gonet.nabhi.misc.model.jdbc.composite.BINId;
import us.gonet.nabhi.misc.model.jdbc.composite.RCPTId;
import us.gonet.nabhi.misc.model.jdbc.jdb.*;

import java.util.Collections;

@Component
public class SanitizeModel {

    private StreamFilter filter;
    private static final String INVALID_PARAM = "Invalid param ";

    @Autowired
    public SanitizeModel( StreamFilter filter ) {
        this.filter = filter;
    }

    public void sanitize( State e ) throws SanitazeException {
        try {
            e.setStateCode( filter.sanitizeString( e.getStateCode() ) );
            e.setStateName( filter.sanitizeString( e.getStateName() ) );
            e.setStateShortName( filter.sanitizeString( e.getStateShortName() ) );
            e.setZone( filter.sanitizeString( e.getZone() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getStateName() );
        }
    }

    public void sanitize ( BIN e ) throws SanitazeException {
            sanitize(e.getBinId());
            sanitizeIdf( e.getIdf() );
    }

    public void sanitize( BINId e ) throws SanitazeException {
        try {
            e.setBin( filter.sanitizeString( e.getBin() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getBin() );
        }
    }

    public void sanitizeIdf( IDF e )throws SanitazeException {
        try {
            e.setFiid( filter.sanitizeString( e.getFiid() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getFiid() );
        }
    }

    public void sanitize( County e ) throws SanitazeException {
        try {
            e.setCountyCode( filter.sanitizeString( e.getCountyCode() ) );
            e.setCountyName( filter.sanitizeString( e.getCountyName() ) );
            sanitize( e.getState() );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getCountyName() );
        }
    }

    public void sanitize( Country e ) throws SanitazeException {
        try {
            e.setCountryCode( filter.sanitizeString( e.getCountryCode() ) );
            e.setName( filter.sanitizeString( e.getName() ) );
            e.setAlpha2( filter.sanitizeString( e.getAlpha2() ) );
            e.setAlpha3( filter.sanitizeString( e.getAlpha3() ) );
            e.setSymbols( filter.sanitizeString( e.getSymbols(), "[a-zA-Z0-9 .=*/_" + e.getSymbols() + "]+" ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getName() );
        }
    }

    public void sanitize( IDF e ) throws SanitazeException {
        try {
            e.setFiid( filter.sanitizeString( e.getFiid() ) );
            e.setAcquiringId( filter.sanitizeString( e.getAcquiringId() ) );
            e.setLogicalNet( filter.sanitizeString( e.getLogicalNet() ) );
            e.setName( filter.sanitizeString( e.getName() ) );
            e.setNameLong( filter.sanitizeString( e.getNameLong(), "[a-zA-Z0-9 .=*/_,()]+ " ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getName() );
        }
    }

    public void sanitize( Button e ) throws SanitazeException {
        try {
            e.setId( Integer.parseInt( filter.sanitizeString( "" + e.getId() ) ) );
            e.setScreenComponent( filter.sanitizeString( e.getScreenComponent() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getScreenComponent() );
        }
    }

    public void sanitize( APC e ) throws SanitazeException {
        try {
            e.getApcId().setFiid( filter.sanitizeString( e.getApcId().getFiid() ) );
            e.getApcId().setFormAcct( filter.sanitizeString( e.getApcId().getFormAcct() ) );
            e.getApcId().setToAcct( filter.sanitizeString( e.getApcId().getToAcct() ) );
            e.getApcId().setTranCode( filter.sanitizeString( e.getApcId().getTranCode() ) );
            sanitize( e.getAllowedCode() );
            e.setFiidOwner( filter.sanitizeString( e.getFiidOwner() ) );
            e.setRoutingGroup( filter.sanitizeString( e.getRoutingGroup() ) );
            e.setSharingGroup( filter.sanitizeString( e.getSharingGroup() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getApcId().toString() );
        }
    }

    public void sanitize( TranAllowed e ) throws SanitazeException {
        try {
            e.setAllowedCode( filter.sanitizeString( e.getAllowedCode() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getAllowedCode() );
        }
    }

    public void sanitize( NodeProsa e ) throws SanitazeException {
        try {
            e.setIp( filter.sanitizeString( e.getIp() ) );
            e.setNodeName( filter.sanitizeString( e.getNodeName() ) );
            e.setPort( filter.sanitizeString( e.getPort() ) );
            e.setTracerNumber( Integer.parseInt( filter.sanitizeString( "" + e.getTracerNumber() ) ) );
            e.setZpk( filter.sanitizeString( e.getZpk() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getNodeName() );
        }
    }

    public void sanitize( Surcharge e ) throws SanitazeException {
        try {
            e.getSurchargeId().setFiidAcquirer( filter.sanitizeString( e.getSurchargeId().getFiidAcquirer() ) );
            e.getSurchargeId().setFiidIssuing( filter.sanitizeString( e.getSurchargeId().getFiidIssuing() ) );
            e.getSurchargeId().setTranCode( filter.sanitizeString( e.getSurchargeId().getTranCode() ) );
            e.setSurchargeIdentity( filter.sanitizeString( e.getSurchargeIdentity() ) );
            e.setSurcharges( filter.sanitizeString( e.getSurcharges() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getSurchargeId().toString() );
        }
    }

    public void sanitize( ATD e, boolean create ) throws SanitazeException {
        try {
            e.setTerminalId( filter.sanitizeString( e.getTerminalId() ) );
            e.setSequenceNumber( filter.sanitizeString( e.getSequenceNumber() ) );
            e.setSequence( filter.sanitizeString( e.getSequence() ) );
            e.setDeviceType( filter.sanitizeString( e.getDeviceType() ) );
            if ( create ) {
                e.getCounty().setCountyCodeId( Integer.parseInt( filter.sanitizeString( "" + e.getCounty().getCountyCodeId() ) ) );
                e.getNodeProsa().setIdNode( Integer.parseInt( filter.sanitizeString( "" + e.getNodeProsa().getIdNode() ) ) );
                e.getIdf().setFiid( filter.sanitizeString( e.getIdf().getFiid() ) );
            } else {
                sanitize( e.getIdf() );
                sanitize( e.getCounty() );
                sanitize( e.getNodeProsa() );
            }
            sanitize( e.getAtm(), create );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getTerminalId() );
        }
    }

    private void sanitize( ATM e, boolean create ) throws SanitazeException {
        try {
            e.setTerminalId( filter.sanitizeString( e.getTerminalId() ) );
            e.setIp( filter.sanitizeString( e.getIp() ) );
            e.setSucursal( filter.sanitizeString( e.getSucursal() ) );
            if ( !create && e.getLastTrx() != null && e.getReceipt() != null ) {
                e.setLastTrx( filter.sanitizeString( e.getLastTrx(), "[a-zA-Z0-9 .=*&!]+" ) );
                e.setReceipt( filter.sanitizeString( e.getReceipt(), "[a-zA-Z0-9 .=*:/,$\\n]+" ) );
            }
            sanitize( e.getScreen() );
            sanitize( e.getDevices() );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getTerminalId() );
        }
    }

    public void sanitize( Screen e ) throws SanitazeException {
        try {
            e.setTerminalId( filter.sanitizeString( e.getTerminalId() ) );
            e.setScreenType( filter.sanitizeString( e.getScreenType() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getTerminalId() );
        }
    }

    public void sanitize( ScreenGroup e ) throws SanitazeException {
        try {
            e.setGroupId( filter.sanitizeString( e.getGroupId() ) );
            e.setPublicityName( filter.sanitizeString( e.getPublicityName() ) );
            e.setBackGround( filter.sanitizeString( e.getBackGround() ) );
            for ( ButtonMapping b : e.getButtonsAllowed().getButtons() ) {
                sanitize( b );
            }
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getBackGround() );
        }
    }

    public void sanitize( ButtonMapping e ) throws SanitazeException {
        try {
            e.setBitmap( filter.sanitizeString( e.getBitmap() ) );
            e.setScreenComponent( filter.sanitizeString( e.getScreenComponent() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getBitmap() );
        }
    }

    private void sanitize( Devices e ) throws SanitazeException {
        try {
            sanitize( e.getTerminalDevices().getDispenser() );
            sanitize( e.getTerminalDevices().getPrinter() );
        } catch ( SanitazeException e1 ) {
            throwSanitizeException( "Devices: " + e.getTerminalId() + e1.getMessage() );
        }
    }

    private void sanitize( Dispenser e ) throws SanitazeException {
        try {
            e.setStatus( Integer.parseInt( filter.sanitizeString( "" + e.getStatus() ) ) );
            for ( Cassette c : e.getCassettes() ) {
                sanitize( c );
            }
        } catch ( ServerException e1 ) {
            throwSanitizeException( "Dispenser" );
        }
    }


    private void sanitize( Cassette e ) throws SanitazeException {
        try {
            e.setCassetteIndex( Integer.parseInt( filter.sanitizeString( "" + e.getCassetteIndex() ) ) );
            e.setCurrency( filter.sanitizeString( e.getCurrency() ) );
            e.setDenomination( Integer.parseInt( filter.sanitizeString( "" + e.getDenomination() ) ) );
            e.setInitialCount( Integer.parseInt( filter.sanitizeString( "" + e.getInitialCount() ) ) );
            e.setDecrement( Integer.parseInt( filter.sanitizeString( "" + e.getDecrement() ) ) );
            e.setCurrent( Integer.parseInt( filter.sanitizeString( "" + e.getCurrent() ) ) );
            e.setDispensed( Integer.parseInt( filter.sanitizeString( "" + e.getDispensed() ) ) );
            e.setDeposited( Integer.parseInt( filter.sanitizeString( "" + e.getDeposited() ) ) );
            e.setRejected( Integer.parseInt( filter.sanitizeString( "" + e.getRejected() ) ) );
            e.setRetracted( Integer.parseInt( filter.sanitizeString( "" + e.getRetracted() ) ) );
            e.setStatus( filter.sanitizeString( e.getStatus() ) );
            e.setType( filter.sanitizeString( e.getType() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( "Cassette " + e.getCassetteIndex() );
        }
    }

    private void sanitize( Printer e ) throws SanitazeException {
        try {
            e.setPaper( filter.sanitizeString( e.getPaper() ) );
            e.setToner( filter.sanitizeString( e.getToner() ) );
            e.setStatus( Integer.parseInt( filter.sanitizeString( "" + e.getStatus() ) ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( "Printer" );
        }
    }

    public void sanitize( TokenUsers e ) throws SanitazeException {
        try {
            e.setId( filter.sanitizeString( e.getId() ) );
            e.setClave( filter.sanitizeString( e.getClave() ) );
            e.setRol( Byte.parseByte( filter.sanitizeString( "" + e.getRol() ) ) );
            sanitize( e.getUserDetail() );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getId() );
        }
    }

    private void sanitize( TokenUsersDetail e ) throws SanitazeException {
        try {
            e.setUser( filter.sanitizeString( e.getUser() ) );
            e.setExpirationTime( Long.parseLong( filter.sanitizeString( "" + e.getExpirationTime() ) ) );
            e.setExpirationTokenTime( Long.parseLong( filter.sanitizeString( "" + e.getExpirationTokenTime() ) ) );
            e.setIssueTime( Long.parseLong( filter.sanitizeString( "" + e.getIssueTime() ) ) );
            e.setLastUsage( Long.parseLong( filter.sanitizeString( "" + e.getLastUsage() ) ) );
            e.setServicesAllow( filter.sanitizeString( e.getServicesAllow() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getUser() );
        }
    }

    public void sanitize( Journal e ) throws SanitazeException {
        try {
            e.setMessage( filter.sanitizeString( e.getMessage() ) );
            e.setTerminalId( filter.sanitizeString( e.getTerminalId() ) );
            e.setTerminalIdJournal( filter.sanitizeString( e.getTerminalIdJournal() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getTerminalId() + " Journal" );
        }
    }

    public void sanitize( RCPT e ) throws SanitazeException {
        //try {
        //  e.setHeader( filter.sanitizeString( e.getHeader(), "[a-zA-Z0-9 '['']',.=*/_%#@]+" ) );
        //  e.setBody( filter.sanitizeString( e.getBody(), "[a-zA-Z0-9 [].=*/_%#@]+" ) );
        //  e.setTrailer( filter.sanitizeString( e.getTrailer(), "[a-zA-Z0-9 [].=*/_%#@]+" ) );
        sanitize( e.getRcptId() );
        //} catch ( ServerException e1 ) {
        //    throwSanitizeException( e.toString() + " RCPT ID" );
        //}
    }

    public void sanitize( RCPTId e ) throws SanitazeException {
        try {
            e.setFiid( filter.sanitizeString( e.getFiid() ) );
            e.setTranCode( filter.sanitizeString( e.getTranCode() ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.toString() + " RCPT ID" );
        }
    }

    public void sanitize( Image64 e ) throws SanitazeException {
        try {
            e.setName( filter.sanitizeString( e.getName() ) );
            e.setCategory( filter.sanitizeString( e.getCategory() ) );
            sanitize( e.getWrapper() );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getName() );
        }
    }

    private void sanitize( FileWrapper e ) throws SanitazeException {
        try {
            e.setName( filter.sanitizeString( e.getName() ) );
            e.setExtension( filter.sanitizeString( e.getExtension() ) );
            e.setFormat( filter.sanitizeString( e.getFormat() ) );
            e.setCodec( filter.sanitizeString( e.getCodec(), "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$" ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getName() );
        }
    }

    public void sanitize( DashboardUsers e ) throws SanitazeException {
        try {
            e.setPassword( filter.sanitizeString( e.getPassword() ) );
            e.setPhoneNumber( filter.sanitizeString( e.getPhoneNumber() ) );
            e.setEmail( filter.sanitizeString( e.getEmail(), "[a-zA-Z0-9 .=*:,/_@]+" ) );
            e.setFullname( filter.sanitizeString( e.getFullname() ) );
            e.setRol( filter.sanitizeString( e.getRol() ) );
            e.setExpirationTime( Long.parseLong( filter.sanitizeString( "" + e.getExpirationTime() ) ) );
            e.setExpirationTokenTime( Long.parseLong( filter.sanitizeString( "" + e.getExpirationTokenTime() ) ) );
            e.setIssueTime( Long.parseLong( filter.sanitizeString( "" + e.getIssueTime() ) ) );
            e.setLastUsage( Long.parseLong( filter.sanitizeString( "" + e.getLastUsage() ) ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getEmail() );
        }
    }

    public void sanitize( BankStyle e ) throws SanitazeException {
        final String REGEX = "[a-zA-Z0-9 .=*:,<\"%/_{}()#]+";
        try {
            e.setId( filter.sanitizeString( e.getId() ) );
            e.setButtons( filter.sanitizeString( e.getButtons(), REGEX ) );
            e.setDashboard( filter.sanitizeString( e.getDashboard(), REGEX ) );
        } catch ( ServerException e1 ) {
            throwSanitizeException( e.getId() );
        }
    }


    private void throwSanitizeException( String param ) throws SanitazeException {
        throw new SanitazeException( INVALID_PARAM, Collections.singletonList( new ErrorWS( "JDB-01", INVALID_PARAM + param ) ) );
    }
}
