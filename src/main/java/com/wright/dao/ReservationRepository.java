package com.wright.dao;

import com.wright.model.exception.RoomDoesNotExistException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface ReservationRepository {

    /**
     * Adds a booking
     * @param guestName     guest name on the booking
     * @param roomNumber    room to be booked
     * @param date          date to book the room for
     */
    void addBooking(String guestName, Integer roomNumber, LocalDate date) throws RoomDoesNotExistException;

    /**
     * Returns the guest name if there's a booking for the room and date specified
     * @param roomNumber    the room number to get the booking details for
     * @param date          the date to get the booking details for
     * @return              {@link Optional} containing guest name if booking exists, otherwise empty optional
     */
    Optional<String> getGuestNameForBooking(Integer roomNumber, LocalDate date);

    /**
     * Adds a room to the booking system
     * @param roomNumber    the room number to add to the booking system
     */
    void addRoom(Integer roomNumber);

    /**
     * Gets the rooms in the booking system
     * @return              the rooms in the booking system
     */
    Set<Integer> getRooms();



}
