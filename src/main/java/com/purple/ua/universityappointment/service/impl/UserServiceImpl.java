package com.purple.ua.universityappointment.service.impl;

import com.purple.ua.universityappointment.model.ConfirmationToken;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.ConfirmationTokenRepository;
import com.purple.ua.universityappointment.repository.UserRepository;
import com.purple.ua.universityappointment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.purple.ua.universityappointment.util.UserMapper.INSTANCE;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public List<User> getAllLecturers() {
        return userRepository.findByRoles("lecturer");
    }

    @Override
    public List<User> getAllStudents() {
        return userRepository.findByRoles("student");
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        String login = user.getLogin();
        String email = user.getEmail();
        if (userRepository.findByLogin(login).isPresent()) {
            throw new RuntimeException(String.format("Login is alredy in use"));
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException(String.format("Email is alredy in use"));

        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User save = userRepository.save(user);
            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("anotherdayofbill2017@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:8082/confirm-account?token=" + confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);
            return save;
        }
    }

    @Override
    public User updateUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public User deleteUser(User user) {
        userRepository.delete(user);
        return user;
    }
}
