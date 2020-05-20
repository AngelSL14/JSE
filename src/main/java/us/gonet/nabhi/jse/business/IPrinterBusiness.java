package us.gonet.nabhi.jse.business;

import us.gonet.nabhi.misc.model.jse.request.Generic;
import us.gonet.nabhi.misc.model.jse.response.Ticket;

public interface IPrinterBusiness {

    Ticket printingTicket( Generic generic );

}
