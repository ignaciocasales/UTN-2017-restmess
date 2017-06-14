package com.utn.restmess.controllers;

/**
 * Created by ignacio on 6/14/17.
 * <p>
 * DuplicateEmailException.
 */
public class DuplicateEmailException extends Exception {

    public DuplicateEmailException(String s) {
        super(s);
    }
}
