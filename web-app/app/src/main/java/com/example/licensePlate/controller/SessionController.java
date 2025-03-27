package com.example.licensePlate.controller;



import com.example.licensePlate.model.Client;
import com.example.licensePlate.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/session")
public class SessionController {


    @Autowired
    private ClientService clientService;

    @Autowired
    private HttpSession session;


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        try {
            Client client = clientService.registerClient(username, password);
            Map<String, String> response = new HashMap<>();

            System.out.println(username + " registered successfully!");

            response.put("message", "Registration successful");
            session.setAttribute("username", client.getUsername());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        System.out.println(username + " is trying to login...");

        if (clientService.validateLogin(username, password)) {
            session.setAttribute("username", username);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Login successful!");

            System.out.println(username + " logged in successfully");
            return ResponseEntity.ok(response);
        }

        System.out.println("Login failed, invalid username or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password!"));
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {

        // NOTE: LOGOUT WILL BE HANDLED BY config/SecurityConfig.java
        // THE CODE BELOW MIGHT NOT BE LOGGED ON THE CONSOLE, SINCE IT'S HANDLED DIFFERENTLY.

        System.out.println(session.getAttribute("username") + " logged out successfully");
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully!"));
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validateSession() {

        System.out.println("Validating session...");
        String username = (String) session.getAttribute("username");
        Map<String, String> response = new HashMap<>();

        if (username == null) {
            System.out.println("User not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User is not logged in"));
        }


        System.out.println(username + " is logged in.");
        response.put("status", "success");
        response.put("message", "User is logged in!");
        response.put("username", username);
        return ResponseEntity.ok(response);

    }

}
