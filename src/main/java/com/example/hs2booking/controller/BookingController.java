package com.example.hs2booking.controller;

import com.example.hs2booking.model.dto.BookingDTO;
import com.example.hs2booking.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.hs2booking.util.ValidationMessages.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllRecords(
            @RequestParam(value = "page", defaultValue = "0") @Min(value = 0, message = MSG_PAGE_NEGATIVE) int page,
            @RequestParam(value = "size", defaultValue = "5") @Min(value = 0, message = MSG_SIZE_NEGATIVE) @Max(value = 50, message = MSG_SIZE_TOO_BIG) int size,
            @RequestParam(value = "playerId", required = false) @Min(value = 0, message = MSG_ID_NEGATIVE) Long playerId,
            @RequestParam(value = "teamId", required = false) @Min(value = 0, message = MSG_ID_NEGATIVE) Long teamId,
            @RequestParam(value = "playgroundId", required = false) @Min(value = 0, message = MSG_ID_NEGATIVE) Long playgroundId
    ) {
        List<BookingDTO> bookings;

        if (playerId != null) {
            bookings = bookingService.getBookingsByPlayer(playerId);
        } else if (teamId != null) {
            bookings = bookingService.getBookingsByTeam(teamId);
        } else if (playgroundId != null) {
            bookings = bookingService.getBookingsByPlayground(playgroundId);
        } else {
            bookings = bookingService.findAll(page, size);
        }

        return ResponseEntity.ok(bookings);
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<?> getRecordById(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long bookingId
    ) {
        BookingDTO booking = bookingService.findById(bookingId);
        return ResponseEntity.ok(booking);
    }

    /*@GetMapping(value = "/")
    public ResponseEntity<?> getRecordByPlayerId(
            @RequestParam(value = "playerId") @Min(value = 0, message = MSG_ID_NEGATIVE) long playerId
    ) {
        List<BookingDTO> bookings = bookingService.getBookingsByPlayer(playerId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> getRecordByTeamId(
            @RequestParam(value = "teamId") @Min(value = 0, message = MSG_ID_NEGATIVE) long teamId
    ) {
        List<BookingDTO> bookings = bookingService.getBookingsByTeam(teamId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> getRecordByPlaygroundId(
            @RequestParam(value = "playgroundId") @Min(value = 0, message = MSG_ID_NEGATIVE) long playgroundId
    ) {
        List<BookingDTO> bookings = bookingService.getBookingsByPlayground(playgroundId);
        return ResponseEntity.ok(bookings);
    }*/

    @PostMapping(value = "/")
    @PreAuthorize("hasAnyRole('PLAYER', 'TEAM_MANAGER')")
    public ResponseEntity<?> createBookingRecord(@Valid @RequestBody BookingDTO newRecord) {
        BookingDTO created = bookingService.create(newRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping(value = "/{recordId}")
    @PreAuthorize("hasAnyRole('PLAYER', 'TEAM_MANAGER')")
    public ResponseEntity<?> deleteBookingRecord(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long recordId
    ) {
        bookingService.delete(recordId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
