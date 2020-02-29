package com.ushan.dev.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.NOT_FOUND)
public class CarNotFoundException extends RuntimeException {

	public CarNotFoundException() {}
}
