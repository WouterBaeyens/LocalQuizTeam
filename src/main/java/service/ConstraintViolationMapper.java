/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

//@Singleton
@Provider
public class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {

    @Context
    private ResourceInfo resourceInfo;
    
   
  @Override
  public Response toResponse(ConstraintViolationException e) {
    // There can be multiple constraint Violations
    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
    List<String> messages = new ArrayList<>();
    for (ConstraintViolation<?> violation : violations) {
        messages.add(" - " + violation.getMessage()); // this is the message you are actually looking for

    }
    //GenericEntity<List<String>> entity = new GenericEntity<List<String>>(messages) {};
    String messagesAsBulletPoints = String.join("\n", messages);
    return Response.status(Status.BAD_REQUEST).entity(messagesAsBulletPoints).build();
  }

}
