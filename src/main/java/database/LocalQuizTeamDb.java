/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Person;
import domain.Quiz;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;


/**
 *
 * @author Wouter
 */
@Stateless
public class LocalQuizTeamDb implements LocalQuizTeamRepositoryUI {
    
    private Logger logger = Logger.getLogger(LocalQuizTeamDb.class);
    
    /*@Resource
    UserTransaction ut;*/
    @PersistenceContext(unitName="LocalQuizTeam_unit", type=PersistenceContextType.TRANSACTION)
    private EntityManager em;
    
    @Override
    public void addPerson(Person person) {
        Person p = getPerson(person.getEmail());
        if (p != null) {
            throw new DbException("[Err. already persisted: old " + p + " | new " + person + "] ");
        }
        try {
            //ut.begin();
            em.persist(person);
            //ut.commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public Person getPerson(String email){
        return em.find(Person.class, email);
    }
    
    @Override
    public List<Person> getAllPersons() {
        try {
            Query query = em.createNamedQuery("Person.getAll");
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }
    
    @Override
    public Person getPersonByName(String name){
        try {
            Query query = em.createNamedQuery("Person.getByName");
            query.setParameter("name", name);
            return ((Person) query.getSingleResult());
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            logger.error("em=" + em + "..." + e.getLocalizedMessage());
            throw new DbException(e.getMessage(), e);
        }
    }
    
    @Override
    public void mergePerson(Person person) {
        Person p = getPerson(person.getEmail());
        if (p == null) {
            throw new DbException("[Err. to-be-update person is not yet in the databas: " + person + "] ");
        }
        try {
            //ut.begin();
            em.merge(person);
            //ut.commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public void removePerson(Person person) {
        Person p = getPerson(person.getEmail());
        if (p == null) {
            throw new DbException("[Err. to-be-removed person is not yet in the database: " + person + "] ");
        }
        for(Quiz q : getAllQuizzes()){
            q.removeParticipant(p);
            em.merge(q);
        }
        try {
            //ut.begin();
            if (!em.contains(p)) {
                p = em.merge(p);
            }
            em.remove(p);
            //ut.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }
    
    @Override
    public void addQuiz(Quiz quiz) {
        Quiz q = getQuiz(quiz.getId());
        if (q != null) {
            throw new DbException("[Err. already persisted: old " + q + " | new " + quiz + "] ");
        }
        try {
            //ut.begin();
            em.persist(quiz);
            //ut.commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public Quiz getQuiz(long id){
        return em.find(Quiz.class, id);
    }
        
    @Override
        public List<Quiz> getAllQuizzes() {
        try {
            Query query = em.createNamedQuery("Quiz.getAll");
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }
    
    @Override
    public void mergeQuiz(Quiz quiz) {
        Quiz q = getQuiz(quiz.getId());
        if (q == null) {
            throw new DbException("[Err. to-be-update person is not yet in the databas: " + quiz + "] ");
        }
        try {
            //ut.begin();
            em.merge(quiz);
            //ut.commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public void removeQuiz(Quiz quiz) {
        Quiz q = getQuiz(quiz.getId());
        if (q == null) {
            throw new DbException("[Err. to-be-removed person is not yet in the database: " + quiz + "] ");
        }
        try {
            //ut.begin();
            em.remove(quiz);
            //ut.commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            logger.error(e.getMessage());
        }
    }
}
