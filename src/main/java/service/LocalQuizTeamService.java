/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.LocalQuizTeamRepositoryUI;
import domain.Person;
import domain.Quiz;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.mvc.Viewable;
import remotecommunicators.RemoteQuizCommunicator;

/**
 *
 * @author Wouter
 */
@Stateless
@Path("api/")
public class LocalQuizTeamService {

    @EJB
    private LocalQuizTeamRepositoryUI repository;

    @Inject
    private RemoteQuizCommunicator quizCommunicator;
    
    private String quizTeamName = "Agata Quiz Team";

    private static final Logger logger = Logger.getLogger(LocalQuizTeamService.class);
    
    @GET
    @Path("/")
    @Produces("text/html")
    //@Produces("text/plain")
    public Response index() {
        Map<String, Object> map = new HashMap<>();
        /*try{
            updateQuizDB(quizCommunicator.getForeignQuizzes());
        } catch(NotFoundException e){
            map.put("errorMessage", "Couldn't reach the LosFlippos Database for updates, so the quizzes shown here may be outdated");
        } catch(Exception e){
            map.put("errorMessage", "error updating with data from LosFlippos: " + e.getLocalizedMessage());
        }*/
        map.put("members", repository.getAllPersons());
        map.put("quizzes", repository.getAllQuizzes());
        return Response.ok(new Viewable("/index", map)).build();
    }
    
        @POST
    @Path("/update")
    @Produces("text/html")
    //@Produces("text/plain")
    public Response updateDB() throws JsonProcessingException {
        try{
            updateQuizDB(quizCommunicator.getForeignQuizzes());
        } catch(NotFoundException e){
            return Response.status(404).entity("Couldn't reach the LosFlippos Database for updates, so the quizzes shown here may be outdated").build();
        } catch(Exception e){
            return Response.status(500).entity("error updating with data from LosFlippos: " + e.getLocalizedMessage()).build();
        }
        List<Quiz> quizzes = repository.getAllQuizzes();
        String json = new ObjectMapper().writeValueAsString(quizzes);
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }
    
    public void updateQuiz(Quiz quiz) {
        if (repository.getQuiz(quiz.getId()) == null) {
            repository.addQuiz(quiz);
        } else {
            Quiz quizToUpdate = repository.getQuiz(quiz.getId());
            quizToUpdate.setAdress(quiz.getAdress());
            quizToUpdate.setDate(quiz.getDate());
            quizToUpdate.setMinPeople(quiz.getMinPeople());
            quizToUpdate.setName(quiz.getName());
            repository.mergeQuiz(quizToUpdate);
        }
    }

    public void updateQuizDB(Quiz[] quizzes) {
        List<Quiz> localQuizzes = repository.getAllQuizzes();
        for (Quiz quiz : quizzes) {
            Optional<Quiz> storedQuiz = localQuizzes.stream().filter(q -> ((Quiz) q).getId() == quiz.getId()).findFirst();
            //Quiz storedQuiz = repository.getQuiz(quiz.getId());
            //if (storedQuiz == null) {
            if(!storedQuiz.isPresent()){
            repository.addQuiz(quiz);
            } else {
                localQuizzes.remove(storedQuiz.get());
                updateQuiz(quiz);
            }
        }
        for(Quiz quiz : localQuizzes){
            //Removes the quizzes that are no longer present/active in the main (losflippos) database
            repository.removeQuiz(quiz);
        }
    }

    /*
    public void createStub() {
        Quiz quiz = new Quiz();
        quiz.setAdress("so far out I can't even see it on my map!");
        quiz.setDate(Date.valueOf(LocalDate.now()));
        quiz.setName("the greatest quiz on earth");
        quiz.setMinPeople(150);
        repository.addQuiz(quiz);
        Quiz quiz2 = new Quiz();
        quiz2.setAdress("we have yet to figure out a location");
        quiz2.setDate(Date.valueOf(LocalDate.now().plusYears(80)));
        quiz2.setMinPeople(1);
        quiz2.setName("The quiz for the smartestest people");
        repository.addQuiz(quiz2);
    }*/
}
