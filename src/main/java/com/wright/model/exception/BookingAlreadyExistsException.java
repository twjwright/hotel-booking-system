package com.wright.model.exception;

public class BookingAlreadyExistsException extends Exception {

    public BookingAlreadyExistsException() {
        super("Bookings already exists for this room on this date");
    }
}
