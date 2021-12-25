package com.iwcn.iwcn_p1.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import com.iwcn.iwcn_p1.model.Customer;
import com.iwcn.iwcn_p1.model.Event;
import com.iwcn.iwcn_p1.model.Ticket;
import com.iwcn.iwcn_p1.model.User;
import com.iwcn.iwcn_p1.repository.TicketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iwcn.iwcn_p1.error.NotFoundException;

@Service
public class TicketService {

    @Autowired
	private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    public List<Ticket> findAll() {
		return ticketRepository.findAll();
	}

    public List<Ticket> getTicketsFromCustomer(User user) {
        return ticketRepository.findByCustomer(user);
    }


    public Ticket createTicket(Customer customer, Long eventId) {
        
        //first try to reserve the seat
        eventService.reserveSeat(eventId);
        
        //once the seat is reserved, create the ticket
        Event event = eventService.findById(eventId).orElseThrow();
        Ticket ticket = Ticket.builder()
        .price(event.getPrice())
        .date(setCurrentDate())
        .event(event)
        .customer(customer)
        .build();
        
        ticketRepository.saveAndFlush(ticket);

        return ticket;
    }


    public Ticket deleteTicket(Long ticketId){

        Ticket ticket = ticketRepository
        .findById(ticketId)
        .orElseThrow(() -> new NotFoundException("ticket doesnt exist"));

        //removing a ticket from its repository automatically deletes it from other related entities
        //ticketRepository.deleteById(ticketId); //this doesnt work!! but why??
        //ticketRepository.delete(ticket); //this doesnt work either!! but why??
        ticketRepository.deleteAllByIdInBatch(Arrays.asList(ticketId));

        //update currentSeats
        eventService.removeSeatReservation(ticket.getEvent().getId());

        return ticket;
    }

    private String setCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");  
        LocalDateTime currentTime = LocalDateTime.now();  
        return dtf.format(currentTime).toString();
    }
}
