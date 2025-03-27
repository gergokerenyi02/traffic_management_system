package com.example.licensePlate.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import com.example.licensePlate.exception.ValidationException;
import com.example.licensePlate.model.Client;
import com.example.licensePlate.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class ClientService {


    @Autowired
    private ClientRepository clientRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Client registerClient(String username, String rawPassword) {
        System.out.println("Trying to register client with username: " + username);
        if(username == null || username.isEmpty()){
            System.out.println("Username is null or empty!");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "The name you entered can not be null or empty!");
        }

        if (clientRepository.findByUsername(username).isPresent()) {
            System.out.println("Username is already in use!");
            throw new ValidationException(HttpStatus.BAD_REQUEST,"Username is taken!");
        }


        Client client = new Client();
        client.setUsername(username);
        client.setPassword(rawPassword);
        return clientRepository.save(client);


    }



    public boolean validateLogin(String username, String rawPassword) {
        Optional<Client> clientOpt = clientRepository.findByUsername(username);
        return clientOpt.isPresent() && passwordEncoder.matches(rawPassword, clientOpt.get().getPassword());
    }


}