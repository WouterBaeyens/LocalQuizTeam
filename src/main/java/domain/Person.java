/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Wouter
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Person.getAll", query="select p from Person p"),
    @NamedQuery(name="Person.getByName", query="select p from Person p where p.name = :name")

})
public class Person implements Serializable {
   
    @FormParam("email")
    @Id @NotNull
    @NotEmpty(message="please fill in your email.")
    private String email;
    
    //jpa
    @FormParam("name")
    @Column(unique=true)
    //bean
    //@UniqueConstraint
    @Size(min=2, message="your username must be at least 2 characters long.")
    private String name;

    public Person(){}
    
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
    if (other == this) return true;
    if (!(other instanceof Person))return false;
    Person otherMyClass = (Person)other;
    return (this.getEmail().equals(otherMyClass.getEmail()) && this.getName().equals(otherMyClass.getName()));
    }
    
      @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
