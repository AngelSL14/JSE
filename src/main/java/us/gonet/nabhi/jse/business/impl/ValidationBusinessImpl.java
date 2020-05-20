package us.gonet.nabhi.jse.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.business.IValidationBusiness;
import us.gonet.nabhi.jse.core.adp.Dispensed;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.exception.ServerException;
import us.gonet.nabhi.misc.model.PaymentService;
import us.gonet.nabhi.misc.model.adp.BillsModel;
import us.gonet.nabhi.misc.model.devices.cdm.Cassette;
import us.gonet.nabhi.misc.model.jse.request.CashWithdrawalModel;
import us.gonet.nabhi.misc.model.jse.request.Generic;
import us.gonet.nabhi.misc.rest.pse.PSERequester;

import java.util.ArrayList;
import java.util.List;

@Component
public class ValidationBusinessImpl implements IValidationBusiness {


    private PSERequester pseRequester;
    private Dispensed dispensed;

    @Autowired
    public ValidationBusinessImpl( PSERequester pseRequester, Dispensed dispensed ) {
        this.pseRequester = pseRequester;
        this.dispensed = dispensed;
    }

    @Override
    public ResponseWrapper < BillsModel > validateWithdrawal( CashWithdrawalModel generic ) {
        ResponseWrapper < BillsModel > wrapper = new ResponseWrapper <>();
        try {
            wrapper.addBody( dispensed.dispenseFourUnits( generic.getIp(), Integer.parseInt( generic.getCashWithAmount() ) ) );
            wrapper.setCode( "200" );
        } catch ( ServerException e ) {
            wrapper.setCode( "-500" );
            wrapper.addAllError( e.getErrors() );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < Cassette > validateMinAmount( Generic generic ) {
        ResponseWrapper < Cassette > wrapper = new ResponseWrapper <>();
        try {
            wrapper.addBody( dispensed.getMinimumAmount( generic.getIp() ) );
            wrapper.setCode( "200" );
        } catch ( ServerException e ) {
            wrapper.setCode( "-500" );
            wrapper.addAllError( e.getErrors() );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < PaymentService > > listOfPaymentServices( String catalog, String service ) {
        ResponseWrapper < List < PaymentService > > wrapper = new ResponseWrapper <>();
        try {
            wrapper.addBody( pseRequester.serviceList( "/pse/listOfServices/{api}/{catalog}/{service}", "WC", catalog, service ) );
            wrapper.setCode( "200" );
        } catch ( ServerException e ) {
            wrapper.setCode( "-500" );
            List < ErrorWS > errorWS = new ArrayList <>();
            errorWS.add( new ErrorWS( "JXI-12", "Unable to retrieved list of service" ) );
            errorWS.addAll( e.getErrors() );
            wrapper.addAllError( errorWS );
        }
        return wrapper;
    }
}
