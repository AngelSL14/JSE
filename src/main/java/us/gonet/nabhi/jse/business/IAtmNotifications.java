package us.gonet.nabhi.jse.business;

import us.gonet.nabhi.misc.model.jse.request.AtmNotificationModel;

public interface IAtmNotifications {

    boolean updateDevice( AtmNotificationModel model );

    void sendToDevice( AtmNotificationModel model );
}
