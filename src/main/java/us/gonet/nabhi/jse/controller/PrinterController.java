package us.gonet.nabhi.jse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import us.gonet.nabhi.jse.business.IPrinterBusiness;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jse.request.Generic;
import us.gonet.nabhi.misc.model.jse.response.Ticket;

@RestController
@RequestMapping( "prntrcntrllr" )
@CrossOrigin( origins = { "*" }, maxAge = 6000 )
public class PrinterController {

    private IPrinterBusiness business;

    private static final String[] ALLOWED_FIELDS = new String[]{ "ip", "termId" };

    @InitBinder( "Generic" )
    public void initBinder( WebDataBinder binder ) {
        binder.setAllowedFields( ALLOWED_FIELDS );
    }

    @Autowired
    public PrinterController( IPrinterBusiness business ) {
        this.business = business;
    }

    @ResponseBody
    @PostMapping( "/prntng" )
    public ResponseWrapper < Ticket > printingTicket( @RequestBody Generic generic ) {
        ResponseWrapper < Ticket > response = new ResponseWrapper <>();
        response.setCode( "200" );
        response.addBody( business.printingTicket( generic ) );
        return response;
    }
}