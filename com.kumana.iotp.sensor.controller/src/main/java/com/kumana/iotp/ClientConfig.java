package com.kumana.iotp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties("client")
@Component
public class ClientConfig implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(ClientConfig.class);

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
            logger.error("client.url is missing");
            throw new Exception("client url is missing");
        }
        if (user == null) {
            logger.error("client.user is missing");
            throw new Exception("client user is missing");
        }
        if (pass == null) {
            logger.error("client.pass is missing");
            throw new Exception("client pass is missing");
        }
        if (clientid == null) {
            logger.error("client.clientid is missing");
            throw new Exception("clientid is missing");
        }
    }
}
