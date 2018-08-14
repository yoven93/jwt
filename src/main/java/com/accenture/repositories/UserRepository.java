package com.accenture.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accenture.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	/**
	 * Find a User by username
	 */
	public User findByUsername(String username);
}
