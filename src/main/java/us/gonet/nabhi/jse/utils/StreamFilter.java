package us.gonet.nabhi.jse.utils;

import org.springframework.stereotype.Component;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.exception.ServerException;

import java.util.regex.Pattern;

@Component
public class StreamFilter< T > {


    public String sanitizeString( String val, String... customRegex ) throws ServerException {
        String regex = "[a-zA-Z0-9 .=*:,/_]+";
        if ( customRegex != null && customRegex.length > 0 ) {
            regex = customRegex[ 0 ];
        }
        if ( val != null && Pattern.matches( regex, val ) ) {
            String newVal = Pattern.quote( val );
            return newVal.substring( 2, newVal.length() - 2 );
        } else {
            throw new ServerException( "JDB095", null );
        }
    }

    public ResponseWrapper < T > sanitizeError() {
        ResponseWrapper < T > response = new ResponseWrapper <>();
        response.setCode( "01" );
        response.addError( new ErrorWS( "JDB-01", "Invalid param" ) );
        return response;
    }
}
