package com.kumana.iotp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties("connector")
@Component
public class ConnectorConfig implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(ConnectorConfig.class);

    private String url;
    private String user;
    private String pass;
    private String clientid;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (url == null) {
            logger.error("connector.url is missing");
            throw new Exception("connector url is missing");
        }
        if (user == null) {
            logger.error("connector.user is missing");
            throw new Exception("connector user is missing");
        }
        if (pass == null) {
            logger.error("connector.pass is missing");
            throw new Exception("connector password is missing");
        }
        if (clientid == null) {
            logger.error("connector.clientid is missing");
            throw new Exception("connector clientid is missing");
        }
    }


}
