package com.leapwise.banking.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BankingException extends RuntimeException {

    private final String title;

    private final String message;

    private final HttpStatus statusCode;
}
