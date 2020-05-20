package us.gonet.nabhi.jse.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.ServerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class UrlFilter {

    private static final Logger LOG = LoggerFactory.getLogger( UrlFilter.class );


    public String sanitizeString( String val ) throws ServerException {
        String regex = "[a-zA-Z0-9.=?]+";

        if ( val != null && Pattern.matches( regex, val ) ) {
            String newVal = Pattern.quote( val );
            return newVal.substring( 2, newVal.length() - 2 );
        } else {
            List < ErrorWS > errs = new ArrayList <>();
            errs.add( new ErrorWS( "JXI095", "Invalid Body Parameter" ) );
            LOG.error( "Invalid Body Parameter" );
            throw new ServerException( "JXI095", errs );
        }


    }

    public Map < String, String > sanitizeEmv( Map < String, String > emv ) throws ServerException {
        Map < String, String > emvSan = new HashMap <>();
        for ( Map.Entry e : emv.entrySet() ) {
            emvSan.put( ( String ) e.getKey(), sanitizeString( ( String ) e.getValue() ) );
        }
        return emvSan;
    }


}
