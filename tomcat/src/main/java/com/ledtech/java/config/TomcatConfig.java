package com.ledtech.java.config;

import com.ledtech.java.model.TomcatProperties;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName:TomcatConfig
 * @Author:DK_Li
 * @Date:Created in 2019/5/2 0:20
 * @Description:tomcat的配置类
 * @Version:1.0
 */
@Configuration
public class TomcatConfig {

    private TomcatProperties tomcatProperties;

    @Autowired
    public void setTomcatProperties(TomcatProperties tomcatProperties) {
        this.tomcatProperties = tomcatProperties;
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(redirectConnector());
        return tomcat;
    }

    private Connector redirectConnector() {
        Connector connector = new Connector(tomcatProperties.getProtocol());
        connector.setAttribute("acceptorThreadCount",tomcatProperties.getAcceptorThreadCount());
        connector.setAttribute("minSpareThreads",tomcatProperties.getMinSpareThreads());
        connector.setAttribute("maxSpareThreads",tomcatProperties.getMaxSpareThreads());
        connector.setAttribute("maxThreads",tomcatProperties.getMaxThreads());
        connector.setAttribute("maxConnections",tomcatProperties.getMaxConnections());
        connector.setAttribute("connectionTimeout",tomcatProperties.getConnectionTimeout());
        connector.setAttribute("redirectPort",tomcatProperties.getRedirectPort());
        connector.setAttribute("compression",tomcatProperties.getCompression());
        connector.setAttribute("address",tomcatProperties.getAddress());
        connector.setAttribute("maxFileSize",tomcatProperties.getMaxFileSize());
        connector.setAttribute("maxRequestSize",tomcatProperties.getMaxRequestSize());
        return connector;
    }
}