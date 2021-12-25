 package com.iwcn.iwcn_p1.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //bundles the features of @ToString, @EqualsAndHashCode, @Getter / @Setter and @RequiredArgsConstructor together:
@NoArgsConstructor // Contructor needed to load data from DB
@AllArgsConstructor
@Builder //lets you do something like: Person.builder().name("Adam Savage").city("San Francisco").build();
@Entity // To generate DB table and save it
public class Event {

    public interface Basic {}
    public interface Tickets {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Basic.class)
    private Long id;

    @JsonView(Basic.class)
    private String name;

    @JsonView(Basic.class)
    private String description;

    @JsonView(Basic.class)
    private String datetime;

    @JsonView(Basic.class)
    private Double price;

    @JsonView(Basic.class)
    private Integer maxSeats;
    
    @Builder.Default // this let us set a default value when using Lombok's Builder pattern to instantiate objects
    @JsonView(Basic.class)
    private Integer reservedSeats = 0;

    @JsonView(Basic.class)
    private String imageURL;

    @ManyToOne
    @JsonIgnore
    private User organizer;

    //There is no relation table because the main entity is Ticket
    //CascadeType.ALL -> cascade=ALL is usually configured so that objects do not have to be saved independently
    //orphanRemoval = true -> a ticket exists only if its event exists
    @OneToMany(mappedBy = "event",fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Tickets.class)
    private List<Ticket> tickets;


    public Ticket addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setEvent(this);
        return ticket;
    }
 
    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setEvent(null);
    }

    // to set default value in the constructors define your own getter method overriding the lombok getter 
    public Integer getReservedSeats() {
        return Objects.isNull(reservedSeats) ? 0 : reservedSeats;
    }

/*  NEVER DO THIS
    // check seat availability
    public boolean seatsAvailable() {
        return this.reservedSeats < this.maxSeats;
    } */

}
