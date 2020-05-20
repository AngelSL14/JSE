package us.gonet.nabhi.jse.business;

import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.PaymentService;
import us.gonet.nabhi.misc.model.adp.BillsModel;
import us.gonet.nabhi.misc.model.devices.cdm.Cassette;
import us.gonet.nabhi.misc.model.jse.request.CashWithdrawalModel;
import us.gonet.nabhi.misc.model.jse.request.Generic;

import java.util.List;

public interface IValidationBusiness {

    ResponseWrapper < BillsModel > validateWithdrawal( CashWithdrawalModel generic );

    ResponseWrapper < Cassette > validateMinAmount( Generic generic );

    ResponseWrapper < List < PaymentService > > listOfPaymentServices( String catalog, String service );
}
