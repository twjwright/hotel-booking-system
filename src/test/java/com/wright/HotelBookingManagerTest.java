package com.wright;

import com.wright.dao.ReservationRepository;
import com.wright.model.exception.BookingAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class HotelBookingManagerTest {

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
        Integer roomNumber = 101;
        LocalDate date = LocalDate.now();

        given(mockRepository.getGuestNameForBooking(roomNumber, date)).willReturn(Optional.empty());

        // when
        boolean returnValue = underTest.isRoomAvailable(roomNumber, date);

        // then
        assertTrue(returnValue);
    }


    /**
     * @verifies return false when there is an existing booking for room and date
     * @see HotelBookingManager#isRoomAvailable(Integer, LocalDate)
     */
    @Test
    public void isRoomAvailable_should_return_false_when_there_is_an_existing_booking_for_room_and_date() {
        // given
        Integer roomNumber = 101;
        LocalDate date = LocalDate.now();

        given(mockRepository.getGuestNameForBooking(roomNumber, date)).willReturn(Optional.of("A. Guest"));

        // when
        boolean returnValue = underTest.isRoomAvailable(roomNumber, date);

        // then
        assertFalse(returnValue);
    }

    /**
     * @verifies add booking if no booking exists for room and date
     * @see HotelBookingManager#addBooking(String, Integer, LocalDate)
     */
    @Test
    public void addBooking_should_add_booking_if_no_booking_exists_for_room_and_date() throws Exception {
        // given
        String guestName = "A. Guest";
        Integer roomNumber = 101;
        LocalDate date = LocalDate.now();

        given(mockRepository.getGuestNameForBooking(roomNumber, date)).willReturn(Optional.empty());

        // when
        underTest.addBooking(guestName, roomNumber, date);

        // then
        verify(mockRepository).addBooking(guestName, roomNumber, date);
    }

    /**
     * @verifies throw {@link BookingAlreadyExistsException} if booking already exists for room and date
     * @see HotelBookingManager#addBooking(String, Integer, LocalDate)
     */
    @Test(expected = BookingAlreadyExistsException.class)
    public void addBooking_should_throw_link_BookingAlreadyExistsException_if_booking_already_exists_for_room_and_date() throws Exception {
        // given
        String guestName = "A. Guest";
        Integer roomNumber = 101;
        LocalDate date = LocalDate.now();

        given(mockRepository.getGuestNameForBooking(roomNumber, date)).willReturn(Optional.of(guestName));

        // when
        underTest.addBooking("Another Guest", roomNumber, date);

        // then
        verify(mockRepository, never()).addBooking(any(), any(), any());
        fail("exception should have been thrown");
    }
}