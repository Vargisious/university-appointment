package com.purple.ua.universityappointment.security;

import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(userLogin);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found:  " + userLogin));
        return user.map(MyUserDetails::new).get();
    }
}
