package com.iwcn.iwcn_p1.security;

import java.util.ArrayList;
import java.util.List;

import com.iwcn.iwcn_p1.error.BadRequestException;
import com.iwcn.iwcn_p1.model.User;
import com.iwcn.iwcn_p1.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RepositoryUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user;
		try {
			user = (User) userRepository
				.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		

		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ROLE_" + user.getClass().toString()));

		return new org.springframework.security.core.userdetails.User(
			user.getUsername(), 
			user.getEncodedPassword(), 
			roles
		);
	} catch (Throwable e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new BadRequestException("df");
	}
	}
}
