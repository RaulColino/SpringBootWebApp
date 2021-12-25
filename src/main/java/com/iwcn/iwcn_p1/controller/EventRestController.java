package com.iwcn.iwcn_p1.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwcn.iwcn_p1.dto.EventUpdateRequest;
import com.iwcn.iwcn_p1.dto.EventUploadRequest;
import com.iwcn.iwcn_p1.error.UnauthorizedActionException;
import com.iwcn.iwcn_p1.model.Admin;
import com.iwcn.iwcn_p1.model.Event;
import com.iwcn.iwcn_p1.model.Organizer;
import com.iwcn.iwcn_p1.model.User;
import com.iwcn.iwcn_p1.service.EventService;
import com.iwcn.iwcn_p1.service.ImageService;
import com.iwcn.iwcn_p1.service.LocalImageService;
import com.iwcn.iwcn_p1.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/events")
public class EventRestController {

    private static final String EVENT_IMAGES_FOLDER = "eventimages";

    @Autowired
    private EventService eventService;

    @Autowired
	public ImageService imgStorageService;

    @Autowired
    private UserService userService;

    
    // findAll
    @GetMapping("/")
    @JsonView(Event.Basic.class)
    public Collection<Event> getEvents() {
        return eventService.findAll();
    }

    // findById
    @GetMapping("/{id}")
    @JsonView(Event.Basic.class)
    public Event getEvent(@PathVariable Long id) {
        return eventService.findById(id).orElseThrow();
    }

    // see image
    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws MalformedURLException {
        return imgStorageService.createResponseFromImage(EVENT_IMAGES_FOLDER, id);
    }

    // create
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Event> saveEvent(HttpServletRequest request, @ModelAttribute @Valid EventUploadRequest eventData) throws IOException { 
        
        Principal principal = request.getUserPrincipal(); 
        Organizer organizer = (Organizer)userService.findByUsername(principal.getName()).orElseThrow();
        
        return eventService.createEvent(organizer, eventData);
    }

    // put
    @PutMapping("/{id}")
    @JsonView(Event.Basic.class)
    public ResponseEntity<Event> replaceEvent(@PathVariable Long id, @Valid @RequestBody EventUpdateRequest updatedEvent) {
       return eventService.update(id, updatedEvent);
    }

    // delete
    @DeleteMapping("/{id}")
    @JsonView(Event.Basic.class)
    public Event deleteEvent(@PathVariable Long id, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        Event event = eventService.getEvent(id);

        // only admin users and the organizer that uploaded the event can remove the event
        if (!request.isUserInRole(Admin.class.toString())) {

            Organizer organizer = (Organizer) user; 
            Boolean created = eventService.checkEventCreatedByOrganizer(event, organizer); // throw new UnauthorizedActionException(); if not organizer of the event
			if (!created) throw new UnauthorizedActionException();
		}

        Event responseEvent = eventService.deleteEventById(id);

        return responseEvent;
    }
}
