package us.gonet.nabhi.jse.business.jdb;

import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jdbc.jdb.*;

import java.util.List;
import java.util.Map;

public interface ISaveService {

    ResponseWrapper < List < BIN > > saveBins( List< BIN> bines );

    ResponseWrapper < List < State > > saveStates( List < State > states );

    ResponseWrapper < List < County > > saveCountys( List < County > countys );

    ResponseWrapper < List < IDF > > saveIdfs( Map < String, Object > entrys );

    ResponseWrapper < List < Button > > saveButtons( List < Button > buttons );

    ResponseWrapper < List < APC > > saveApcs( List < APC > apcs );

    ResponseWrapper < List < TranAllowed > > saveTranAllowes( List < TranAllowed > tranAlloweds );

    ResponseWrapper < List < NodeProsa > > saveNodes( List < NodeProsa > nodes );

    ResponseWrapper < List < Surcharge > > saveSurcharges( List < Surcharge > surcharges );

    ResponseWrapper < List < ATD > > saveAtms( List < ATD > atds, boolean create );

    ResponseWrapper < List < RCPT > > saveRcpts( List < RCPT > rcpts );

    ResponseWrapper < IDF > updateIdf( IDF idf );

    ResponseWrapper < IDF > saveIdf( IDF idf );

    ResponseWrapper < String > saveImages( List < Image64 > image64s );

    ResponseWrapper < String > saveScreen( List < ScreenGroup > screenGroups );

    ResponseWrapper < String > saveBankStyle( List < BankStyle > bankStyles );
}
