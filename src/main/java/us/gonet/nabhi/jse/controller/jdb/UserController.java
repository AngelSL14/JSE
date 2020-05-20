package us.gonet.nabhi.jse.controller.jdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import us.gonet.nabhi.jse.business.jdb.IUserService;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jdbc.jdb.DashboardUsers;
import us.gonet.nabhi.misc.model.jdbc.jdb.TokenUsers;

@RestController
@RequestMapping( "/user" )
@CrossOrigin( origins = { "*" }, maxAge = 6000 )
public class UserController {

    private IUserService userService;
    private static final String[] ALLOWED_FIELDS = new String[]{ "id", "clave", "rol" };

    @InitBinder( "TokenUser" )
    public void initBinder( WebDataBinder binder ) {
        binder.setAllowedFields( ALLOWED_FIELDS );
    }

    @Autowired
    public UserController( IUserService userService ) {
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping( "/save" )
    public ResponseWrapper < TokenUsers > saveTokenUser( @RequestBody TokenUsers tokenUser ) {
        return userService.saveTokenUser( tokenUser );
    }

    @ResponseBody
    @GetMapping( "/findById/{id:.+}" )
    public ResponseWrapper < TokenUsers > findTokenById( @PathVariable String id ) {
        return userService.findTokenById( id );
    }

    @ResponseBody
    @PostMapping( "/save/dash" )
    public ResponseWrapper < DashboardUsers > saveDash( @RequestBody DashboardUsers dashboardUsers ) {
        return userService.saveDashUser( dashboardUsers );
    }

    @ResponseBody
    @GetMapping( "/findByEmail/{email:.+}" )
    public ResponseWrapper < DashboardUsers > findUserByEmail( @PathVariable String email ) {
        return userService.findDashByEmail( email );
    }

    @ResponseBody
    @GetMapping( "/deleteByEmail/{email:.+}" )
    public ResponseWrapper < String > deleteUserByEmail( @PathVariable String email ) {
        return userService.deleteDashByEmail( email );
    }
}
