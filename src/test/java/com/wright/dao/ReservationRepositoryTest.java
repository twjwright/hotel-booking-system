package com.wright.dao;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReservationRepositoryTest {

    private static final Integer ROOM_NUMBER = 101;
    private static final Integer ANOTHER_ROOM_NUMBER = 102;
    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate TOMORROW = TODAY.plus(1, DAYS);
    private static final String GUEST_NAME = "A. Guest";

    private ReservationRepository underTest;

    @Before
    public void setup() {
        underTest = new ReservationRepository();
    }

    /**
     * @verifies add booking for guest, room number, and date
     * @see ReservationRepository#addBooking(String, Integer, LocalDate)
     */
    @Test
    public void addBooking_should_add_booking_for_guest_room_number_and_date() {
        // given

        // when
        underTest.addBooking(GUEST_NAME, ROOM_NUMBER, TODAY);

        // then
        Optional<String> returnValue = underTest.getGuestNameForBooking(ROOM_NUMBER, TODAY);

        assertTrue(returnValue.isPresent());
        assertEquals(GUEST_NAME, returnValue.get());
    }

    /**
     * @verifies return empty optional if no bookings exist
     * @see ReservationRepository#getGuestNameForBooking(Integer, java.time.LocalDate)
     */
    @Test
    public void getGuestNameForBooking_should_return_empty_optional_if_no_bookings_exist() {
        // given

        // when
        Optional<String> returnValue = underTest.getGuestNameForBooking(ROOM_NUMBER, TODAY);

        // then
        assertFalse(returnValue.isPresent());
    }

    /**
     * @verifies return empty optional if booking exists for room but on another date
     * @see ReservationRepository#getGuestNameForBooking(Integer, java.time.LocalDate)
     */
    @Test
    public void getGuestNameForBooking_should_return_empty_optional_if_booking_exists_for_room_but_on_another_date() {
        // given
        underTest.addBooking(GUEST_NAME, ROOM_NUMBER, TOMORROW);

        // when
        Optional<String> returnValue = underTest.getGuestNameForBooking(ROOM_NUMBER, TODAY);

        // then
        assertFalse(returnValue.isPresent());
    }
    /**
     * @verifies return empty optional if booking exists on date but for another room
     * @see ReservationRepository#getGuestNameForBooking(Integer, LocalDate)
     */
    @Test
    public void getGuestNameForBooking_should_return_empty_optional_if_booking_exists_on_date_but_for_another_room() {
        // given
        underTest.addBooking(GUEST_NAME, ANOTHER_ROOM_NUMBER, TODAY);

        // when
        Optional<String> returnValue = underTest.getGuestNameForBooking(ROOM_NUMBER, TODAY);

        // then
        assertFalse(returnValue.isPresent());
    }

    /**
     * @verifies return optional of guests name if booking exists for room and on same date
     * @see ReservationRepository#getGuestNameForBooking(Integer, LocalDate)
     */
    @Test
    public void getGuestNameForBooking_should_return_optional_of_guests_name_if_booking_exists_for_room_and_on_same_date() {
        // given
        underTest.addBooking(GUEST_NAME, ROOM_NUMBER, TODAY);

        // when
        Optional<String> returnValue = underTest.getGuestNameForBooking(ROOM_NUMBER, TODAY);

        // then
        assertTrue(returnValue.isPresent());
        assertEquals(GUEST_NAME, returnValue.get());
    }
}