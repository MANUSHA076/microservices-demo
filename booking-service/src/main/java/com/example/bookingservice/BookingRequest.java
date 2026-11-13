package com.example.bookingservice;

public record BookingRequest(Long userId, String route, int seats) {}
