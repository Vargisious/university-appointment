package com.purple.ua.universityappointment.repository;

import com.purple.ua.universityappointment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByRoles(String role);

    Optional<User> findByLogin(String login);

    User findByFirstNameAndLastName(String firstName, String lastName);

    Optional<User> findByEmail(String email);


}
