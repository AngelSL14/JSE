package us.gonet.nabhi.jse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import us.gonet.nabhi.jse.business.IAtmTerminalPinKey;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jke.TmkEntity;
import us.gonet.nabhi.misc.model.jse.request.Generic;

@RestController
@RequestMapping( "tmkkey" )
@CrossOrigin( origins = { "*" }, maxAge = 6000 )
public class ATMTerminalPinKeyController {

    private IAtmTerminalPinKey terminalPinKey;
    private static final String[] ALLOWED_FIELDS = new String[]{ "ip" };

    @InitBinder( "Generic" )
    public void initBinder( WebDataBinder binder ) {
        binder.setAllowedFields( ALLOWED_FIELDS );
    }

    @Autowired
    public ATMTerminalPinKeyController( IAtmTerminalPinKey terminalPinKey ) {
        this.terminalPinKey = terminalPinKey;
    }

    @ResponseBody
    @PostMapping( "/atmData" )
    public ResponseWrapper < TmkEntity > incomingAtm( @RequestBody Generic atm ) {
        return terminalPinKey.tpkRequest( atm );
    }
}
