package us.gonet.nabhi.jse.i8583.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.core.srh.SurchargeFinder;
import us.gonet.nabhi.misc.exception.ATMException;
import us.gonet.nabhi.misc.exception.SurchargeException;
import us.gonet.nabhi.misc.model.jdbc.jdb.APC;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;
import us.gonet.nabhi.misc.model.jdbc.jdb.Surcharge;
import us.gonet.nabhi.misc.model.srh.RequestSurcharge;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


@Component
public class ISOUtils {

    private static final String ZONE_HOST = "America/Mexico_City";
    private SurchargeFinder finder;

    @Autowired
    public ISOUtils( SurchargeFinder finder ) {
        this.finder = finder;
    }

    public String allowGroup( ATD atd, APC apc ) {
        return apc.getSharingGroup() + apc.getAllowedCode().getAllowedCode() + atd.getCounty().getState().getStateCode() + atd.getCounty().getCountyCode() + atd.getIdf().getCountry().getCountryCode() + apc.getRoutingGroup();
    }

    public String surchargeValue( String ip, String track, String tranCode ) throws ATMException {
        RequestSurcharge requestSurcharge = new RequestSurcharge();
        requestSurcharge.setIp( ip );
        requestSurcharge.setTrack( track );
        requestSurcharge.setTransactionCode( tranCode );
        Surcharge surcharge = null;
        try {
            surcharge = finder.getSurcharge( requestSurcharge );
        } catch ( SurchargeException e ) {
            return "1000";
        }
        return surcharge.getSurcharges();
    }

    public String obfuscateCardNumber( String cardNumber ) {
        StringBuilder obs = new StringBuilder();
        int i = 0;
        for ( char c : cardNumber.toCharArray() ) {
            if ( i < cardNumber.length() - 4 ) {
                obs.append( "*" );
            } else {
                obs.append( c );
            }
            i++;
        }
        return obs.toString();
    }

    public String verifyUTCDifference( String zone ) {
        ZoneId center = ZoneId.of( ZONE_HOST );
        ZonedDateTime zoneHost = Instant.now().atZone( center );
        int secondsHost = zoneHost.getOffset().getTotalSeconds();
        ZoneId atm = ZoneId.of( zone );
        ZonedDateTime atmZone = Instant.now().atZone( atm );
        int secondsATM = atmZone.getOffset().getTotalSeconds();
        if ( secondsHost == secondsATM ) {
            return "+000";
        }
        if ( secondsHost > secondsATM ) {
            String dif = String.valueOf( ( secondsHost - secondsATM ) / 60 );
            if ( dif.length() < 3 ) {
                dif = "0" + dif;
            }
            return "-" + dif;
        } else {
            String dif = String.valueOf( ( secondsATM - secondsHost ) / 60 );
            if ( dif.length() < 3 ) {
                dif = "0" + dif;
            }
            return "+" + dif;
        }
    }
}
