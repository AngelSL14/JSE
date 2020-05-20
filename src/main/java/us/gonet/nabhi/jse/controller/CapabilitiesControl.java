package us.gonet.nabhi.jse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import us.gonet.nabhi.jse.business.ICapabilitiesScreen;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jdbc.jdb.Screen;
import us.gonet.nabhi.misc.model.jse.Publicity;
import us.gonet.nabhi.misc.model.jse.request.Generic;
import us.gonet.nabhi.misc.model.jse.response.ScreenCapabilities;

@RestController
@RequestMapping( "capa" )
@CrossOrigin( origins = { "*" }, maxAge = 6000 )
public class CapabilitiesControl {

    private ICapabilitiesScreen iCapabilitiesScreen;

    private static final String[] ALLOWED_FIELDS = new String[]{ "ip" };

    @InitBinder( "Generic" )
    public void initBinder( WebDataBinder binder ) {
        binder.setAllowedFields( ALLOWED_FIELDS );
    }

    @Autowired
    public CapabilitiesControl( ICapabilitiesScreen iCapabilitiesScreen ) {
        this.iCapabilitiesScreen = iCapabilitiesScreen;
    }

    @ResponseBody
    @PostMapping( "/bilities" )
    public ResponseWrapper < ScreenCapabilities > returnCapabilities( @RequestBody Generic generic ) {
        return iCapabilitiesScreen.getButtons( generic );
    }

    @ResponseBody
    @PostMapping( "/publicity" )
    public ResponseWrapper < Publicity > returnPublicity( @RequestBody Generic generic ) {
        return iCapabilitiesScreen.getPublicity( generic );
    }

    @ResponseBody
    @PostMapping( "/screen" )
    public ResponseWrapper < Screen > getAllScreen( @RequestBody Generic generic ) {
        return iCapabilitiesScreen.getAllScreen( generic );
    }

    @ResponseBody
    @PostMapping( "         " )
    public ResponseWrapper < Integer > ping( @RequestBody String ip ) {
        return iCapabilitiesScreen.ping( ip );
    }
}
