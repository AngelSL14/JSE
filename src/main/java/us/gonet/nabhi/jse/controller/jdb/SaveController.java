package us.gonet.nabhi.jse.controller.jdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.gonet.nabhi.jse.business.jdb.ISaveService;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jdbc.jdb.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping( "/save" )
public class SaveController {

    private ISaveService saveService;

    @Autowired
    public SaveController( ISaveService saveService ) {
        this.saveService = saveService;
    }

    @ResponseBody
    @PostMapping( "/idf" )
    public ResponseWrapper < List < IDF > > saveIdfs( @RequestBody Map < String, Object > entrys ) {
        return saveService.saveIdfs( entrys );
    }

    @ResponseBody
    @PostMapping( "/updateIdf" )
    public ResponseWrapper < IDF > updateIDF( @RequestBody IDF idf ) {
        return saveService.updateIdf( idf );
    }

    @ResponseBody
    @PostMapping( "/saveIdf" )
    public ResponseWrapper < IDF > saveIDF( @RequestBody IDF idf ) {
        return saveService.saveIdf( idf );
    }

    @ResponseBody
    @PostMapping( "/state" )
    public ResponseWrapper < List < State > > saveState( @RequestBody List < State > states ) {
        return saveService.saveStates( states );
    }

    @ResponseBody
    @PostMapping( "/county" )
    public ResponseWrapper < List < County > > saveCounty( @RequestBody List < County > counties ) {
        return saveService.saveCountys( counties );
    }

    @ResponseBody
    @PostMapping( "/buttons" )
    public ResponseWrapper < List < Button > > saveButtons( @RequestBody List < Button > buttons ) {
        return saveService.saveButtons( buttons );
    }

    @ResponseBody
    @PostMapping( "/apc" )
    public ResponseWrapper < List < APC > > saveApc( @RequestBody List < APC > apcs ) {
        return saveService.saveApcs( apcs );
    }

    @ResponseBody
    @PostMapping( "/tranAllowed" )
    public ResponseWrapper < List < TranAllowed > > saveTranAllowed( @RequestBody List < TranAllowed > tranAlloweds ) {
        return saveService.saveTranAllowes( tranAlloweds );
    }

    @ResponseBody
    @PostMapping( "/node" )
    public ResponseWrapper < List < NodeProsa > > saveNode( @RequestBody List < NodeProsa > nodes ) {
        return saveService.saveNodes( nodes );
    }

    @ResponseBody
    @PostMapping( "/surcharge" )
    public ResponseWrapper < List < Surcharge > > saveSurcharge( @RequestBody List < Surcharge > surcharges ) {
        return saveService.saveSurcharges( surcharges );
    }

    @ResponseBody
    @PostMapping( "/atm/{create:.+}" )
    public ResponseWrapper < List < ATD > > saveAtm( @PathVariable boolean create, @RequestBody List < ATD > atds ) {
        return saveService.saveAtms( atds, create );
    }

    @ResponseBody
    @PostMapping( "/bin" )
    public ResponseWrapper < List < BIN > > saveBin( @RequestBody List < BIN > bines ) {
        return saveService.saveBins( bines );
    }

    @ResponseBody
    @PostMapping( "/rcpt" )
    public ResponseWrapper < List < RCPT > > saveRcpt( @RequestBody List < RCPT > rcpts ) {
        return saveService.saveRcpts( rcpts );
    }

    @ResponseBody
    @PostMapping( "/image" )
    public ResponseWrapper < String > saveImages( @RequestBody List < Image64 > image64s ) {
        return saveService.saveImages( image64s );
    }


    @ResponseBody
    @PostMapping( "/screen" )
    public ResponseWrapper < String > saveScreens( @RequestBody List < ScreenGroup > screenGroups ) {
        return saveService.saveScreen( screenGroups );
    }

    @ResponseBody
    @PostMapping( "/bankStyle" )
    public ResponseWrapper < String > saveBankStyles( @RequestBody List < BankStyle > bankStyles ) {
        return saveService.saveBankStyle( bankStyles );
    }

}
