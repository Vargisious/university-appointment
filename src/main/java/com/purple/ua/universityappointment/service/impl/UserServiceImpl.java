package com.purple.ua.universityappointment.service.impl;

import com.purple.ua.universityappointment.dto.UserDto;
import com.purple.ua.universityappointment.exception.FieldAlreadyInUseException;
import com.purple.ua.universityappointment.exception.UserNotFoundException;
import com.purple.ua.universityappointment.model.ConfirmationToken;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.ConfirmationTokenRepository;
import com.purple.ua.universityappointment.repository.UserRepository;
import com.purple.ua.universityappointment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.purple.ua.universityappointment.util.UserMapper.INSTANCE;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public UserDto getUserById(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        return INSTANCE.toDto(user.orElseThrow(() ->
                new UserNotFoundException(HttpStatus.NOT_FOUND, "User with id: " + id + " not found")));
    }

    @Override
    public List<UserDto> getAllLecturers() {
        List<User> users = userRepository.findByRoles("ROLE_LECTURER");
        if (!users.isEmpty()) {
            return INSTANCE.listToDto(users);
        } else {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "No lecturers found");
        }
    }

    @Override
    public List<UserDto> getAllStudents() {
        List<User> users = userRepository.findByRoles("ROLE_STUDENT");
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

            mailInput(userDto.getEmail(), "Complete Registration!"
                    , "To confirm your account, please click here : "
                            + "http://localhost:8080/confirm-account?token="
                            + confirmationToken.getConfirmationToken());

            return INSTANCE.toDto(save);
        }
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserServiceImpl userService = new UserServiceImpl();
        User ensured = userService.userNameAndPasswordEnsure(INSTANCE.toEntity(userDto));
        User user = userRepository.save(ensured);
        return INSTANCE.toDto(user);
    }


    @Override
    public UserDto deleteUserById(long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(HttpStatus.NOT_FOUND, "User with id: " + id + " not found"));
        confirmationTokenRepository.deleteByUserId(id);
        userRepository.deleteById(id);
        return INSTANCE.toDto(user);
    }

    public User userNameAndPasswordEnsure(User user) {
        User oldUser = userRepository.findById(user.getId()).get();
        user.setUserName(oldUser.getUserName());
        user.setEmail(oldUser.getEmail());
        return user;
    }

    public void mailInput(String email, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        emailSenderService.sendEmail(mailMessage);
    }
}
