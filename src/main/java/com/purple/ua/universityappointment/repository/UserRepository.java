package com.purple.ua.universityappointment.repository;

import com.purple.ua.universityappointment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String login);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u FROM user_table u  WHERE u.roles like '%LECTURER%'")
    List<User> findAllLecturers();

    @Query(value = "SELECT u FROM user_table u  WHERE u.roles like '%STUDENT%'")
    List<User> findAllStudents();

}