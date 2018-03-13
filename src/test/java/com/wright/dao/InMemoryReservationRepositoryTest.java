package com.wright.dao;

import com.wright.model.exception.RoomDoesNotExistException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

/**
 * Tests for {@link InMemoryReservationRepository}
 */
public class InMemoryReservationRepositoryTest {

    private static final Integer ROOM_NUMBER = 101;
    private static final Integer ANOTHER_ROOM_NUMBER = 102;
    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate TOMORROW = TODAY.plus(1, DAYS);
    private static final String GUEST_NAME = "A. Guest";

    private InMemoryReservationRepository underTest;

    @Before
    public void setup() {
        underTest = new InMemoryReservationRepository();
    }

    /**
     * @verifies add booking for guest, room number, and date
     * @see InMemoryReservationRepository#addBooking(String, Integer, LocalDate)
     */
    @Test
    public void addBooking_should_add_booking_for_guest_room_number_and_date() throws Exception {
        // given
        underTest.addRoom(ROOM_NUMBER);

        // when
        underTest.addBooking(GUEST_NAME, ROOM_NUMBER, TODAY);

        // then
        Optional<String> guestNameForBooking = underTest.getGuestNameForBooking(ROOM_NUMBER, TODAY);

        assertTrue(guestNameForBooking.isPresent());
        assertEquals(GUEST_NAME, guestNameForBooking.get());
    }

    /**
     * @verifies return {@link RoomDoesNotExistException} if trying to add a booking for a room that doesn't exist
     * @see InMemoryReservationRepository#addBooking(String, Integer, LocalDate)
     */
    @Test(expected = RoomDoesNotExistException.class)
    public void addBooking_should_return_link_RoomDoesNotExistException_if_trying_to_add_a_booking_for_a_room_that_doesnt_exist() throws Exception {
        // given
        underTest.addRoom(ROOM_NUMBER);

        // when
        underTest.addBooking(GUEST_NAME, ANOTHER_ROOM_NUMBER, TODAY);

        // then
        fail("RoomDoesNotExistException should have been thrown");
    }

    /**
     * @verifies return empty optional if no bookings exist
     * @see InMemoryReservationRepository#getGuestNameForBooking(Integer, java.time.LocalDate)
     */
    @Test
    public void getGuestNameForBooking_should_return_empty_optional_if_no_bookings_exist() {
        // given

        // when
        Optional<String> guestNameForBooking = underTest.getGuestNameForBooking(ROOM_NUMBER, TODAY);

        // then
        assertFalse(guestNameForBooking.isPresent());
    }

    /**
     * @verifies return empty optional if booking exists for room but on another date
     * @see InMemoryReservationRepository#getGuestNameForBooking(Integer, java.time.LocalDate)
     */
    @Test
    public void getGuestNameForBooking_should_return_empty_optional_if_booking_exists_for_room_but_on_another_date() throws Exception {
        // given
        underTest.addRoom(ROOM_NUMBER);
        underTest.addBooking(GUEST_NAME, ROOM_NUMBER, TOMORROW);

        // when
        Optional<String> guestNameForBooking = underTest.getGuestNameForBooking(ROOM_NUMBER, TODAY);

        // then
        assertFalse(guestNameForBooking.isPresent());
    }
    /**
     * @verifies return empty optional if booking exists on date but for another room
     * @see InMemoryReservationRepository#getGuestNameForBooking(Integer, LocalDate)
     */
    @Test
    public void getGuestNameForBooking_should_return_empty_optional_if_booking_exists_on_date_but_for_another_room() throws Exception {
        // given
        underTest.addRoom(ANOTHER_ROOM_NUMBER);
        underTest.addBooking(GUEST_NAME, ANOTHER_ROOM_NUMBER, TODAY);

        // when
        Optional<String> guestNameForBooking = underTest.getGuestNameForBooking(ROOM_NUMBER, TODAY);

        // then
        assertFalse(guestNameForBooking.isPresent());
    }

    /**
     * @verifies return optional of guests name if booking exists for room and on same date
     * @see InMemoryReservationRepository#getGuestNameForBooking(Integer, LocalDate)
     */
    @Test
    public void getGuestNameForBooking_should_return_optional_of_guests_name_if_booking_exists_for_room_and_on_same_date() throws Exception {
        // given
        underTest.addRoom(ROOM_NUMBER);
        underTest.addBooking(GUEST_NAME, ROOM_NUMBER, TODAY);

        // when
        Optional<String> guestNameForBooking = underTest.getGuestNameForBooking(ROOM_NUMBER, TODAY);

        // then
        assertTrue(guestNameForBooking.isPresent());
        assertEquals(GUEST_NAME, guestNameForBooking.get());
    }

    /**
     * @verifies return the rooms in the booking system
     * @see InMemoryReservationRepository#getRooms()
     */
    @Test
    public void getRooms_should_return_the_rooms_in_the_booking_system() throws Exception {
        // given
        underTest.addRoom(ROOM_NUMBER);
        underTest.addRoom(ANOTHER_ROOM_NUMBER);

        // when
        Set<Integer> rooms = underTest.getRooms();

        // then
        assertThat(rooms, hasItem(ROOM_NUMBER));
        assertThat(rooms, hasItem(ANOTHER_ROOM_NUMBER));
    }
}