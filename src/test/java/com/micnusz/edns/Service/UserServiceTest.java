package com.micnusz.edns.Service;

import com.micnusz.edns.dto.UserRequest;
import com.micnusz.edns.dto.UserResponse;
import com.micnusz.edns.exception.EmailAlreadyExistsException;
import com.micnusz.edns.mapper.UserMapper;
import com.micnusz.edns.model.User;
import com.micnusz.edns.repository.UserRepository;
import com.micnusz.edns.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private static final UUID TEST_USER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID TEST_OTHER_USER_ID = UUID.fromString("987f6543-e21b-45d3-b456-123456789012");
    private static final String TEST_EMAIL = "john@example.com";
    private static final String TEST_OTHER_EMAIL = "jane@example.com";
    private static final LocalDateTime TEST_CREATED_AT = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

    @Test
    void shouldCreateUserSuccessfully() {
        // Given
        UserRequest request = new UserRequest(TEST_EMAIL);

        User user = User.builder()
                .email(TEST_EMAIL)
                .build();

        User savedUser = User.builder()
                .id(TEST_USER_ID)
                .email(TEST_EMAIL)
                .enabled(true)
                .createdAt(TEST_CREATED_AT)
                .build();

        UserResponse expectedResponse = UserResponse.builder()
                .id(TEST_USER_ID)
                .email(TEST_EMAIL)
                .createdAt(TEST_CREATED_AT)
                .build();

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(expectedResponse);

        // When
        UserResponse response = userService.createUser(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(TEST_USER_ID);
        assertThat(response.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(response.getCreatedAt()).isEqualTo(TEST_CREATED_AT);

        verify(userRepository).findByEmail(TEST_EMAIL);
        verify(userMapper).toEntity(request);
        verify(userRepository).save(user);
        verify(userMapper).toDto(savedUser);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        UserRequest request = new UserRequest(TEST_EMAIL);

        User existingUser = User.builder()
                .id(TEST_USER_ID)
                .email(TEST_EMAIL)
                .enabled(true)
                .createdAt(TEST_CREATED_AT.minusDays(1))
                .build();

        when(userRepository.findByEmail(TEST_EMAIL))
                .thenReturn(Optional.of(existingUser));

        // When & Then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining(TEST_EMAIL);

        verify(userRepository).findByEmail(TEST_EMAIL);
        verify(userMapper, never()).toEntity(any());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void shouldGetAllUsers() {
        // Given
        User user1 = User.builder()
                .id(TEST_USER_ID)
                .email(TEST_EMAIL)
                .enabled(true)
                .createdAt(TEST_CREATED_AT)
                .build();

        User user2 = User.builder()
                .id(TEST_OTHER_USER_ID)
                .email(TEST_OTHER_EMAIL)
                .enabled(true)
                .createdAt(TEST_CREATED_AT.plusHours(1))
                .build();

        UserResponse response1 = UserResponse.builder()
                .id(TEST_USER_ID)
                .email(TEST_EMAIL)
                .createdAt(TEST_CREATED_AT)
                .build();

        UserResponse response2 = UserResponse.builder()
                .id(TEST_OTHER_USER_ID)
                .email(TEST_OTHER_EMAIL)
                .createdAt(TEST_CREATED_AT.plusHours(1))
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.toDto(user1)).thenReturn(response1);
        when(userMapper.toDto(user2)).thenReturn(response2);

        // When
        List<UserResponse> users = userService.getUsers();

        // Then
        assertThat(users)
                .hasSize(2)
                .extracting(
                        UserResponse::getId,
                        UserResponse::getEmail,
                        UserResponse::getCreatedAt
                )
                .containsExactly(
                        tuple(TEST_USER_ID, TEST_EMAIL, TEST_CREATED_AT),
                        tuple(TEST_OTHER_USER_ID, TEST_OTHER_EMAIL, TEST_CREATED_AT.plusHours(1))
                );

        verify(userRepository).findAll();
        verify(userMapper, times(2)).toDto(any(User.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() {
        // Given
        when(userRepository.findAll()).thenReturn(List.of());

        // When
        List<UserResponse> users = userService.getUsers();

        // Then
        assertThat(users).isEmpty();
        verify(userRepository).findAll();
        verify(userMapper, never()).toDto(any());
    }


    @Test
    void shouldCreateUserWithMapperThatSetsId() {
        // Given
        UserRequest request = new UserRequest(TEST_EMAIL);

        User userWithId = User.builder()
                .id(TEST_USER_ID)
                .email(TEST_EMAIL)
                .createdAt(TEST_CREATED_AT)
                .build();

        UserResponse expectedResponse = UserResponse.builder()
                .id(TEST_USER_ID)
                .email(TEST_EMAIL)
                .createdAt(TEST_CREATED_AT)
                .build();

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(userMapper.toEntity(request)).thenReturn(userWithId);
        when(userRepository.save(userWithId)).thenReturn(userWithId);
        when(userMapper.toDto(userWithId)).thenReturn(expectedResponse);

        // When
        UserResponse response = userService.createUser(request);

        // Then
        assertThat(response.getId()).isEqualTo(TEST_USER_ID);

        verify(userRepository).save(userWithId);
    }

    @Test
    void shouldCreateMultipleUsersWithDifferentEmails() {
        // Given
        UserRequest request1 = new UserRequest("user1@example.com");
        UserRequest request2 = new UserRequest("user2@example.com");

        User user1 = User.builder().email("user1@example.com").build();
        User user2 = User.builder().email("user2@example.com").build();

        User savedUser1 = User.builder()
                .id(UUID.randomUUID())
                .email("user1@example.com")
                .createdAt(TEST_CREATED_AT)
                .build();

        User savedUser2 = User.builder()
                .id(UUID.randomUUID())
                .email("user2@example.com")
                .createdAt(TEST_CREATED_AT.plusMinutes(1))
                .build();

        when(userRepository.findByEmail("user1@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("user2@example.com")).thenReturn(Optional.empty());
        when(userMapper.toEntity(request1)).thenReturn(user1);
        when(userMapper.toEntity(request2)).thenReturn(user2);
        when(userRepository.save(user1)).thenReturn(savedUser1);
        when(userRepository.save(user2)).thenReturn(savedUser2);
        when(userMapper.toDto(savedUser1)).thenReturn(
                UserResponse.builder()
                        .id(savedUser1.getId())
                        .email("user1@example.com")
                        .createdAt(TEST_CREATED_AT)
                        .build()
        );
        when(userMapper.toDto(savedUser2)).thenReturn(
                UserResponse.builder()
                        .id(savedUser2.getId())
                        .email("user2@example.com")
                        .createdAt(TEST_CREATED_AT.plusMinutes(1))
                        .build()
        );

        // When
        UserResponse response1 = userService.createUser(request1);
        UserResponse response2 = userService.createUser(request2);

        // Then
        assertThat(response1.getEmail()).isEqualTo("user1@example.com");
        assertThat(response2.getEmail()).isEqualTo("user2@example.com");
        assertThat(response1.getId()).isNotEqualTo(response2.getId());

        verify(userRepository, times(2)).findByEmail(anyString());
        verify(userRepository, times(2)).save(any(User.class));
    }
}