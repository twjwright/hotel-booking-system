package com.wright.model.exception;

public class BookingAlreadyExistsException extends Exception {

    public BookingAlreadyExistsException() {
        super("Booking already exists for this room on this date");
    }
}
