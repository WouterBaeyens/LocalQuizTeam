/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import database.LocalQuizTeamRepositoryUI;
import domain.Person;
import domain.Quiz;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
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

/**
 *
 * @author Wouter
 */
@Stateless
@Path("api/quizinfo")
public class LocalQuizTeamService {

    @EJB
    private LocalQuizTeamRepositoryUI repository;

    private String quizTeamName = "Agata Quiz Team";

    private static final Logger logger = Logger.getLogger(LocalQuizTeamService.class);
    
    @GET
    @Path("/")
    @Produces("text/html")
    //@Produces("text/plain")
    public Response index() {
        //createStub();
        Map<String, Object> map = new HashMap<>();
        updateQuizDB(getForeignQuizzes());
        map.put("members", repository.getAllPersons());
        map.put("quizzes", repository.getAllQuizzes());
        return Response.ok(new Viewable("/home", map)).build();
    }

    @POST
    @Path("member")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateMember(Person member) {
        Person storedMember = repository.getPerson(member.getEmail());
        if (storedMember == null) {
            repository.addPerson(member);
        } else {
            storedMember.setName(member.getName());
            repository.mergePerson(storedMember);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("members", repository.getAllPersons());
        map.put("quizzes", repository.getAllQuizzes());
        //return Response.ok(new Viewable("/home", map)).build();
    }

    @POST
    @Path("member")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update_FormEncoded(@Valid @BeanParam Person member) {
        updateMember(member);
        //return updateMember(member);
        return Response.ok().build();
    }

    @POST
    @Path("member/delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public void delete_FormEncoded(@BeanParam Person member) {
        Person storedPerson = repository.getPerson(member.getEmail());
        repository.removePerson(storedPerson);
        //return updateMember(member);
    }

    //reason for using path variable:
    //https://stackoverflow.com/questions/36912331/rest-delete-bad-request
    @DELETE
    @Path("member/delete/{email}")
    public void delete(@PathParam("email") String email) {
        logger.error("email = " + email);
        System.out.println("emaill = " + email);
        Person storedPerson = repository.getPerson(email);
        repository.removePerson(storedPerson);
    }
    
    @GET
    @Path("/quiz/{quizId}/subscribe/{personId}")
    @Produces("text/html")
    //@Produces("text/plain")
    public Response subscribeParticipant(@PathParam("quizId") long quizId, @PathParam("personId") String personId) {
        //createStub();
        Map<String, Object> map = new HashMap<>();
        Person person = repository.getPerson(personId);
        Quiz quiz = repository.getQuiz(quizId);
        if (person == null || quiz == null) {
            throw new IllegalArgumentException("can't subscribe this person (" + personId + ") to the quiz " + quizId  + "because either the personId or the quizId is invalid");
        }
        quiz.addParticipant(person);
        repository.mergeQuiz(quiz);
        if(quiz.getParticipants().size() >= quiz.getMinPeople()){
            subscribeToQuiz(quiz.getId());
        }
        map.put("members", repository.getAllPersons());
        map.put("quizzes", repository.getAllQuizzes());
        return Response.ok(new Viewable("/home", map)).build();
    }

    @GET
    @Path("/quiz/{quizId}/unsubscribe/{personId}")
    @Produces("text/html")
    //@Produces("text/plain")
    public Response unsubscribeParticipant(@PathParam("quizId") long quizId, @PathParam("personId") String personId) {
        //createStub();
        Map<String, Object> map = new HashMap<>();
        Person person = repository.getPerson(personId);
        Quiz quiz = repository.getQuiz(quizId);
        if (person == null || quiz == null) {
            throw new IllegalArgumentException("can't unsubscribe this person (" + personId + ") from the quiz " + quizId  + "because either the personId or the quizId is invalid");
        }
        quiz.removeParticipant(person);
        repository.mergeQuiz(quiz);
        if(quiz.getParticipants().size() < quiz.getMinPeople()){
            unsubscribeFromQuiz(quiz.getId());
        }
        map.put("members", repository.getAllPersons());
        map.put("quizzes", repository.getAllQuizzes());
        return Response.ok(new Viewable("/home", map)).build();
    }

    public Quiz[] getForeignQuizzes() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client
                .target("http://localhost:8080/LosFlippos/api/quizzes")
                .queryParam("name", "harry");
        Quiz[] response = target.request(MediaType.APPLICATION_JSON).get(Quiz[].class);
        return response;
    }

    public Response subscribeToQuiz(long quizId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/LosFlippos/api/quiz/" + quizId + "/subscribe");
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("name", quizTeamName);
        Response response = target.request().post(Entity.form(formData));
        return response;
    }

        public Response unsubscribeFromQuiz(long quizId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/LosFlippos/api/quiz/" + quizId + "/unsubscribe");
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("name", quizTeamName);
        Response response = target.request().post(Entity.form(formData));
        return response;
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
        for (Quiz quiz : quizzes) {
            Quiz storedQuiz = repository.getQuiz(quiz.getId());
            if (storedQuiz == null) {
                repository.addQuiz(quiz);
            } else {
                updateQuiz(quiz);
            }
        }
    }

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
    }

}
