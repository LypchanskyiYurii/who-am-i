package com.eleks.academy.whoami.configuration;

import com.eleks.academy.whoami.core.exception.ErrorResponse;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.model.response.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GameControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(GameException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handleGameException(GameException gameException) {
		return gameException::getMessage;
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers, HttpStatus status,
																  WebRequest request) {
		List<String> details = new ArrayList<>();
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}
		return new ResponseEntity<>(new ErrorResponse("Validation failed!", details), HttpStatus.BAD_REQUEST);
	}

}
