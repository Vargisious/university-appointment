package com.purple.ua.universityappointment.service.impl;

import com.purple.ua.universityappointment.dto.UserDto;
import com.purple.ua.universityappointment.exception.FieldAlreadyInUseException;
import com.purple.ua.universityappointment.exception.UserNotFoundException;
import com.purple.ua.universityappointment.model.ConfirmationToken;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.ConfirmationTokenRepository;
import com.purple.ua.universityappointment.repository.UserRepository;
import com.purple.ua.universityappointment.service.UserService;
import com.purple.ua.universityappointment.util.MailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.purple.ua.universityappointment.util.mapper.UserMapper.INSTANCE;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailUtils mailUtils;

    @Override
    public UserDto getUser(long id) {
        Optional<User> user = userRepository.findById(id);
        return INSTANCE.toDto(user.orElseThrow(() ->
                new UserNotFoundException(HttpStatus.NOT_FOUND, "User with id: " + id + " not found")));
    }

    @Override
    public User getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() ->
                new UserNotFoundException(HttpStatus.NOT_FOUND, "User with id: " + id + " not found"));
    }

    @Override
    public List<UserDto> getAllLecturers() {
        List<User> users = userRepository.findAllLecturers();
        if (!users.isEmpty()) {
            return INSTANCE.listToDto(users);
        } else {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "No lecturers found");
        }
    }

    @Override
    public List<UserDto> getAllStudents() {
        List<User> users = userRepository.findAllStudents();
        if (!users.isEmpty()) {
            return INSTANCE.listToDto(users);
        } else {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "No students found");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            return INSTANCE.listToDto(users);
        }
        throw new UserNotFoundException(HttpStatus.NOT_FOUND, "No users found");
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        String userName = userDto.getUserName();
        String email = userDto.getEmail();
        if (userRepository.findByUserName(userName).isPresent()) {
            throw new FieldAlreadyInUseException(HttpStatus.CONFLICT, "Username " + userName + " is already in use");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new FieldAlreadyInUseException(HttpStatus.CONFLICT, "Email " + email + " is already in use");

        } else {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User user = INSTANCE.toEntity(userDto);
            User save = userRepository.save(user);
            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);

            mailUtils.mailInput(userDto.getEmail(), "Complete Registration!"
                    , "To confirm your account, please click here : "
                            + mailUtils.getUrl() + "/confirm-account?token="
                            + confirmationToken.getConfirmationToken());

            return INSTANCE.toDto(save);
        }
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User ensured = userNameAndPasswordEnsure(INSTANCE.toEntity(userDto));
        User user = userRepository.save(ensured);
        return INSTANCE.toDto(user);
    }


    @Override
    public UserDto deleteUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(HttpStatus.NOT_FOUND, "User with id: " + id + " not found"));
        confirmationTokenRepository.deleteByUserId(id);
        userRepository.deleteById(id);
        return INSTANCE.toDto(user);
    }


    public User userNameAndPasswordEnsure(User user) {
        User fromDB = userRepository.findById(user.getId()).orElseThrow(
                () -> new UserNotFoundException(HttpStatus.NOT_FOUND, "No lecturers found"));
        user.setUserName(fromDB.getUserName());
        user.setEmail(fromDB.getEmail());
        return user;
    }
}
