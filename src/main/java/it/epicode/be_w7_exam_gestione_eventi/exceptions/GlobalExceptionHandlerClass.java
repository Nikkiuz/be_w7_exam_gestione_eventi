package it.epicode.be_w7_exam_gestione_eventi.exceptions;


import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandlerClass {
	@ExceptionHandler(value = EntityNotFoundException.class)
	protected ResponseEntity<Error>entityNotFound(EntityNotFoundException e){
		Error error = new Error();
		error.setMessage("Entity not found");
		error.setDetails(e.getMessage());
		error.setStatus("404");
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = EntityExistsException.class)
	protected ResponseEntity<Error>alreadyExists(EntityExistsException e){
		Error error = new Error();
		error.setMessage("Entity already exists");
		error.setDetails(e.getMessage());
		error.setStatus("409");
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);

	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
		Map<String, String> errors = new HashMap<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			String fieldName = violation.getPropertyPath().toString();
			if (fieldName.contains(".")) {
				fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
			}
			errors.put(fieldName, violation.getMessage());

		}
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}
