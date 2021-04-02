package com.jira.ibm.repositories;

import com.jira.ibm.domain.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserCredentials, Long> {
    UserCredentials findByEmail(String email);

    UserCredentials findByUserName(String userName);
}