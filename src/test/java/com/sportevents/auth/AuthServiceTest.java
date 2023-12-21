package com.sportevents.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.sportevents.common.UserRole;
import com.sportevents.request.RegisterRequest;
import com.sportevents.user.User;
import com.sportevents.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {
    @Mock
    private FirebaseAuth firebaseAuth;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testInvalidEmail() {
        // given
        RegisterRequest registerRequest = new RegisterRequest(
                "testmail.com",
                "testUsername",
                "Testowe1",
                "Testowe1"
        );

        //when
        ResponseEntity<String> response = authService.register(registerRequest);

        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Invalid email", response.getBody());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void testInvalidPassword() {
        // given
        RegisterRequest registerRequest = new RegisterRequest(
                "test@mail.com",
                "testUsername",
                "invalid",
                "invalid"
        );

        //when
        ResponseEntity<String> response = authService.register(registerRequest);

        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Password does not match the requirements", response.getBody());
        verify(userRepository, times(0)).save(any(User.class));
    }
}
