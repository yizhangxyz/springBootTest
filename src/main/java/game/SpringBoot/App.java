package game.SpringBoot;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 *
 */

@SpringBootApplication
public class App 
{
    public static void main(String[] args) 
    {
    	SpringApplication.run(App.class, args);
    }
    
    @Bean
    public TomcatServletWebServerFactory  servletContainer()
    {
    	TomcatServletWebServerFactory  tomcat  = new TomcatServletWebServerFactory();
    	tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer()
        {
        	@Override
            public void customize(Connector connector)
        	{
                //((AbstractProtocol) connector.getProtocolHandler()).setConnectionTimeout(100);
            }
        });

        return tomcat;
    }

}