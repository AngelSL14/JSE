package us.gonet.nabhi.jse.business.jdb.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.business.jdb.IUserService;
import us.gonet.nabhi.jse.core.jdb.SanitizeModel;
import us.gonet.nabhi.jse.utils.StreamFilter;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.exception.SanitazeException;
import us.gonet.nabhi.misc.exception.ServerException;
import us.gonet.nabhi.misc.jdb.entity.dash.DashboardUsersEntity;
import us.gonet.nabhi.misc.jdb.entity.token.TokenUsersDetailEntity;
import us.gonet.nabhi.misc.jdb.entity.token.TokenUsersEntity;
import us.gonet.nabhi.misc.jdb.repository.dash.DashboardUserRepository;
import us.gonet.nabhi.misc.jdb.repository.token.TokenUsersDetailRepository;
import us.gonet.nabhi.misc.jdb.repository.token.TokenUsersRepository;
import us.gonet.nabhi.misc.model.jdbc.jdb.DashboardUsers;
import us.gonet.nabhi.misc.model.jdbc.jdb.TokenUsers;

import java.util.Date;
import java.util.Optional;

@Component
public class UserService implements IUserService {

    private TokenUsersRepository tokenUsersRepository;
    private TokenUsersDetailRepository tokenUsersDetailRepository;
    private DashboardUserRepository dashboardUserRepository;
    private ModelMapper mapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SanitizeModel sanitize;
    private StreamFilter filter;

    @Autowired
    public UserService( TokenUsersRepository tokenUsersRepository, TokenUsersDetailRepository tokenUsersDetailRepository, DashboardUserRepository dashboardUserRepository, ModelMapper mapper, BCryptPasswordEncoder bCryptPasswordEncoder, SanitizeModel sanitize, StreamFilter filter ) {
        this.tokenUsersRepository = tokenUsersRepository;
        this.tokenUsersDetailRepository = tokenUsersDetailRepository;
        this.dashboardUserRepository = dashboardUserRepository;
        this.mapper = mapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sanitize = sanitize;
        this.filter = filter;
    }

    @Override
    public ResponseWrapper < TokenUsers > saveTokenUser( TokenUsers tokenUser ) {
        ResponseWrapper < TokenUsers > wrapper = new ResponseWrapper <>();
        try {
            sanitize.sanitize( tokenUser );
        } catch ( SanitazeException e ) {
            wrapper.setCode( "96" );
            wrapper.addAllError( e.getErrors() );
            return wrapper;
        }
        tokenUser.setClave( ( bCryptPasswordEncoder.encode( tokenUser.getClave() ) ) );
        tokenUsersDetailRepository.save( mapper.map( tokenUser.getUserDetail(), TokenUsersDetailEntity.class ) );
        TokenUsersEntity entity = tokenUsersRepository.save( mapper.map( tokenUser, TokenUsersEntity.class ) );
        wrapper.setCode( "00" );
        wrapper.addBody( mapper.map( entity, TokenUsers.class ) );
        return wrapper;
    }

    @Override
    public ResponseWrapper < TokenUsers > findTokenById( String id ) {
        ResponseWrapper < TokenUsers > wrapper = new ResponseWrapper <>();
        Optional < TokenUsersEntity > token;
        try {
            token = tokenUsersRepository.findById( filter.sanitizeString( id ) );
            if ( token.isPresent() ) {
                token.get().getUserDetail().setLastUsage( new Date().getTime() / 1000 );
                tokenUsersDetailRepository.save( token.get().getUserDetail() );
                wrapper.setCode( "00" );
                wrapper.addBody( mapper.map( token.get(), TokenUsers.class ) );
            } else {
                wrapper.setCode( "96" );
                wrapper.addError( new ErrorWS( "JDB-02", "User not exists" ) );
            }
            return wrapper;
        } catch ( ServerException e ) {
            wrapper.setCode( "96" );
            wrapper.addAllError( e.getErrors() );
            return wrapper;
        }
    }

    @Override
    public ResponseWrapper < DashboardUsers > saveDashUser( DashboardUsers dashboardUser ) {
        ResponseWrapper < DashboardUsers > wrapper = new ResponseWrapper <>();
        try {
            sanitize.sanitize( dashboardUser );
        } catch ( SanitazeException e ) {

            wrapper.setCode( "97" );
            wrapper.addAllError( e.getErrors() );
            return wrapper;
        }
        dashboardUser.setPassword( ( bCryptPasswordEncoder.encode( dashboardUser.getPassword() ) ) );
        DashboardUsersEntity entity = dashboardUserRepository.save( mapper.map( dashboardUser, DashboardUsersEntity.class ) );
        wrapper.setCode( "00" );
        wrapper.addBody( mapper.map( entity, DashboardUsers.class ) );
        return wrapper;
    }


    @Override
    public ResponseWrapper < DashboardUsers > findDashByEmail( String email ) {
        ResponseWrapper < DashboardUsers > wrapper = new ResponseWrapper <>();
        DashboardUsersEntity token;
        try {
            token = dashboardUserRepository.findByEmail( filter.sanitizeString( email, "[a-zA-Z0-9 .=*:,/_@]+" ) );
            if ( token != null ) {
                token.setLastUsage( new Date().getTime() / 1000 );
                dashboardUserRepository.save( token );
                wrapper.setCode( "00" );
                wrapper.addBody( mapper.map( token, DashboardUsers.class ) );
            } else {
                wrapper.setCode( "96" );
                wrapper.addError( new ErrorWS( "JDB-02", "User not exists" ) );
            }
            return wrapper;
        } catch ( ServerException e ) {
            wrapper.setCode( "96" );
            wrapper.addAllError( e.getErrors() );
            return wrapper;
        }
    }

    @Override
    public ResponseWrapper < String > deleteDashByEmail( String email ) {
        ResponseWrapper < String > wrapper = new ResponseWrapper <>();
        DashboardUsersEntity token;
        try {
            filter.sanitizeString( email, "[a-zA-Z0-9 .=*:,/_@]+" );
            token = dashboardUserRepository.findByEmail( email );
            if ( token != null ) {
                dashboardUserRepository.delete( token );
                wrapper.setCode( "00" );
                wrapper.addBody( "OK" );
            } else {
                wrapper.setCode( "96" );
                wrapper.addError( new ErrorWS( "JDB-02", "User not exists" ) );
            }
            return wrapper;
        } catch ( ServerException e ) {
            wrapper.setCode( "96" );
            wrapper.addAllError( e.getErrors() );
            return wrapper;
        }
    }
}
