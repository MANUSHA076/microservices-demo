package com.example.userservice;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    // Database ekak nathuwa in-memory store ekak (demo walata)
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(2);

    public UserController() {
        users.put(1L, new User(1L, "Kasun Perera", "kasun@example.com"));
        users.put(2L, new User(2L, "Nimali Silva", "nimali@example.com"));
    }

    @GetMapping
    public List<User> getAll() {
        return List.copyOf(users.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = users.get(id);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User req) {
        long id = idGen.incrementAndGet();
        User user = new User(id, req.name(), req.email());
        users.put(id, user);
        return user;
    }
}
