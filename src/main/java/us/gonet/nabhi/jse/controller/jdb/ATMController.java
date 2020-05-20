package us.gonet.nabhi.jse.controller.jdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.gonet.nabhi.jse.business.jdb.IATMService;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.JournalQuery;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;
import us.gonet.nabhi.misc.model.jdbc.jdb.ButtonMapping;
import us.gonet.nabhi.misc.model.jdbc.jdb.Journal;
import us.gonet.nabhi.misc.model.jdbc.jdb.Screen;
import us.gonet.nabhi.misc.model.jdbc.jdb.up.ATDUp;

import java.util.List;

@RestController
@RequestMapping( "/atm" )
public class ATMController {

    private IATMService atmService;

    @Autowired
    public ATMController( IATMService atmService ) {
        this.atmService = atmService;
    }

    @ResponseBody
    @GetMapping( "/{id:.+}" )
    public ResponseWrapper < ATD > findATM( @PathVariable String id ) {
        return atmService.findATM( id );
    }

    @ResponseBody
    @GetMapping( "/{id:.+}/{fiid:.+}" )
    public ResponseWrapper < ATD > findATM( @PathVariable String id, @PathVariable String fiid ) {
        return atmService.findATM( id, fiid );
    }

    @ResponseBody
    @GetMapping( "up/{id:.+}/{fiid:.+}" )
    public ResponseWrapper < ATDUp > findATMUpTime( @PathVariable String id, @PathVariable String fiid ) {
        return atmService.findATMUpTime( id, fiid );
    }

    @ResponseBody
    @GetMapping( "/findAll" )
    public ResponseWrapper < List < ATD > > findAllATM() {
        return atmService.findAll();
    }

    @ResponseBody
    @GetMapping( "/findAll/{fiid:.+}" )
    public ResponseWrapper < List < ATD > > findAllATM( @PathVariable String fiid ) {
        return atmService.findAll( fiid );
    }

    @ResponseBody
    @GetMapping( "up/findAll/{fiid:.+}" )
    public ResponseWrapper < List < ATDUp > > findAllATMUpTime( @PathVariable String fiid ) {
        return atmService.findAllUpTime( fiid );
    }

    @ResponseBody
    @PostMapping( "/save/journal" )
    public ResponseWrapper < String > saveJournal( @RequestBody Journal journal ) {
        return atmService.saveJournal( journal );
    }

    @ResponseBody
    @PostMapping( "/save/journals" )
    public ResponseWrapper < String > saveJournals( @RequestBody List < Journal > journal ) {
        return atmService.saveJournal( journal );
    }

    @ResponseBody
    @PostMapping( "/save/screen" )
    public ResponseWrapper < Screen > saveScreen( @RequestBody Screen screen ) {
        return atmService.saveScreen( screen );
    }

    @ResponseBody
    @PostMapping( "/save/screen/single/{id:.+}" )
    public ResponseWrapper < String > saveSingleScreen( @PathVariable String id, @RequestBody ButtonMapping single ) {
        return atmService.saveSingleScreen( id, single );
    }

    @ResponseBody
    @PostMapping( "/find/journal/query" )
    public ResponseWrapper < List < Journal > > findJournalQuery( @RequestBody JournalQuery query ) {
        return atmService.findByQuery( query );
    }

    @ResponseBody
    @GetMapping( "/jfindAll/{id:.+}" )
    public ResponseWrapper < List < Journal > > findJournalQuery( @PathVariable String id ) {
        return atmService.find( id );
    }

    @ResponseBody
    @PostMapping( "/find/journal/query/{fiid:.+}" )
    public ResponseWrapper < List < Journal > > findJournalQuery( @PathVariable String fiid, @RequestBody JournalQuery query ) {
        return atmService.findByQuery( query, fiid );
    }

    @ResponseBody
    @GetMapping( "/findAll/{id:.+}/{fiid:.+}" )
    public ResponseWrapper < List < Journal > > findJournalQuery( @PathVariable String id, @PathVariable String fiid ) {
        return atmService.find( id, fiid );
    }

}
