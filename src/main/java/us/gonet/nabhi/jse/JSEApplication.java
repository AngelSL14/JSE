package us.gonet.nabhi.jse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import us.gonet.nabhi.jse.config.Initializer;
import us.gonet.nabhi.misc.config.EndpointConfig;


@SpringBootApplication
public class JSEApplication extends SpringBootServletInitializer implements CommandLineRunner {

    private EndpointConfig endpointConfig;
    private Initializer initializer;

    @Autowired
    public void setBean( EndpointConfig endpointConfig, Initializer initializer ) {
        this.endpointConfig = endpointConfig;
        this.initializer = initializer;
    }


    @Override
    protected SpringApplicationBuilder configure( SpringApplicationBuilder application ) {
        return application.sources( JSEApplication.class );
    }

    public static void main( String[] args ) {
        SpringApplication.run( JSEApplication.class );
    }

    public void run( String... args ) {
        endpointConfig.init();
        initializer.init();
        initializer.setATM();
    }

}
