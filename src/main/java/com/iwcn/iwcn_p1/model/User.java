package com.iwcn.iwcn_p1.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor // Contructor needed to load data from DB
@AllArgsConstructor

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)//JPA chooses this strategy by default if we don't specify one explicitly.
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.INTEGER) 
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Basic.class)
    private Long id;

    @Column(unique = true)
    @JsonView(Basic.class)
    private String username;

    @Column(unique = true)
    @JsonView(Basic.class)
    private String email;

    @JsonIgnore
	private String encodedPassword;

    public interface Basic {
    }
}



