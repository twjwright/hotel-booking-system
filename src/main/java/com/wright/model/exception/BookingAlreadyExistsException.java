package com.wright.model.exception;

/**
 * Exception thrown when booking already exists
 */
public class BookingAlreadyExistsException extends Exception {

    public BookingAlreadyExistsException() {
        super("Booking already exists for this room on this date");
    }
}
