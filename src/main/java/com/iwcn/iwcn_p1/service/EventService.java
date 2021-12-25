package com.iwcn.iwcn_p1.service;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.iwcn.iwcn_p1.dto.EventUpdateRequest;
import com.iwcn.iwcn_p1.dto.EventUploadRequest;
import com.iwcn.iwcn_p1.error.BadRequestException;
import com.iwcn.iwcn_p1.error.NotFoundException;
import com.iwcn.iwcn_p1.model.Event;
import com.iwcn.iwcn_p1.model.Organizer;
import com.iwcn.iwcn_p1.model.Ticket;
import com.iwcn.iwcn_p1.model.User;
import com.iwcn.iwcn_p1.repository.EventRepository;
import com.iwcn.iwcn_p1.repository.TicketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private static final String EVENT_IMAGES_FOLDER = "eventimages";
    
    @Autowired
	private EventRepository eventRepository;

    @Autowired
	private TicketRepository ticketRepository;

    @Autowired
	private LocalImageService imageStorageService;

    public List<Event> findAll() {
        eventRepository.flush();
		return eventRepository.findAll();
	}

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public ResponseEntity<Event> update(Long id, EventUpdateRequest updatedEventData) {
        
        Event currentEvent = eventRepository.findById(id).orElseThrow();
        Event updatedEvent = Event.builder()
        .id(id)
        .name(updatedEventData.getName())
        .description(updatedEventData.getDescription())
        .datetime(updatedEventData.getDatetime())
        .price(updatedEventData.getPrice())
        .maxSeats(updatedEventData.getMaxSeats())
        .reservedSeats(currentEvent.getReservedSeats())
        .imageURL(currentEvent.getImageURL())
        .organizer(currentEvent.getOrganizer())
        .tickets(currentEvent.getTickets())
        .build();
        
        eventRepository.saveAndFlush(updatedEvent);
        return ResponseEntity.ok(updatedEvent); 
    }

    public ResponseEntity<Event> createEvent(Organizer organizer, @Valid EventUploadRequest eventData) throws IOException{
        
        // create event object from Multipart fields
        Event event = Event.builder()
        .name(eventData.getName())
        .description(eventData.getDescription())
        .datetime(eventData.getDatetime())
        .price(eventData.getPrice())
        .maxSeats(eventData.getMaxSeats())
        .reservedSeats(eventData.getReservedSeats())
        .organizer(organizer)
        .tickets(new ArrayList<Ticket>())
        .build();

        // save the event to get an id
        eventRepository.save(event); //you can also do: eventRepository.saveAndFlush(event);

        // create image url, set it to the event and update the saved event
        URI imageURL = fromCurrentRequest().path("/{id}/image").buildAndExpand(event.getId()).toUri();// "/{id}/image"
        event.setImageURL(imageURL.toString());
        eventRepository.save(event); //you can also do: eventRepository.saveAndFlush(

        // save image into local storage
        imageStorageService.saveImage(EVENT_IMAGES_FOLDER, event.getId(), eventData.getImage());

        return ResponseEntity.created(imageURL).body(event);
    }

    public List<Event> getEventsFromOrganizer(User user) {
        return eventRepository.findByOrganizer(user);
    }

    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(()-> new NotFoundException("Event with id "+eventId+" doesn't exist"));
    }

    @Transactional
    public void reserveSeat(Long eventId){
        //System.out.println("event::::::::::::"+eventRepository.getById(eventId));
        System.out.println("event::::::::::::"+eventId);
        try{
            Event e = eventRepository.findById(eventId).orElseThrow(()-> new NotFoundException("Event with id "+eventId+" doesn't exist"));
            System.out.println("::::::::::::"+e.getMaxSeats());
            System.out.println("::::::::::::"+e.getReservedSeats());
            if (e.getReservedSeats() >= e.getMaxSeats()) throw new BadRequestException("No seats available");
            System.out.println("2");
            eventRepository.reserveSeat(eventId);
        } catch (Exception ex) {
            System.out.println(ex);
            String defaultMsg = ex.getMessage();
			String msg = (defaultMsg == null) ? "There was an error reserving the seat" : defaultMsg;
			throw new BadRequestException(msg);
        }
    }

    public void removeSeatReservation(Long eventId) {
        eventRepository.removeSeatReservation(eventId);
    }

    public Event deleteEventById(Long id) {

        Event event = eventRepository.findById(id).orElseThrow();

        List<Long> ticketIds = event.getTickets().stream().map((t) -> t.getId()).collect(Collectors.toList());
        //ticketRepository.flush();
        ticketRepository.deleteAllByIdInBatch(ticketIds);
        
        //eventRepository.flush();
        //eventRepository.delete(event); //this doesnt  work!! why??

        eventRepository.deleteAllByIdInBatch(Arrays.asList(id));
        
        //eventRepository.deleteById(id);

        return event;
    }

    public Boolean checkEventCreatedByOrganizer(Event event, Organizer organizer) {

        return eventRepository.findByOrganizer(organizer).contains(event);
    }

/* 	public boolean exist(long id) {
		return repository.existsById(id);
	} */
}
