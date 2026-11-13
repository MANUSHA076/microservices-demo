package com.example.bookingservice;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final Map<Long, Booking> bookings = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);
    private final RestClient userClient;

    public BookingController(RestClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping
    public List<Booking> getAll() {
        return List.copyOf(bookings.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {
        Booking b = bookings.get(id);
        return b == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(b);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking create(@RequestBody BookingRequest req) {
        // Service-to-service call: booking-service -> user-service
        UserDto user = fetchUser(req.userId());

        long id = idGen.incrementAndGet();
        Booking booking = new Booking(id, user.id(), user.name(), req.route(), req.seats());
        bookings.put(id, booking);
        return booking;
    }

    private UserDto fetchUser(Long userId) {
        try {
            return userClient.get().uri("/users/{id}", userId).retrieve().body(UserDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + userId + " not found");
        } catch (ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "user-service is down");
        }
    }
}
