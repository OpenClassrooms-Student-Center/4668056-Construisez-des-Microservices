package com.ecommerce.micrommerce.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ProduitGratuitException extends RuntimeException {
    public ProduitGratuitException(String s) {
        super(s);
    }
}
