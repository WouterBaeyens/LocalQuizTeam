/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecommunicators;

import domain.Quiz;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 *
 * @author Wouter
 */
//@Default
//@Stateless
@Dependent
public class RemoteQuizCommunicator{
    
    private static final String BASEURL ="http://localhost:8080/LosFlippos/api/quiz/";
    private static final String quizTeamName = "Agata Quiz Team";    
    
    public RemoteQuizCommunicator(){}
    
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
        WebTarget target = client.target(BASEURL + quizId + "/subscribe");
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("name", quizTeamName);
        Response response = target.request().post(Entity.form(formData));
        return response;
    }

    public Response unsubscribeFromQuiz(long quizId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASEURL + quizId + "/unsubscribe");
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("name", quizTeamName);
        Response response = target.request().post(Entity.form(formData));
        return response;
    }
        
        
}
