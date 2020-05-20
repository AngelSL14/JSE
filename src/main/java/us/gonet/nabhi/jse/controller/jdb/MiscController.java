package us.gonet.nabhi.jse.controller.jdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.gonet.nabhi.jse.business.jdb.IMiscService;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jdbc.composite.APCId;
import us.gonet.nabhi.misc.model.jdbc.composite.RCPTId;
import us.gonet.nabhi.misc.model.jdbc.composite.SurchargeId;
import us.gonet.nabhi.misc.model.jdbc.jdb.*;

import java.util.List;

@RestController
@RequestMapping( "/misc" )
public class MiscController {

    private IMiscService iMiscService;

    @Autowired
    public MiscController( IMiscService iMiscService ) {
        this.iMiscService = iMiscService;
    }

    @ResponseBody
    @GetMapping( "/bin/findAll" )
    public ResponseWrapper < List < BIN > > findAllBin() {
        return iMiscService.findAllBin();
    }

    @ResponseBody
    @GetMapping( "/idf/{fiid:.+}" )
    public ResponseWrapper < IDF > findByFiid( @PathVariable String fiid ) {
        return iMiscService.findByFiid( fiid );
    }

    @ResponseBody
    @GetMapping( "/bankStyle/findAll" )
    public ResponseWrapper < List < BankStyle > > findAllBankStyles() {
        return iMiscService.findAllBackStyle();
    }

    @ResponseBody
    @GetMapping( "/bank/findStyle/{id:.+}" )
    public ResponseWrapper < BankStyle > findStyle( @PathVariable String id ) {
        return iMiscService.findBankStyle( id );
    }

    @ResponseBody
    @GetMapping( "/node/findNode/{id:.+}" )
    public ResponseWrapper < NodeProsa > findNode( @PathVariable String id ) {
        return iMiscService.findNode( id );
    }

    @ResponseBody
    @PostMapping( "/rcpt/find" )
    public ResponseWrapper < RCPT > findRcpt( @RequestBody RCPTId rcptId ) {
        return iMiscService.findRCPT( rcptId );
    }

    @ResponseBody
    @GetMapping( "/rcpt/findAll" )
    public ResponseWrapper < List < RCPT > > findRcptAll() {
        return iMiscService.findRCPTAll();
    }


    @ResponseBody
    @GetMapping( "/image/findAll" )
    public ResponseWrapper < List < Image64 > > findAllImage() {
        return iMiscService.findAllImages();
    }

    @ResponseBody
    @GetMapping( "/image/findAll/{fiid:.+}" )
    public ResponseWrapper < List < Image64 > > findAllImage( @PathVariable String fiid ) {
        return iMiscService.findAllImagesByFiid( fiid );
    }

    @ResponseBody
    @GetMapping( "/image/{id:.+}/{category:.+}" )
    public ResponseWrapper < Image64 > findById( @PathVariable String id, @PathVariable String category ) {
        return iMiscService.findById( id, category );
    }

    @ResponseBody
    @GetMapping( "/image/{id:.+}/{category:.+}/{fiid:.+}" )
    public ResponseWrapper < Image64 > findById( @PathVariable String id, @PathVariable String category, @PathVariable String fiid ) {
        return iMiscService.findById( id, category, fiid );
    }

    @ResponseBody
    @GetMapping( "/screen/findAll" )
    public ResponseWrapper < List < ScreenGroup > > findAllScreen() {
        return iMiscService.findAllScreen();
    }

    @ResponseBody
    @GetMapping( "/screen/find/{fiid:.+}" )
    public ResponseWrapper < ScreenGroup > findScreenByFiid( @PathVariable String fiid ) {
        return iMiscService.findScreenByFiid( fiid );
    }

    @ResponseBody
    @GetMapping( "/screen/findAll/{fiid:.+}" )
    public ResponseWrapper < List < ScreenGroup > > findAllScreenByFiid( @PathVariable String fiid ) {
        return iMiscService.findAllScreen( fiid );
    }

    @ResponseBody
    @GetMapping( "/county/{county:.+}/{state:.+}" )
    public ResponseWrapper < Integer > findCounty( @PathVariable String county, @PathVariable String state ) {
        return iMiscService.findCounty( county, state );
    }

    @ResponseBody
    @PostMapping( "/delete/apc" )
    public ResponseWrapper < String > deleteApc( @RequestBody APCId apcId ) {
        return iMiscService.deleteApc( apcId );
    }

    @ResponseBody
    @PostMapping( "/delete/srh" )
    public ResponseWrapper < String > deleteSrh( @RequestBody SurchargeId srhId ) {
        return iMiscService.deleteSrh( srhId );
    }

    @ResponseBody
    @GetMapping( "/refresh" )
    public ResponseWrapper < String > refreshMemory( ){
        return iMiscService.refreshMemory();
    }

}
