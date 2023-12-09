package com.soa.secondservice.jndi;

import com.soa.ejb.service.RemoteAdditionalService;

import jakarta.ws.rs.NotFoundException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import java.util.Properties;

public class JNDIConfig {
    public static RemoteAdditionalService additionalService() {
        Properties jndiProps = new Properties();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProps.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
        try {
            final Context context = new InitialContext(jndiProps);
            return  (RemoteAdditionalService) context.lookup("ejb:/ejb-1.0-SNAPSHOT/AdditionalService!com.soa.ejb.service.RemoteAdditionalService");
        } catch (NamingException e){
            System.out.println("Unable to find bean");
            e.printStackTrace();
            throw new NotFoundException();
        }
    }
}
