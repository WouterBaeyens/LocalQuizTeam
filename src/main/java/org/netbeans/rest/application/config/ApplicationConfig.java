/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ejb.Singleton;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ExceptionMapper;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import remotecommunicators.RemoteQuizCommunicator;
import service.Unused_ApplicationBinder;

/**
 *
 * @author Wouter
 */
@javax.ws.rs.ApplicationPath("webresources")
//public class ApplicationConfig extends Application {
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig(){
        register(service.LocalQuizTeamService.class);
        //reason for this property is to avoid general 400-error on bean validation
        //source: https://stackoverflow.com/questions/25755773/bean-validation-400-errors-are-returning-default-error-page-html-instead-of-re
        //property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, "true");
          //property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
          //property(ServerProperties.BV_FEATURE_DISABLE, true);
          
        register(service.ConstraintViolationMapper.class);
        //register(new Unused_ApplicationBinder());
        //register(RemoteQuizCommunicator.class);
        
        /*register(new AbstractBinder() {
            @Override
protected void configure() {
    bind(ConstraintViolationMapper.class).to(ExceptionMapper.class)
            .in(Singleton.class);
}
        });*/
                }
    /*@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }*/

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    /*private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(service.LocalQuizTeamService.class);
    }*/
    
}
