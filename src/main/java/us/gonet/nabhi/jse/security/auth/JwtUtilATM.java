package us.gonet.nabhi.jse.security.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.core.memory.atm.ATMSearch;
import us.gonet.nabhi.misc.exception.ATMException;
import us.gonet.nabhi.misc.exception.JWTException;
import us.gonet.nabhi.misc.model.sec.User;
import us.gonet.nabhi.misc.security.filter.jwt.IJwtUtil;
import us.gonet.nabhi.misc.security.filter.jwt.JwtBuilder;

import static java.util.Collections.emptyList;

@Component
public class JwtUtilATM extends IJwtUtil {

    private static final Logger LOG = LoggerFactory.getLogger( JwtUtilATM.class );
    private ATMSearch atmSearch;

    public JwtUtilATM( JwtBuilder jwtBuilder, ATMSearch atmSearch ) {
        super( jwtBuilder );
        this.atmSearch = atmSearch;
    }

    @Override
    public Authentication getAuthentication( String token ) throws JWTException {
        try {
            User user = super.getJwtBuilder().verifyJwtWithHS512( token );
            if ( user.getId().contains( "@" ) ) {
                return new UsernamePasswordAuthenticationToken( user, null, emptyList() );
            }
            String device = user.getId();
            return search( device, user );
        } catch ( MalformedJwtException | ExpiredJwtException e ) {
            LOG.error( "Token expired" );
        } catch ( SignatureException e ) {
            LOG.error( "Invalid Token" );
        }
        return null;
    }

    private Authentication search( String device, User user ) {
        try {
            atmSearch.search( device );
        } catch ( ATMException e ) {
            return retry( device, user );
        }
        return new UsernamePasswordAuthenticationToken( user, null, emptyList() );
    }

    private Authentication retry( String device, User user ) {
        try {
            atmSearch.searchInDataBase( device );
            return new UsernamePasswordAuthenticationToken( user, null, emptyList() );
        } catch ( ATMException e1 ) {
            if ( user.getId().equals( "super" ) ) {
                return new UsernamePasswordAuthenticationToken( user, null, emptyList() );
            }
            LOG.error( "Invalid Token for this service" );
            return null;
        }

    }
}

