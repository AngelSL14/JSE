package us.gonet.nabhi.jse.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import us.gonet.nabhi.misc.rest.RequestFactory;
import us.gonet.nabhi.misc.rest.header.HttpHeadersCustom;
import us.gonet.nabhi.misc.security.filter.SecurityConfig;
import us.gonet.nabhi.misc.security.filter.auth.UserAuth;
import us.gonet.nabhi.misc.security.filter.auth.impl.RepositorySecAccess;
import us.gonet.nabhi.misc.security.filter.jwt.JwtBuilder;
import us.gonet.nabhi.misc.security.filter.jwt.JwtFilter;
import us.gonet.nabhi.misc.security.filter.jwt.LoginFilter;
import us.gonet.nabhi.misc.spring.*;

@Configuration
@Import( { HttpHeadersCustom.class, AuthBean.class, RequestFactory.class, EndpointsBean.class,
        RestJKEBean.class, UtilsBean.class, RestPSEBean.class, JwtFilter.class, LoginFilter.class,
        UserAuth.class, SecurityConfig.class, RepositorySecAccess.class, JwtBuilder.class, ScheduleBean.class,
        RestISOBean.class, I8583Bean.class, JDBBean.class } )
public class JxiBean {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }

    @Bean
    public RequestFactory security() {
        return new RequestFactory();
    }

    @Bean
    public RequestFactory jkeRest() {
        return new RequestFactory();
    }

    @Bean
    public RequestFactory pseRest() {
        return new RequestFactory();
    }

    @Bean
    public RequestFactory isoRest() {
        return new RequestFactory();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
