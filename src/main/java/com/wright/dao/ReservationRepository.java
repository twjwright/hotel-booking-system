package com.wright.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ReservationRepository {

    private Map<Integer, Map<LocalDate, String>> reservations = new ConcurrentHashMap<>();

    /**
     *
     * @param guestName
     * @param roomNumber
     * @param date
     * @should add booking for guest, room number, and date
     */
    public void addBooking(String guestName, Integer roomNumber, LocalDate date) {
        Map<LocalDate, String> booking = new HashMap<>();
        booking.put(date, guestName);
        reservations.put(roomNumber, booking);
    }

    /**
     *
     * @param roomNumber
     * @param date
     * @return
     * @should return empty optional if no bookings exist
     * @should return empty optional if booking exists for room but on another date
     * @should return empty optional if booking exists on date but for another room
     * @should return optional of guests name if booking exists for room and on same date
     */
    public Optional<String> getGuestNameForBooking(Integer roomNumber, LocalDate date) {
        if (!reservations.containsKey(roomNumber)) {
            return Optional.empty();
        }
        if (!reservations.get(roomNumber).containsKey(date)) {
            return Optional.empty();
        }
        return Optional.of(reservations.get(roomNumber).get(date));
    }
}
