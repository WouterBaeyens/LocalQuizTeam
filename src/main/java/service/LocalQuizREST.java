/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import remotecommunicators.RemoteQuizCommunicator;
import database.LocalQuizTeamRepositoryUI;
import domain.Person;
import domain.Quiz;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author Wouter
 */
//@RequestScoped
@Path("api/quiz")
public class LocalQuizREST {
    
    @EJB
    private LocalQuizTeamRepositoryUI repository;

    @Inject
    private RemoteQuizCommunicator quizCommunicator;
    
    private String quizTeamName = "Agata Quiz Team";
    
    @GET
    @Path("/{quizId}/subscribe/{personId}")
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
            quizCommunicator.subscribeToQuiz(quiz.getId());
        }
        map.put("members", repository.getAllPersons());
        map.put("quizzes", repository.getAllQuizzes());
        return Response.ok().build();
    }
    
        @GET
    @Path("/{quizId}/unsubscribe/{personId}")
    @Produces("text/html")
    //@Produces("text/plain")
    public Response unsubscribeParticipant(@PathParam("quizId") long quizId, @PathParam("personId") String personId) {
        //createStub();
        Person person = repository.getPerson(personId);
        Quiz quiz = repository.getQuiz(quizId);
        if (person == null || quiz == null) {
            throw new IllegalArgumentException("can't unsubscribe this person (" + personId + ") from the quiz " + quizId  + "because either the personId or the quizId is invalid");
        }
        quiz.removeParticipant(person);
        repository.mergeQuiz(quiz);
        if(quiz.getParticipants().size() < quiz.getMinPeople()){
            quizCommunicator.unsubscribeFromQuiz(quiz.getId());
        }
        return Response.ok().build();
    }
}
