package com.iwcn.iwcn_p1.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import com.iwcn.iwcn_p1.model.Admin;
import com.iwcn.iwcn_p1.model.Customer;
import com.iwcn.iwcn_p1.model.Event;
import com.iwcn.iwcn_p1.model.Organizer;
import com.iwcn.iwcn_p1.model.Ticket;
import com.iwcn.iwcn_p1.repository.EventRepository;
import com.iwcn.iwcn_p1.repository.TicketRepository;
import com.iwcn.iwcn_p1.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInitalizer {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EventRepository eventRepository;
  
  @Autowired
  private TicketRepository ticketRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;


  @PostConstruct
  public void init() {

    // Default users
    Admin juan = Admin
    .builder()
    .username("juan")
    .email("juan@mail.com")
    .encodedPassword(passwordEncoder.encode("pass"))
    .build();
    Organizer pepe = Organizer
    .builder()
    .username("pepe")
    .email("pepe@mail.com")
    .encodedPassword(passwordEncoder.encode("pass"))
    .events(new ArrayList<Event>())
    .build();
    Customer maria = Customer
    .builder()
    .username("maria")
    .email("maria@mail.com")
    .encodedPassword(passwordEncoder.encode("pass"))
    .tickets(new ArrayList<Ticket>())
    .build();
    userRepository.save(juan);
    userRepository.save(pepe);
    userRepository.save(maria);

    // Default events
    Event show = Event
    .builder()
    .name("music show")
    .description("A big concert")
    .datetime("20/02/2022-15:40")
    .price(20.)
    .maxSeats(3)
    .reservedSeats(2)
    .organizer(pepe)
    .build();
    Event congress = Event
    .builder()
    .name("mobile congress")
    .description("Latest tech in your pocket")
    .datetime("20/08/2022-15:40")
    .price(10.)
    .maxSeats(1)
    .reservedSeats(1)
    .organizer(pepe)
    .build();
    Long id1 = eventRepository.save(show).getId();
    Long id2 = eventRepository.save(congress).getId();
    Event e1 = eventRepository.findById(id1).get();
    Event e2 = eventRepository.findById(id2).get();
    e1.setImageURL("https://127.0.0.1:8443/api/v1/events/"+e1.getId().intValue()+"/image");
    e2.setImageURL("https://127.0.0.1:8443/api/v1/events/"+e2.getId().intValue()+"/image");
    eventRepository.saveAndFlush(e1);
    eventRepository.saveAndFlush(e2);

    // Default tickets
    ticketRepository.save(
      Ticket
      .builder()
      .price(20.0)
      .date("11/01/2022")
      .event(show)
      .customer(maria)
      .build()
    );

    //--- For Concurrency Tests ---

    IntStream.range(99, 100)//0-99
    .mapToObj(i -> Customer.builder()
      .username("user"+i) //String.format("The value of the float " +"integer variable is %d",floatVar)
      .email("user"+i+"@mail.com")
      .encodedPassword(passwordEncoder.encode("pass"))
      .tickets(new ArrayList<Ticket>())
      .build())
    .forEach(user-> userRepository.save(user));

    Event event = Event
    .builder()
    .name("concurrency test event")
    .description("To test concurrent requests ")
    .datetime("20/08/2022-15:40")
    .price(10.)
    .maxSeats(50)
    .reservedSeats(0)
    .imageURL("imageURL")
    .organizer(pepe)
    .build();
    eventRepository.save(event);


  }
}
