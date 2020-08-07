package com.purple.ua.universityappointment.service.impl;

import com.purple.ua.universityappointment.exception.UserNotFoundException;
import com.purple.ua.universityappointment.model.ConfirmationToken;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.ConfirmationTokenRepository;
import com.purple.ua.universityappointment.repository.UserRepository;
import com.purple.ua.universityappointment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public User getUserById(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<User> getAllLecturers() {
        return userRepository.findByRoles("ROLE_LECTURER");
    }

    @Override
    public List<User> getAllStudents() {
        return userRepository.findByRoles("ROLE_STUDENT");
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

            emailSenderService.mailInput(user.getEmail(), "Complete Registration!"
                    , "anotherdayofbill2017@gmail.com", "To confirm your account, please click here : "
                            + "http://localhost:8080/user/confirm-account?token="
                            + confirmationToken.getConfirmationToken());

            return save;
        }
    }

    @Override
    public User updateUser(User user) {
        UserServiceImpl userService = new UserServiceImpl();
        User ensured = userService.userNameAndPasswordEnsure(user);
        return userRepository.save(ensured);
    }


    @Override
    public User deleteUserById(long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            confirmationTokenRepository.deleteByUserId(id);
            userRepository.deleteById(id);
            return user.get();
        } else {
            throw new UserNotFoundException();
        }

    }

    public User userNameAndPasswordEnsure(User user) {
        User oldUser = userRepository.findById(user.getId()).get();
        user.setLogin(oldUser.getLogin());
        user.setEmail(oldUser.getEmail());
        return user;
    }
}
