package com.example.bookingservice;

public record Booking(Long id, Long userId, String userName, String route, int seats) {}
