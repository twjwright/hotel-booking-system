package com.wright.model.exception;

/**
 * Exception thrown when room doesn't exist
 */
public class RoomDoesNotExistException  extends Exception {

    public RoomDoesNotExistException(Integer roomNumber) {
        super(String.format("Room %d does not exist in the booking system", roomNumber));
    }
}
