package com.student.manage.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

				//(Return View Pages)
//combination of Controlleradvice + ResponseBody
@RestControllerAdvice 
public class GlobalExceptionHandler {

	@ExceptionHandler(StudentNotFoundException.class )
	public ResponseEntity<?> studentNotFoundException(StudentNotFoundException e) {
		ErrorResponse error=new ErrorResponse(e.getMessage(),HttpStatus.NOT_FOUND, LocalDateTime.now());
		return new ResponseEntity<>(error ,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex){
		HashMap<String, String> errors =new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(e->errors.put(e.getField(), e.getDefaultMessage()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		
	}
}
