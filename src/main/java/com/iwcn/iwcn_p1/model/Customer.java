package com.iwcn.iwcn_p1.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // Contructor needed to load data from DB
@AllArgsConstructor

@Entity
@DiscriminatorValue("2")
public class Customer extends User {

    public interface Tickets {}

    @OneToMany(mappedBy = "customer",fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Tickets.class)
    private List<Ticket> tickets;

    @Builder
    public Customer(Long id, String username, String email, String encodedPassword, List<Ticket> tickets) {
        super(id, username, email, encodedPassword);
        this.tickets = tickets;
    }

    
	public Ticket addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setCustomer(this);
        return ticket;
    }
 
    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setCustomer(null);
    }
}
