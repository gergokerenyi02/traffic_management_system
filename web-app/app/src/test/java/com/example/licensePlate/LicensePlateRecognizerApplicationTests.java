package com.example.licensePlate;

import com.example.licensePlate.model.Client;
import com.example.licensePlate.model.Detection;
import com.example.licensePlate.model.ParkingSession;
import com.example.licensePlate.repository.ClientRepository;
import com.example.licensePlate.repository.DetectionRepository;
import com.example.licensePlate.repository.ParkingSessionRepository;
import com.example.licensePlate.service.ClientService;
import com.example.licensePlate.service.DetectionService;
import com.example.licensePlate.service.ParkingSessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.licensePlate.exception.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PasswordTests {

    @Test
    void testSetPassword_invalidShortPassword() {
        Client client = new Client();
        assertThrows(ValidationException.class, () -> client.setPassword("asd"));
    }

    @Test
    void testSetPassword_invalidCharacters() {
        Client client = new Client();
        assertThrows(ValidationException.class, () -> client.setPassword("asdw123_"));
    }

    @Test
    void testSetPassword_validPassword() {
        Client client = new Client();
        client.setPassword("password1234");
        assertNotNull(client.getPassword());

    }

}


@SpringBootTest
@AutoConfigureTestDatabase
class ParkingSessionRepositoryTest {

    @Autowired
    private ParkingSessionRepository parkingSessionRepository;

    @Test
    void testSaveAndFindByLicensePlate() {
        ParkingSession session = new ParkingSession("TESZT1", LocalDateTime.now());
        parkingSessionRepository.save(session);

        Optional<ParkingSession> found = parkingSessionRepository.findByLicensePlate("TESZT1");
        assertTrue(found.isPresent());
        assertEquals("TESZT1", found.get().getLicensePlate());
    }

    @Test
    void testDeleteByLicensePlate() {
        ParkingSession session = new ParkingSession("LKM256", LocalDateTime.now());
        parkingSessionRepository.save(session);

        parkingSessionRepository.deleteByLicensePlate("LKM256");

        Optional<ParkingSession> found = parkingSessionRepository.findByLicensePlate("LKM256");
        assertFalse(found.isPresent());
    }
}



@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Test
    void testRegisterClient_usernameIsEmpty() {
        assertThrows(ValidationException.class,
                () -> clientService.registerClient("", "validPassword123"));
    }

    @Test
    void testRegisterClient_usernameIsTaken() {
        when(clientRepository.findByUsername("johny")).thenReturn(Optional.of(new Client()));

        assertThrows(ValidationException.class,
                () -> clientService.registerClient("johny", "validPassword123"));
    }

    @Test
    void testRegisterClient_success() {
        when(clientRepository.findByUsername("johny")).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client client = invocation.getArgument(0);
            client.setId(1L);
            return client;
        });

        Client saved = clientService.registerClient("johny", "password123");
        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals("johny", saved.getUsername());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testValidateLogin_wrongPassword() {
        Client client = new Client();
        client.setUsername("johny");
        client.setPassword("password123");

        when(clientRepository.findByUsername("johny")).thenReturn(Optional.of(client));

        boolean result = clientService.validateLogin("johny", "badPassword123");
        assertFalse(result);
    }

    @Test
    void testValidateLogin_success() {
        Client client = new Client();
        client.setUsername("johny");
        client.setPassword("password123");

        when(clientRepository.findByUsername("johny")).thenReturn(Optional.of(client));

        boolean result = clientService.validateLogin("johny", "password123");
        assertTrue(result);
    }
}

@ExtendWith(MockitoExtension.class)
class ParkingSessionServiceTest {

    @InjectMocks
    private ParkingSessionService parkingSessionService;

    @Mock
    private ParkingSessionRepository parkingSessionRepository;

    @Test
    void testParkCar_success() {
        String plate = "LKM256";
        ParkingSession session = new ParkingSession(plate, LocalDateTime.now());

        when(parkingSessionRepository.save(any(ParkingSession.class))).thenReturn(session);

        ParkingSession saved = parkingSessionService.parkCar(plate);
        assertNotNull(saved);
        assertEquals(plate, saved.getLicensePlate());
    }

    @Test
    void testParkCar_repositoryThrowsException() {
        String plate = "LKM256";
        when(parkingSessionRepository.save(any(ParkingSession.class))).thenThrow(new RuntimeException("Database Error"));

        ParkingSession saved = parkingSessionService.parkCar(plate);

        assertNull(saved);
    }

    @Test
    void testExitCar_found() {
        String plate = "LKM256";
        ParkingSession session = new ParkingSession(plate, LocalDateTime.now());

        when(parkingSessionRepository.findByLicensePlate(plate)).thenReturn(Optional.of(session));
        boolean result = parkingSessionService.exitCar(plate);

        assertTrue(result);
        verify(parkingSessionRepository, times(1)).deleteByLicensePlate(plate);
    }

    @Test
    void testExitCar_notFound() {
        String plate = "LKM256";
        when(parkingSessionRepository.findByLicensePlate(plate)).thenReturn(Optional.empty());

        boolean result = parkingSessionService.exitCar(plate);
        assertFalse(result);
        verify(parkingSessionRepository, never()).deleteByLicensePlate(anyString());
    }
}

@ExtendWith(MockitoExtension.class)
class DetectionServiceTest {

    @InjectMocks
    private DetectionService detectionService;

    @Mock
    private DetectionRepository detectionRepository;

    @Test
    void testSaveDetection() {
        Detection detection = new Detection(
                LocalDateTime.now(),
                "200",
                "Some message",
                "LKM256",
                99.9
        );

        when(detectionRepository.save(any(Detection.class))).thenAnswer(invocation -> {
            Detection d = invocation.getArgument(0);
            d.setDetectionId(5L);
            return d;
        });

        Detection saved = detectionService.saveDetection(detection);
        assertEquals(5L, saved.getDetectionId());
        assertEquals("LKM256", saved.getLicensePlate());
    }

    @Test
    void testGetNdetections() {
        Detection d1 = new Detection(LocalDateTime.now(), "200", "ok", "TESZT1", 98.3);
        Detection d2 = new Detection(LocalDateTime.now(),  "200", "ok", "TESZT2", 90.1);
        Detection d3 = new Detection(LocalDateTime.now(),  "200", "ok", "TESZT3", 85.0);

        List<Detection> all = List.of(d1, d2, d3);
        when(detectionRepository.findAll()).thenReturn(all);

        List<Detection> result = detectionService.getNdetections(2);

        assertEquals(2, result.size());
        assertEquals("TESZT3", result.get(0).getLicensePlate());
        assertEquals("TESZT2", result.get(1).getLicensePlate());
    }
}
