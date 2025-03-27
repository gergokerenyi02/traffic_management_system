package com.example.licensePlate.model;

import jakarta.persistence.*;
//import jakarta.validation.ValidationException;ú
import com.example.licensePlate.exception.ValidationException;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@Entity
@Getter @Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Size(max = 100)
    private String username;



    @Size(min = 8)
    private String password;

    public void setPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 8) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Password length must be at least 8.");
        }
        if (!rawPassword.matches("^[a-zA-Z0-9]+$")) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Password can only contain letters and numbers (no whitespaces or special characters).");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(rawPassword);
    }

}