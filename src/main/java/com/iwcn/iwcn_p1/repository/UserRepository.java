package com.iwcn.iwcn_p1.repository;

import java.util.List;
import java.util.Optional;

import com.iwcn.iwcn_p1.model.Customer;
import com.iwcn.iwcn_p1.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

/*     @Modifying
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    @Query("DELETE FROM User u WHERE u.id = :userId")
    void deleteThisUser(long userId); */

}