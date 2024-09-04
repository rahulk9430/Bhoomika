package com.sopra.UserManagement.Exception;

public class ErrorResponse {
	private int status;
	private String message;

	// Constructor with both status and message
	public ErrorResponse(int status, String message) {
		this.status = status;
		this.message = message;
	}

	// Constructor with only message (status defaults to 0)
	public ErrorResponse(String message) {
		this.status = 0;  // Default value or could be omitted
		this.message = message;
	}

	// Getters and Setters
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
