package us.gonet.nabhi.jse.business.jdb;

import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jdbc.jdb.DashboardUsers;
import us.gonet.nabhi.misc.model.jdbc.jdb.TokenUsers;

public interface IUserService {

    ResponseWrapper < TokenUsers > saveTokenUser( TokenUsers tokenUser );

    ResponseWrapper < TokenUsers > findTokenById( String id );

    ResponseWrapper < DashboardUsers > saveDashUser( DashboardUsers dashboardUser );

    ResponseWrapper < DashboardUsers > findDashByEmail( String email );

    ResponseWrapper < String > deleteDashByEmail( String email );
}
