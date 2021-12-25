package com.iwcn.iwcn_p1.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //bundles the features of @ToString, @EqualsAndHashCode, @Getter / @Setter and @RequiredArgsConstructor together
@NoArgsConstructor // Contructor needed to load data from DB
@AllArgsConstructor
@Builder //lets you do something like: Person.builder().name("Adam Savage").city("San Francisco").build();
@Entity
public class Ticket {

    public interface Basic {}
    public interface EventInfo {}
    public interface CustomerInfo {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Basic.class)
    private Long id;

    @JsonView(Basic.class)
    private Double price;

    @JsonView(Basic.class)
    private String date;

    @ManyToOne
    @JsonView(EventInfo.class)
    private Event event; //The main entity in this relation is Event. Bidirectional relation

    @ManyToOne
    @JsonView(CustomerInfo.class)
    private User customer; //The main entity in this relation is Customer. Unidirectional relation
}
