package com.iwcn.iwcn_p1.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonView;
import com.iwcn.iwcn_p1.dto.UserRegisterRequest;
import com.iwcn.iwcn_p1.error.BadRequestException;
import com.iwcn.iwcn_p1.model.Customer;
import com.iwcn.iwcn_p1.model.Organizer;
import com.iwcn.iwcn_p1.model.Event;
import com.iwcn.iwcn_p1.model.Ticket;
import com.iwcn.iwcn_p1.model.User;
import com.iwcn.iwcn_p1.service.UserService;
import com.nimbusds.oauth2.sdk.Response;

import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    interface UserDetail extends User.Basic, Customer.Tickets, Organizer.Events, Ticket.Basic, Ticket.EventInfo, Event.Basic{}

    @Autowired
    private UserService userService;

    // get all users. Only administrators
    @GetMapping("/")
    @JsonView(UserDetail.class)
    public Collection<User> getUsers(HttpServletRequest req) {  
        return userService.processGetUsers(req);
    }

    // find user by id. Only administrators
    @GetMapping("/{id}")
    @JsonView(UserDetail.class)
    public ResponseEntity<User> getUser(@PathVariable Long id, HttpServletRequest req) {  

        User user = userService.processGetUser(req, id).orElseThrow();
        return ResponseEntity.ok(user); 
    }

    // get logged user data
    @GetMapping("/me")
    @JsonView(UserDetail.class)
    public ResponseEntity<User> me(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(user);   
    }

    // save (register new user)
    @PostMapping("/")
    @JsonView(User.Basic.class)
    public ResponseEntity<User> createUser(@RequestBody UserRegisterRequest registerData) {

        User user = userService.register(registerData);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(user);
    }

    // delete
    @DeleteMapping("/{id}")
    @JsonView(User.Basic.class)
    public User deleteUser(@PathVariable Long id, HttpServletRequest req) {

        User user = userService.deleteUserById(id, req);

        return user;
    }
}
