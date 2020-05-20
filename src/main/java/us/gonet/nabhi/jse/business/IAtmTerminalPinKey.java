package us.gonet.nabhi.jse.business;

import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jke.TmkEntity;
import us.gonet.nabhi.misc.model.jse.request.Generic;


public interface IAtmTerminalPinKey {
    ResponseWrapper < TmkEntity > tpkRequest( Generic atmIp );
}
