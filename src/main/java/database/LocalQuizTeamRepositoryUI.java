/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Person;
import domain.Quiz;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Wouter
 */
@Remote
public interface LocalQuizTeamRepositoryUI {

    void addPerson(Person person);

    void addQuiz(Quiz quiz);

    List<Person> getAllPersons();

    List<Quiz> getAllQuizzes();

    Person getPerson(String email);

    public Person getPersonByName(String name);
    
    Quiz getQuiz(long id);

    void mergePerson(Person person);

    void mergeQuiz(Quiz quiz);

    void removePerson(Person person);

    void removeQuiz(Quiz quiz);
    
}
