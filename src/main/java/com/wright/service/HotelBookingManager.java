package com.wright.service;

import com.wright.dao.InMemoryReservationRepository;
import com.wright.model.exception.BookingAlreadyExistsException;
import com.wright.model.exception.RoomDoesNotExistException;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class HotelBookingManager implements BookingManager {

    private InMemoryReservationRepository repository;

    public HotelBookingManager(InMemoryReservationRepository repository) {
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
    public void addBooking(String guest, Integer room, LocalDate date) throws BookingAlreadyExistsException, RoomDoesNotExistException {
        if (repository.getGuestNameForBooking(room, date).isPresent()) {
            throw new BookingAlreadyExistsException();
        }
        repository.addBooking(guest, room, date);
    }

    /**
     * Returns an {@link Iterable} of the rooms available for the date specified
     * @param date      the date to check for available rooms on
     * @return          an {@link Iterable} of the available rooms
     * @should return an {@link Iterable} of the available rooms
     * @should return an empty {@link Iterable} if no rooms are available
     */
    @Override
    public Iterable<Integer> getAvailableRooms(LocalDate date) {
        return repository.getRooms().stream().
                filter(roomNumber -> !repository.getGuestNameForBooking(roomNumber, date).isPresent()).
                collect(Collectors.toList());
    }
}