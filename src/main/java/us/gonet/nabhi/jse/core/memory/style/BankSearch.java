package us.gonet.nabhi.jse.core.memory.style;

import org.springframework.stereotype.Component;
import us.gonet.nabhi.misc.exception.BankException;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.model.jdbc.jdb.BankStyle;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class BankSearch {

    private BankWrapper bankWrapper;
    private static final String INVALID_BANK = "Invalid Bank";

    public void buildTable( List < BankStyle > styles ) {
        Map < String, BankStyle > styleMap = new LinkedHashMap <>();
        for ( BankStyle i : styles ) {
            styleMap.put( i.getId(), i );
        }
        bankWrapper = new BankWrapper( styleMap );
    }


    public BankStyle search( String bankName ) throws BankException {
        BankStyle style = bankWrapper.getBanks().get( bankName );
        if ( style != null ) {
            return style;
        } else {
            throw new BankException( INVALID_BANK, Collections.singletonList( new ErrorWS( "BANK-01", INVALID_BANK ) ) );
        }
    }


}
