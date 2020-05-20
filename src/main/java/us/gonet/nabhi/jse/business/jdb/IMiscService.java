package us.gonet.nabhi.jse.business.jdb;

import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jdbc.composite.APCId;
import us.gonet.nabhi.misc.model.jdbc.composite.RCPTId;
import us.gonet.nabhi.misc.model.jdbc.composite.SurchargeId;
import us.gonet.nabhi.misc.model.jdbc.jdb.*;

import java.util.List;

public interface IMiscService {

    ResponseWrapper < List < BIN > > findAllBin();

    ResponseWrapper < List < IDF > > findAllIDF();

    ResponseWrapper < IDF > findByFiid( String fiid );

    ResponseWrapper < List < BankStyle > > findAllBackStyle();

    ResponseWrapper < BankStyle > findBankStyle( String id );

    ResponseWrapper < NodeProsa > findNode( String id );

    ResponseWrapper < RCPT > findRCPT( RCPTId rcptId );

    ResponseWrapper < List < RCPT > > findRCPTAll();

    ResponseWrapper < List < Image64 > > findAllImages();

    ResponseWrapper < List < Image64 > > findAllImagesByFiid( String fiid );

    ResponseWrapper < Image64 > findById( String id, String category );

    ResponseWrapper < Image64 > findById( String id, String category, String fiid );

    ResponseWrapper < List < ScreenGroup > > findAllScreen();

    ResponseWrapper < List < ScreenGroup > > findAllScreen( String fiid );

    ResponseWrapper < ScreenGroup > findScreenByFiid( String fiid );

    ResponseWrapper < Integer > findCounty( String county, String state );

    ResponseWrapper < String > deleteApc( APCId apcId );

    ResponseWrapper < String > deleteSrh( SurchargeId surchargeId );

    ResponseWrapper <String > refreshMemory( );

}
