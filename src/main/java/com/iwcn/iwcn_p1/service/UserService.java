package com.iwcn.iwcn_p1.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.iwcn.iwcn_p1.dto.UserRegisterRequest;
import com.iwcn.iwcn_p1.error.BadRequestException;
import com.iwcn.iwcn_p1.error.UnauthorizedActionException;
import com.iwcn.iwcn_p1.model.Admin;
import com.iwcn.iwcn_p1.model.Customer;
import com.iwcn.iwcn_p1.model.Event;
import com.iwcn.iwcn_p1.model.Organizer;
import com.iwcn.iwcn_p1.model.User;
import com.iwcn.iwcn_p1.repository.EventRepository;
import com.iwcn.iwcn_p1.repository.TicketRepository;
import com.iwcn.iwcn_p1.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private EventRepository eventRepository;

	/*
	 * get complete list of all users (this can only be done by Admin users)
	 */
	public Collection<User> processGetUsers(HttpServletRequest req) {

		if (req.isUserInRole(Admin.class.toString())) {
			return userRepository.findAll();
		} else {
			throw new UnauthorizedActionException();
		}
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public User register(UserRegisterRequest registerData) {

		try {

			if(userRepository.findByUsername(registerData.getUsername()).isPresent()) throw new BadRequestException("Username already registered");

			if(userRepository.findByEmail(registerData.getEmail()).isPresent()) throw new BadRequestException("Email already registered");
			
			//System.out.println("role::::::::::::::::::::"+registerData.getRole());
			switch (registerData.getRole()) {
			case "CUSTOMER":
				//System.out.println("this is a customer::::::::::::::::::::"+registerData.getRole());
				return userRepository.saveAndFlush(
					Customer
					.builder()
					.username(registerData.getUsername())
					.email(registerData.getEmail())
					.encodedPassword(passwordEncoder.encode(registerData.getPass()))
					.build()
				);

			case "ORGANIZER":
				//System.out.println("this is a organizer::::::::::::::::::::"+registerData.getRole());
				return userRepository.saveAndFlush(
					Organizer
					.builder()
					.username(registerData.getUsername())
					.email(registerData.getEmail())
					.encodedPassword(passwordEncoder.encode(registerData.getPass()))
					.build()
				);

			default:
				throw new BadRequestException("Invalid role");
			}

		} catch (Exception e) {

			String defaultMsg = e.getMessage();
			String msg = (defaultMsg == null) ? "Invalid request" : defaultMsg;
			throw new BadRequestException(msg);
		}
	}

    public Optional<User> processGetUser(HttpServletRequest req, Long userId) {

		if (req.isUserInRole(Admin.class.toString())) {
			return userRepository.findById(userId);
		} else {
			throw new UnauthorizedActionException();
		}
    }
	
	public User deleteUserById(Long id, HttpServletRequest req) {

		User user = userRepository.findById(id).orElseThrow();

/* 		if (user instanceof Admin) {
			throw new BadRequestException("Administrators cannot be removed");
		} */
		if (user instanceof Customer) {
			
			Customer customer = (Customer) user;
			//ticketRepository.flush();
			ticketRepository.deleteAllByIdInBatch(customer.getTickets().stream().map((t) -> t.getId()).collect(Collectors.toList()));
		}
		if (user instanceof Organizer) {

			Organizer organizer = (Organizer) user;
			
			List<Long> ticketIds = new ArrayList<>();
			for (Event e : organizer.getEvents()) {
				ticketIds.addAll(e.getTickets().stream().map((t) -> t.getId()).collect(Collectors.toList()));
			}
			//ticketRepository.flush();
			ticketRepository.deleteAllByIdInBatch(ticketIds);
		}

		userRepository.delete(user);

		return user;

	}

}

/*
 * public boolean exist(long id) { return repository.existsById(id); }
 */
