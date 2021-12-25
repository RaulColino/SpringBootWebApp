package com.iwcn.iwcn_p1.controller;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import com.fasterxml.jackson.annotation.JsonView;
import com.iwcn.iwcn_p1.model.Customer;
import com.iwcn.iwcn_p1.model.Event;
import com.iwcn.iwcn_p1.model.Ticket;
import com.iwcn.iwcn_p1.service.EventService;
import com.iwcn.iwcn_p1.service.TicketService;
import com.iwcn.iwcn_p1.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketRestController {

    interface TicketDetail extends Ticket.Basic, Ticket.EventInfo, Event.Basic {}

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    // findAll
    @GetMapping("/")
    public Collection<Ticket> getTickets() {
        return ticketService.findAll();
    }

    // create ticket
    @PostMapping("/") // /api/tickets/?eventId={eventId}
    @JsonView(TicketDetail.class)
    public ResponseEntity<Ticket> generateTicket(HttpServletRequest request, Long eventId) {

        Principal principal = request.getUserPrincipal(); 
        Customer customer = (Customer) userService.findByUsername(principal.getName()).orElseThrow();
        Ticket ticket =  ticketService.createTicket(customer, eventId);
        
        URI location = fromCurrentRequest().path("/").build().toUri();
        return ResponseEntity.created(location).body(ticket);
    }

    // create ticket
    @DeleteMapping("/{id}") // /api/tickets/{id}
    @JsonView(TicketDetail.class)
    public ResponseEntity<Ticket> deleteTicket(@PathVariable Long id) {

        Ticket ticket =  ticketService.deleteTicket(id);
        return ResponseEntity.ok(ticket);
    }
}
