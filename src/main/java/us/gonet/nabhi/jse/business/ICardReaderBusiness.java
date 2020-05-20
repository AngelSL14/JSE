package us.gonet.nabhi.jse.business;

import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jse.request.AtmInfo;
import us.gonet.nabhi.misc.model.jse.request.Generic;
import us.gonet.nabhi.misc.model.jse.response.CardInfo;

public interface ICardReaderBusiness {
    ResponseWrapper < Generic > incomingCard( Generic generic );

    ResponseWrapper < CardInfo > validatingCard( AtmInfo atmInfo );

    void cardRemoved( AtmInfo atmInfo );
}
