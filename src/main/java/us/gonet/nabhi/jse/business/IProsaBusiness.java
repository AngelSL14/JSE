package us.gonet.nabhi.jse.business;

import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.exception.ServerException;
import us.gonet.nabhi.misc.model.adp.BillsModel;
import us.gonet.nabhi.misc.model.jse.request.*;
import us.gonet.nabhi.misc.model.jse.response.GenericProcess;

import java.util.Map;

public interface IProsaBusiness {
    ResponseWrapper < BillsModel > cashWithAuth( CashWithdrawalModel generic ) throws ServerException;

    ResponseWrapper < Generic > getCommission( AtmInfo generic ) throws ServerException;

    ResponseWrapper < Map < String, String > > balInquiryAuth( Generic generic ) throws ServerException;

    ResponseWrapper < String > listTrx( Generic generic ) throws ServerException;

    ResponseWrapper < GenericProcess > changePin( ChangeNipModel generic ) throws ServerException;

    ResponseWrapper < GenericProcess > genericSale( GenericSaleModel generic ) throws ServerException;


}
