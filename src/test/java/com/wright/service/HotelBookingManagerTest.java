package com.wright.service;

import com.google.common.collect.Iterables;
import com.wright.dao.ReservationRepository;
import com.wright.model.exception.BookingAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link HotelBookingManager}
 */
public class HotelBookingManagerTest {

    private static final Integer ROOM_NUMBER = 101;
    private static final Integer ANOTHER_ROOM_NUMBER = 102;
    private static final LocalDate TODAY = LocalDate.now();
    private static final String GUEST_NAME = "A. Guest";
    private static final String ANOTHER_GUEST_NAME = "Another Guest";

    @Mock
    private ReservationRepository mockRepository;

    private HotelBookingManager underTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        underTest = new HotelBookingManager(mockRepository);
    }

    /**
     * @verifies return true if no bookings exists for room and date
     * @see HotelBookingManager#isRoomAvailable(Integer, LocalDate)
     */
    @Test
    public void isRoomAvailable_should_return_true_if_no_bookings_exists_for_room_and_date() {
        // given
        given(mockRepository.getGuestNameForBooking(ROOM_NUMBER, TODAY)).willReturn(Optional.empty());

        // when
        boolean isRoomAvailable = underTest.isRoomAvailable(ROOM_NUMBER, TODAY);

        // then
        assertTrue(isRoomAvailable);
    }


    /**
     * @verifies return false when there is an existing booking for room and date
     * @see HotelBookingManager#isRoomAvailable(Integer, LocalDate)
     */
    @Test
    public void isRoomAvailable_should_return_false_when_there_is_an_existing_booking_for_room_and_date() {
        // given
        given(mockRepository.getGuestNameForBooking(ROOM_NUMBER, TODAY)).willReturn(Optional.of(GUEST_NAME));

        // when
        boolean isRoomAvailable = underTest.isRoomAvailable(ROOM_NUMBER, TODAY);

        // then
        assertFalse(isRoomAvailable);
    }

    /**
     * @verifies add booking if no booking exists for room and date
     * @see HotelBookingManager#addBooking(String, Integer, LocalDate)
     */
    @Test
    public void addBooking_should_add_booking_if_no_booking_exists_for_room_and_date() throws Exception {
        // given
        given(mockRepository.getGuestNameForBooking(ROOM_NUMBER, TODAY)).willReturn(Optional.empty());

        // when
        underTest.addBooking(GUEST_NAME, ROOM_NUMBER, TODAY);

        // then
        verify(mockRepository).addBooking(GUEST_NAME, ROOM_NUMBER, TODAY);
    }

    /**
     * @verifies throw {@link BookingAlreadyExistsException} if booking already exists for room and date
     * @see HotelBookingManager#addBooking(String, Integer, LocalDate)
     */
    @Test(expected = BookingAlreadyExistsException.class)
    public void addBooking_should_throw_link_BookingAlreadyExistsException_if_booking_already_exists_for_room_and_date() throws Exception {
        // given
        given(mockRepository.getGuestNameForBooking(ROOM_NUMBER, TODAY)).willReturn(Optional.of(GUEST_NAME));

        // when
        underTest.addBooking(ANOTHER_GUEST_NAME, ROOM_NUMBER, TODAY);

        // then
        verify(mockRepository, never()).addBooking(any(), any(), any());
        fail("exception should have been thrown");
    }

    /**
     * @verifies return an {@link Iterable} of the available rooms
     * @see HotelBookingManager#getAvailableRooms(LocalDate)
     */
    @Test
    public void getAvailableRooms_should_return_an_link_Iterable_of_the_available_rooms() throws Exception {
        // given
        Set<Integer> rooms = new HashSet<>();
        rooms.add(ROOM_NUMBER);
        rooms.add(ANOTHER_ROOM_NUMBER);
        given(mockRepository.getRooms()).willReturn(rooms);
        given(mockRepository.getGuestNameForBooking(ROOM_NUMBER, TODAY)).willReturn(Optional.empty());
        given(mockRepository.getGuestNameForBooking(ANOTHER_ROOM_NUMBER, TODAY)).willReturn(Optional.empty());

        // when
        Iterable<Integer> availableRooms = underTest.getAvailableRooms(TODAY);

        // then
        assertEquals(2, Iterables.size(availableRooms));
        assertThat(availableRooms, hasItem(ROOM_NUMBER));
        assertThat(availableRooms, hasItem(ANOTHER_ROOM_NUMBER));
    }

    /**
     * @verifies return an empty {@link Iterable} if no rooms are available
     * @see HotelBookingManager#getAvailableRooms(LocalDate)
     */
    @Test
    public void getAvailableRooms_should_return_an_empty_link_Iterable_if_no_rooms_are_available() throws Exception {
        // given
        Set<Integer> rooms = new HashSet<>();
        rooms.add(ROOM_NUMBER);
        given(mockRepository.getRooms()).willReturn(rooms);
        given(mockRepository.getGuestNameForBooking(ROOM_NUMBER, TODAY)).willReturn(Optional.of(GUEST_NAME));

        // when
        Iterable<Integer> availableRooms = underTest.getAvailableRooms(TODAY);

        // then
        assertTrue(Iterables.isEmpty(availableRooms));
    }
}