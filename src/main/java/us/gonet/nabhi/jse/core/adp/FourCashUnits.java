package us.gonet.nabhi.jse.core.adp;

import org.springframework.stereotype.Component;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.ServerException;
import us.gonet.nabhi.misc.model.devices.cdm.Cassette;
import us.gonet.nabhi.misc.model.devices.constants.cdm.cassette.CassetteType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static us.gonet.nabhi.misc.model.devices.constants.cdm.cassette.CassetteType.BILLCASSETTE;
import static us.gonet.nabhi.misc.model.devices.constants.cdm.cassette.CassetteType.RECYCLING;

@Component
public class FourCashUnits {

    private static final String STATE = "state";
    private static final String DENOMINATION = "denomination";

    public String getBills( int amount, List < Map < String, Object > > atm, List < Cassette > cassettes ) throws ServerException {
        boolean[] availableCassettes = getAvailableCassettes( atm );
        int multiple = getMultiple( atm );
        System.err.println("Min Multiple" + multiple);
        int[] denomination = getDenomination( atm );
        int denomination3 = denomination[ 2 ];
        int denomination4 = denomination[ 3 ];
        int cassette1 = 0;
        int cassette2 = 0;
        int cassette3 = 0;
        int cassette4 = 0;
        if ( availableCassettes[ 0 ] || availableCassettes[ 1 ] ) {
            int[] cs = lowDenominationsOk( multiple, amount, availableCassettes, denomination );
            cassette1 = cs[ 0 ];
            cassette2 = cs[ 1 ];
            cassette3 = cs[ 2 ];
            cassette4 = cs[ 3 ];
        } else if ( availableCassettes[ 2 ] && availableCassettes[ 3 ] ) {
            int[] cs = highDenominationsOk( amount, denomination );
            cassette1 = cs[ 0 ];
            cassette2 = cs[ 1 ];
            cassette3 = cs[ 2 ];
            cassette4 = cs[ 3 ];
        } else if ( availableCassettes[ 3 ] ) {
            if ( amount % denomination4 == 0 ) {
                cassette4 = cassette4 + ( amount / denomination4 );
            } else {
                throw throwIncorrectAmount();
            }
        } else if ( availableCassettes[ 2 ] ) {
            if ( amount % denomination3 == 0 ) {
                cassette3 = cassette3 + ( amount / denomination3 );
            } else {
                throw throwIncorrectAmount();
            }
        }
        return bills( cassette1, cassette2, cassette3, cassette4, cassettes );
    }

    private int[] lowDenominationsOk( int multiple, int amount, boolean[] availableCassettes, int[] denominations ) throws ServerException {
        if ( multiple != 0 && amount % multiple == 0 ) {
            return lowDenomination( amount, availableCassettes, denominations );
        } else {
            throw throwIncorrectAmount();
        }

    }

    private int[] highDenominationsOk( int amount, int[] denominations ) throws ServerException {
        int denomination2 = denominations[ 1 ];
        int denomination3 = denominations[ 2 ];
        int denomination4 = denominations[ 3 ];
        int[] cassettes = { 0, 0, 0, 0 };
        if ( amount >= denomination3 && amount % denomination2 == 0 ) {
            if ( amount >= denomination4 ) {
                cassettes = highDenomination( amount, denominations );
            } else if ( amount % denomination3 == 0 ) {
                cassettes[ 2 ] = cassettes[ 2 ] + ( amount / denomination3 );
            } else {
                throw throwIncorrectAmount();
            }
        } else {
            throw throwIncorrectAmount();
        }
        return cassettes;
    }

    private static String bills( int c1, int c2, int c3, int c4, List < Cassette > cassettes ) throws ServerException {
        StringBuilder response = new StringBuilder();
        if ( c1 + c2 + c3 + c4 > 0 && c1 + c2 + c3 + c4 < 50 ) {
            int[] bills = { c1, c2, c3, c4 };
            int cas = 0;
            for ( Cassette c : cassettes )
            {
                CassetteType type = CassetteType.valueOfCompose( c.getType() );
                if ( type == BILLCASSETTE || type == RECYCLING )
                {
                    response.append( String.format( "%02d", bills[ cas ] ) );
                    cas++;
                } else if (c.getCassetteIndex() == 1){
                    StringBuilder special = new StringBuilder();
                    special.append("00").append(response);
                    response = special;
                }else{
                    response.append("00");
                }
            }
        } else if ( c1 + c2 + c3 + c4 >= 50 ) {
            List < ErrorWS > errorWS = new ArrayList <>();
            errorWS.add( new ErrorWS( "ADP-04", "The requested amount exceeds 50 notes" ) );
            throw new ServerException( "The requested amount exceeds 50 notes", errorWS );
        }
        return response.toString();
    }

    private boolean[] getAvailableCassettes( List < Map < String, Object > > atm ) throws ServerException {
        boolean[] availableCassettes = { false, false, false, false };
        int notOkCashUnits = 0;
        for ( int i = 0; i < atm.size(); i++ ) {
            availableCassettes[ i ] = ( boolean ) atm.get( i ).get( STATE );
            if ( !( boolean ) atm.get( i ).get( STATE ) ) {
                notOkCashUnits++;
            }
        }
        if ( notOkCashUnits == 4 ) {
            List < ErrorWS > errorWS = new ArrayList <>();
            errorWS.add( new ErrorWS( "ADP-02", "No cassette available to dispense" ) );
            throw new ServerException( "No cassette available to dispense", errorWS );
        }
        return availableCassettes;
    }

    private int getMultiple( List < Map < String, Object > > atm ) {
        int multiplo = 0;
        for ( Map cassettes : atm ) {
            if ( ( boolean ) cassettes.get( STATE ) ) {
                multiplo = ( int ) cassettes.get( DENOMINATION );
                break;
            }
        }
        return multiplo;
    }

    private int[] getDenomination( List < Map < String, Object > > atm ) {
        int[] denoms = { 0, 0, 0, 0 };
        for ( int i = 0; i < atm.size(); i++ ) {
            denoms[ i ] = ( int ) atm.get( i ).get( DENOMINATION );
        }
        return denoms;
    }

    private int[] lowDenomination( int amount, boolean[] availableCassettes, int[] denominations ) {
        int denomination1 = denominations[ 0 ];
        int denomination2 = denominations[ 1 ];
        int denomination3 = denominations[ 2 ];
        int cassette1 = 0;
        int cassette2 = 0;
        int cassette3 = 0;
        int cassette4 = 0;

        while ( amount > 0 ) {
            if ( availableCassettes[ 0 ] && amount >= denomination1 ) {
                cassette1 = cassette1 + 1;
                amount = amount - denomination1;
            }
            if ( availableCassettes[ 1 ] && amount >= denomination2 ) {
                cassette2 = cassette2 + 1;
                amount = amount - denomination2;
            }
            if ( availableCassettes[ 2 ] && amount >= denomination3 ) {
                cassette3 = cassette3 + 1;
                amount = amount - denomination3;
            }

            int[] highBills = billsFromHigher( amount, availableCassettes, denominations );
            cassette1 = cassette1 + highBills[ 0 ];
            cassette2 = cassette2 + highBills[ 1 ];
            cassette3 = cassette3 + highBills[ 2 ];
            cassette4 = cassette4 + highBills[ 3 ];
            amount = highBills[ 4 ];
        }
        return new int[]{ cassette1, cassette2, cassette3, cassette4 };
    }

    private int[] highDenomination( int amount, int[] denominations ) {
        int denomination1 = denominations[ 2 ];
        int denomination2 = denominations[ 3 ];

        int cassette3 = 0;
        int cassette4 = 0;

        while ( amount > 0 ) {
            if ( amount % denomination2 == 0 ) {
                cassette4 = cassette4 + ( amount / denomination2 );
                amount = amount - ( denomination2 * ( amount / denomination2 ) );
            }
            if ( amount % denomination1 == 0 ) {
                if ( amount != 0 ) {
                    cassette3 = cassette3 + 1;
                    amount = amount - denomination1;
                }
            } else {
                cassette4 = cassette4 + 1;
                amount = amount - denomination2;
            }
        }

        return new int[]{ 0, 0, cassette3, cassette4 };
    }

    private ServerException throwIncorrectAmount() {
        List < ErrorWS > errorWS = new ArrayList <>();
        errorWS.add( new ErrorWS( "ADP-03", "El monto solicitado no se puede dispensar" ) );
        return new ServerException( "El monto solicitado no se puede dispensar", errorWS );
    }

    private int[] billsFromHigher( int amount, boolean[] availableCassettes, int[] denominations ) {
        int denomination1 = denominations[ 0 ];
        int denomination2 = denominations[ 1 ];
        int denomination3 = denominations[ 2 ];
        int denomination4 = denominations[ 3 ];
        int cassette1 = 0;
        int cassette2 = 0;
        int cassette3 = 0;
        int cassette4 = 0;

        if ( availableCassettes[ 3 ] && amount >= denomination4 ) {
            cassette4 = amount / denomination4;
            amount = amount - ( cassette4 * denomination4 );
        }
        if ( availableCassettes[ 2 ] && amount >= denomination3 ) {
            cassette3 = cassette3 + ( amount / denomination3 );
            amount = amount - ( denomination3 * ( amount / denomination3 ) );
        }
        if ( availableCassettes[ 1 ] && amount >= denomination2 ) {
            cassette2 = cassette2 + ( amount / denomination2 );
            amount = amount - ( denomination2 * ( amount / denomination2 ) );
        }
        if ( availableCassettes[ 0 ] && amount >= denomination1 ) {
            cassette1 = cassette1 + ( amount / denomination1 );
            amount = amount - ( denomination1 * ( amount / denomination1 ) );
        }
        return new int[]{ cassette1, cassette2, cassette3, cassette4, amount };
    }

}
