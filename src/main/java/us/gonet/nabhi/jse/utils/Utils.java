package us.gonet.nabhi.jse.utils;

import java.util.regex.Pattern;

public class Utils {

    private Utils() {
    }

    private static String isCharacter( String search ) {
        StringBuilder sb = new StringBuilder( search.length() );
        for ( int i = 0; i < search.length(); ++i ) {
            char ch = search.charAt( i );
            if ( Character.isLetterOrDigit( ch ) || ch == ' ' || ch == ';' || ch == '?' || ch == '=' ) {
                sb.append( ch );
            }
        }
        return sb.toString();
    }

    public static String sanitize( String input ) {
        final String message = Pattern.quote( input );
        return isCharacter( message.substring( 2, message.length() - 2 ) );
    }
}
