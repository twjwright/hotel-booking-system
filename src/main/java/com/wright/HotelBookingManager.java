package com.wright;

import com.wright.dao.ReservationRepository;
import com.wright.model.exception.BookingAlreadyExistsException;

import java.time.LocalDate;

public class HotelBookingManager implements BookingManager {

    private ReservationRepository repository;

    public HotelBookingManager(ReservationRepository repository) {
        this.repository = repository;
    }

    /**
     * Checks whether a room is booked on the specified date
     * @param room      room number to check
     * @param date      date to check
     * @return          true if room is available, otherwise false
     * @should return true if no bookings exists for room and date
     * @should return false when there is an existing booking for room and date
     */
    @Override
    public boolean isRoomAvailable(Integer room, LocalDate date) {
        return !repository.getGuestNameForBooking(room, date).isPresent();
    }

    /**
     * Creates a new booking for guest in room for date specified
     * @param guest     guest name to make the booking for
     * @param room      the room to book
     * @param date      the date to book the room on
     * @throws          BookingAlreadyExistsException is booking already exists
     * @should add booking if no booking exists for room and date
     * @should throw {@link BookingAlreadyExistsException} if booking already exists for room and date
     */
    @Override
    public void addBooking(String guest, Integer room, LocalDate date) throws BookingAlreadyExistsException {
        if (repository.getGuestNameForBooking(room, date).isPresent()) {
            throw new BookingAlreadyExistsException();
        }
        repository.addBooking(guest, room, date);
    }
}
