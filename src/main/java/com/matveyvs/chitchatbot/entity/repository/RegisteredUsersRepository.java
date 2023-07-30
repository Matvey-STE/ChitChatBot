package com.matveyvs.chitchatbot.entity.repository;

import com.matveyvs.chitchatbot.entity.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RegisteredUsersRepository extends JpaRepository<RegisteredUser, Integer> {
    List<RegisteredUser> findAll();
    RegisteredUser findByUserName(String userName);

    void deleteByUserName(String userName);
}
