package us.gonet.nabhi.jse.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import us.gonet.nabhi.misc.security.AllowedUrl;
import us.gonet.nabhi.misc.security.filter.SecurityConfig;
import us.gonet.nabhi.misc.security.filter.auth.SecurityAccess;
import us.gonet.nabhi.misc.security.filter.auth.UserAuth;
import us.gonet.nabhi.misc.security.filter.jwt.JwtFilter;
import us.gonet.nabhi.misc.security.filter.jwt.LoginFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity( prePostEnabled = true )
@CrossOrigin( origins = "10.255.11.148", allowedHeaders = "*", maxAge = 6000 )
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserAuth userAuth;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private LoginFilter loginFilter;
    private JwtFilter jwtFilter;
    private SecurityConfig securityConfig;

    private static final String URL = "/auth";

    @Autowired
    public WebSecurityConfig( UserAuth userAuth, LoginFilter loginFilter, JwtFilter jwtFilter, BCryptPasswordEncoder bCryptPasswordEncoder, SecurityAccess access, SecurityConfig securityConfig ) {
        this.userAuth = userAuth;
        this.loginFilter = loginFilter;
        this.jwtFilter = jwtFilter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userAuth.setJdb( access );
        this.securityConfig = securityConfig;
    }

    @Override
    protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( userAuth ).passwordEncoder( bCryptPasswordEncoder );
    }

    @Override
    public void configure( WebSecurity web ) {
        securityConfig.configureWebSecurity( web );
    }

    @Override
    protected void configure( final HttpSecurity http ) throws Exception {
        loginFilter.init( URL, authenticationManager() );
        securityConfig.configureHttp( http, URL, loginFilter, jwtFilter, new AllowedUrl(
                HttpMethod.POST,
                "/capa/bilities",
                "/capa/publicity" ) );
    }
}
