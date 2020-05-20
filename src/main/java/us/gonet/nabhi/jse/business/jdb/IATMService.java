package us.gonet.nabhi.jse.business.jdb;


import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.JournalQuery;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;
import us.gonet.nabhi.misc.model.jdbc.jdb.ButtonMapping;
import us.gonet.nabhi.misc.model.jdbc.jdb.Journal;
import us.gonet.nabhi.misc.model.jdbc.jdb.Screen;
import us.gonet.nabhi.misc.model.jdbc.jdb.up.ATDUp;

import java.util.List;

public interface IATMService {

    ResponseWrapper < ATD > findATM( String terminalId );

    ResponseWrapper < ATD > findATM( String terminalId, String fiid );

    ResponseWrapper < ATDUp > findATMUpTime( String terminalId, String fiid );

    ResponseWrapper < List < ATD > > findAll();

    ResponseWrapper < List < ATD > > findAll( String fiid );

    ResponseWrapper < List < ATDUp > > findAllUpTime( String fiid );

    ResponseWrapper < String > saveJournal( Journal journal );

    ResponseWrapper < String > saveJournal( List < Journal > journals );

    ResponseWrapper < Screen > saveScreen( Screen screen );

    ResponseWrapper < String > saveSingleScreen( String terminalId, ButtonMapping single );

    ResponseWrapper < List < Journal > > find( String... id );

    ResponseWrapper < List < Journal > > findByQuery( JournalQuery query, String... id );

}
