package com.iwcn.iwcn_p1.repository;

import java.util.List;

import com.iwcn.iwcn_p1.model.Ticket;
import com.iwcn.iwcn_p1.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long>{

    List<Ticket> findByCustomer(User user);

/*     @Modifying
    @Query("DELETE FROM UserRoleUser uru WHERE uru.id in ?1")
    @Transactional
    void delete(Set<Integer> id); */

}
