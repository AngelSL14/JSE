package us.gonet.nabhi.jse.business;

import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jdbc.jdb.Screen;
import us.gonet.nabhi.misc.model.jse.Publicity;
import us.gonet.nabhi.misc.model.jse.request.Generic;
import us.gonet.nabhi.misc.model.jse.response.ScreenCapabilities;

public interface ICapabilitiesScreen {

    ResponseWrapper < ScreenCapabilities > getButtons( Generic generic );

    ResponseWrapper < Publicity > getPublicity( Generic generic );

    ResponseWrapper < Screen > getAllScreen( Generic generic );

    ResponseWrapper < Integer > ping( String ip );
}
