package com.accenture.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.entities.User;
import com.accenture.entities.dtos.UserLoginDTO;
import com.accenture.entities.dtos.UserRegisterDTO;
import com.accenture.exceptions.UserAlreadyExistsException;
import com.accenture.security.TokenProvider;
import com.accenture.services.UserService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
	
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private UserService userService;
    
    
    /**
     * Login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		userLoginDTO.getUsername(),
                		userLoginDTO.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(token);
    }
    
    
    /**
     * Register
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO userRegisterDTO) {
    	
    	User user = new User();
    	user.setUsername(userRegisterDTO.getUsername());
    	user.setPassword(userRegisterDTO.getPassword());
    	user.setRole(userRegisterDTO.getRole());
    	
    	try {
			
    		if (userService.createUser(user) != null) {
    			
    			final Authentication authentication = authenticationManager.authenticate(
    	                new UsernamePasswordAuthenticationToken(
    	                		userRegisterDTO.getUsername(),
    	                		userRegisterDTO.getPassword()
    	                )
    	        );
    	        
    	        SecurityContextHolder.getContext().setAuthentication(authentication);
    	        final String token = jwtTokenUtil.generateToken(authentication);
    	        return ResponseEntity.ok(token);
    	        
    		} else {
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occured");
    		}
			
		} catch (UserAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("This user already exists");
		}
    }
}
