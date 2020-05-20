package us.gonet.nabhi.jse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import us.gonet.nabhi.jse.business.IAtmNotifications;
import us.gonet.nabhi.jse.journal.JournalWriter;
import us.gonet.nabhi.misc.model.jse.request.AtmNotificationModel;

@RestController
@RequestMapping( "ntf" )
@CrossOrigin( origins = { "*" }, maxAge = 6000 )
public class AtmNotificationsController {

    private IAtmNotifications bus;
    private JournalWriter journalWriter;

    private static final String[] ALLOWED_FIELDS = new String[]{ "ip", "termId", "device", "status", "extra" };

    @InitBinder( "AtmNotificationModel" )
    public void initBinder( WebDataBinder binder ) {
        binder.setAllowedFields( ALLOWED_FIELDS );
    }

    @Autowired
    public AtmNotificationsController( IAtmNotifications bus, JournalWriter journalWriter ) {
        this.bus = bus;
        this.journalWriter = journalWriter;
    }

    @ResponseBody
    @PostMapping( "" )
    public boolean atmNotification( @RequestBody AtmNotificationModel model ) {
        bus.sendToDevice( model );
        return true;
    }

    @ResponseBody
    @PostMapping( "/response" )
    public boolean atmResponse( @RequestBody AtmNotificationModel model ) {
        return bus.updateDevice( model );
    }
}
