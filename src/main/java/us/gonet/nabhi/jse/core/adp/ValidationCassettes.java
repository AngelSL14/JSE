package us.gonet.nabhi.jse.core.adp;

import org.springframework.stereotype.Component;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.ServerException;
import us.gonet.nabhi.misc.model.devices.cdm.Cassette;
import us.gonet.nabhi.misc.model.devices.constants.cdm.cassette.CassetteStatus;
import us.gonet.nabhi.misc.model.devices.constants.cdm.cassette.CassetteType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static us.gonet.nabhi.misc.model.devices.constants.cdm.cassette.CassetteStatus.*;
import static us.gonet.nabhi.misc.model.devices.constants.cdm.cassette.CassetteType.BILLCASSETTE;
import static us.gonet.nabhi.misc.model.devices.constants.cdm.cassette.CassetteType.RECYCLING;

@Component
public class ValidationCassettes {

    private static final String STATE = "state";
    private static final String DENOMINATION = "denomination";
    private static final String NUMBER = "number";

    public List < Map < String, Object > > validateCashUnits( List < Cassette > cassettes ) {
        List < Map < String, Object > > atm = new ArrayList <>();
        for ( Cassette c : cassettes ) {
            CassetteType type = CassetteType.valueOfCompose( c.getType() );
            if ( type == BILLCASSETTE || type == RECYCLING ) {
                Map < String, Object > cass = new LinkedHashMap <>();
                cass.put( DENOMINATION, c.getDenomination() );
                cass.put( NUMBER, c.getCassetteIndex() );
                cass.put( STATE, validateStatusForSingleCassette( c ) );
                atm.add( cass );
            }
        }
        return mirrorCashDispenser( atm );

    }

    private boolean validateStatusForSingleCassette( Cassette cassette ) {
        CassetteStatus s = CassetteStatus.valueOfCompose( cassette.getStatus() );
        return ( s == OK || ( s == LOW || s == FULL || s == HIGH ) )
                && cassette.getCurrent() >= 100;
    }

    private List < Map < String, Object > > mirrorCashDispenser( List < Map < String, Object > > states ) {
        for ( int i = 1; i < states.size(); i++ ) {
            boolean state1 = ( boolean ) states.get( i - 1 ).get( STATE );
            boolean state2 = ( boolean ) states.get( i ).get( STATE );
            int cass1 = ( int ) states.get( i - 1 ).get( DENOMINATION );
            int cass2 = ( int ) states.get( i ).get( DENOMINATION );
            if ( ( state1 == state2 ) && ( cass1 == cass2 ) ) {
                states.get( i - 1 ).put( STATE, false );
            }
        }
        return states;
    }

    public Cassette getLowCassette( List < Map < String, Object > > atm, List < Cassette > cassettes ) throws ServerException {
        int multiple = 0;
        for ( Map c : atm ) {
            if ( ( boolean ) c.get( STATE ) ) {
                multiple = ( int ) c.get( DENOMINATION );
                break;
            }
        }
        for ( Cassette c : cassettes ) {
            CassetteType type = CassetteType.valueOfCompose( c.getType() );
            if ( c.getDenomination() == multiple && ( type == BILLCASSETTE || type == RECYCLING ) ) {
                return c;
            }
        }
        List < ErrorWS > errors = new ArrayList <>();
        errors.add( new ErrorWS( "ADP-03", "Invalid Denomination" ) );
        throw new ServerException( "Invalid Denomination", errors );
    }

}
