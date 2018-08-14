package com.accenture.exceptions;

public class UserAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 5638975718163342683L;

	public UserAlreadyExistsException(String msg) {
		super(msg);
	}
}
