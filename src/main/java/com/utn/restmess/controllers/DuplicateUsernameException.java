package com.utn.restmess.controllers;

/**
 * Created by ignacio on 6/14/17.
 * <p>
 * DuplicateUsernameException.
 */
public class DuplicateUsernameException extends Exception {

    public DuplicateUsernameException(String s) {
        super(s);
    }
}
