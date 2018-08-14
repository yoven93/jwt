package com.accenture.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.accenture.entities.User;
import com.accenture.exceptions.UserAlreadyExistsException;
import com.accenture.repositories.UserRepository;

@Service(value = "userService")
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bcryptEncoder;
	
	public UserService(UserRepository userRepository, BCryptPasswordEncoder bcryptEncoder) {
		super();
		this.userRepository = userRepository;
		this.bcryptEncoder = bcryptEncoder;
	}
	
	
	/**
	 * Get all users
	 */
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}
	
	
	/**
	 * Get user by username
	 */
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	
	/**
	 * Create user
	 * @throws UserAlreadyExistsException 
	 */
	public User createUser(User user) throws UserAlreadyExistsException {
		
		// Check if username is unique
		if (findByUsername(user.getUsername()) == null) {
			user.setPassword(bcryptEncoder.encode(user.getPassword()));
			return userRepository.save(user);
		}
		
		throw new UserAlreadyExistsException("This username already exists");
	}
	
	
	
	
	
	/**************************************************************************
	 						For authentication Purposes
	 **************************************************************************/
	
	/**
	 * Get the role of a User and convert it into a SimpleGrantedAuthority
	 */
	private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
		return authorities;
	}
	
	
	/**
	 * Get User by username and convert it into a UserDetails object
	 */
	public UserDetails loadUserByUsername(String username) {
		
		User user = userRepository.findByUsername(username);
		
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
	}
	
	
	
	
	/**************************************************************************
							Initialize Database with Users
	 **************************************************************************/
	@PostConstruct
	public void initializeUserTable() {
		 User user1 = new User("yoven", "1234", "ROLE_ADMIN");
		 User user2 = new User("hema", "4321", "ROLE_USER");
		 
		 try {
			createUser(user1);
			createUser(user2);
		} catch (UserAlreadyExistsException e) {
			e.printStackTrace();
		}
	}
}
