/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import database.LocalQuizTeamRepositoryUI;
import domain.Person;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

/**
 *
 * @author Wouter
 */
@Stateless
@Path("api/member")
public class LocalQuizMembersREST {
    
    @EJB
    private LocalQuizTeamRepositoryUI repository;

    private static final Logger logger = Logger.getLogger(LocalQuizMembersREST.class);

    
    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response update_FormEncoded(@Valid @BeanParam Person member) {
        Person storedMember = repository.getPerson(member.getEmail());
        if (storedMember != null) {
            storedMember.setName(member.getName());
            repository.mergePerson(storedMember);

        } else if(repository.getPersonByName(member.getName()) != null){
            return Response.status(Response.Status.PRECONDITION_FAILED).entity("duplicate names are not allowed! Maybe try " + member.getName()+"2 for example.").build();
        } else{
            repository.addPerson(member);
        }
        return Response.ok().build();
    }
    
    //reason for using path variable:
    //https://stackoverflow.com/questions/36912331/rest-delete-bad-request
    @DELETE
    @Path("/delete/{email}")
    public Response delete(@PathParam("email") String email) {
        logger.error("email = " + email);
        System.out.println("emaill = " + email);
        Person storedPerson = repository.getPerson(email);
        if(storedPerson == null){
                return Response.status(Response.Status.NOT_FOUND).entity("no person with email \"" + email + "\" was found").build();

        }
        repository.removePerson(storedPerson);
        return Response.ok().build();
    }
    
    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("email") String email) {
        logger.error("email = " + email);
        System.out.println("emaill = " + email);
        Person storedPerson = repository.getPerson(email);
        if(storedPerson == null){
            return Response.status(Response.Status.NOT_FOUND).entity("no person with email \"" + email + "\" was found").build();
        }
        return Response.ok(storedPerson).build();
    }
    
    @GET
    @Path("/byName/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByName(@PathParam("name") String name) {
        logger.error("email = " + name);
        System.out.println("emaill = " + name);
        Person storedPerson = repository.getPersonByName(name);
        if(storedPerson == null){
            return Response.status(Response.Status.NOT_FOUND).entity("no person with email \"" + name + "\" was found").build();
        }
        return Response.ok(storedPerson).build();
    }
}