package us.gonet.nabhi.jse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import us.gonet.nabhi.jse.business.IValidationBusiness;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.PaymentService;
import us.gonet.nabhi.misc.model.adp.BillsModel;
import us.gonet.nabhi.misc.model.devices.cdm.Cassette;
import us.gonet.nabhi.misc.model.jse.request.CashWithdrawalModel;
import us.gonet.nabhi.misc.model.jse.request.Generic;

import java.util.List;

@RestController
@RequestMapping( "vldtn" )
@CrossOrigin( origins = { "*" }, maxAge = 6000 )
public class ValidationController {

    private static final String[] ALLOWED_FIELDS = new String[]{ "ip", "cashWithAmount" };
    private IValidationBusiness business;

    @InitBinder( "Generic" )
    public void initBinder( WebDataBinder binder ) {
        binder.setAllowedFields( ALLOWED_FIELDS );
    }

    @Autowired
    public ValidationController( IValidationBusiness business ) {
        this.business = business;
    }

    @PostMapping( "wthdw" )
    @ResponseBody
    public ResponseWrapper < BillsModel > validateWithdrawal( @RequestBody CashWithdrawalModel generic ) {
        return business.validateWithdrawal( generic );
    }

    @PostMapping( "mntMnm" )
    @ResponseBody
    public ResponseWrapper < Cassette > validateMinAmount( @RequestBody Generic generic ) {
        return business.validateMinAmount( generic );
    }

    @GetMapping( "/listOfServices/{catalog:.+}/{service:.+}" )
    @ResponseBody
    public ResponseWrapper < List < PaymentService > > listOfService( @PathVariable String catalog, @PathVariable String service ) {
        return business.listOfPaymentServices( catalog, service );
    }
}
