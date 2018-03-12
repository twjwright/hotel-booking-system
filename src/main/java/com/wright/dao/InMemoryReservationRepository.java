package com.wright.dao;

import com.wright.model.exception.RoomDoesNotExistException;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of {@link ReservationRepository}
 */
public class InMemoryReservationRepository implements ReservationRepository {

    private Set<Integer> rooms = new HashSet<>();
    private Map<Pair<LocalDate, Integer>, String> reservations = new ConcurrentHashMap<>();

    /**
     * Adds a booking
     * @param guestName     guest name on the booking
     * @param roomNumber    room to be booked
     * @param date          date to book the room for
     * @should add booking for guest, room number, and date
     * @should return {@link RoomDoesNotExistException} if trying to add a booking for a room that doesn't exist
     */
    public void addBooking(String guestName, Integer roomNumber, LocalDate date) throws RoomDoesNotExistException {
        if (!rooms.contains(roomNumber)) {
            throw new RoomDoesNotExistException(roomNumber);
        }
        reservations.put(Pair.of(date, roomNumber), guestName);
    }

    /**
     * Returns the guest name if there's a booking for the room and date specified
     * @param roomNumber    the room number to get the booking details for
     * @param date          the date to get the booking details for
     * @return              {@link Optional} containing guest name if booking exists, otherwise empty optional
     * @should return empty optional if no bookings exist
     * @should return empty optional if booking exists for room but on another date
     * @should return empty optional if booking exists on date but for another room
     * @should return optional of guests name if booking exists for room and on same date
     */
    public Optional<String> getGuestNameForBooking(Integer roomNumber, LocalDate date) {
        return Optional.ofNullable(reservations.get(Pair.of(date, roomNumber)));
    }

    /**
     * Adds a room to the booking system
     * @param roomNumber    the room number to add to the booking system
     */
    public void addRoom(Integer roomNumber) {
        rooms.add(roomNumber);
    }

    /**
     * Gets the rooms in the booking system
     * @return              the rooms in the booking system
     * @should return the rooms in the booking system
     */
    public Set<Integer> getRooms() {
        return rooms;
    }
}
