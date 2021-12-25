package com.iwcn.iwcn_p1.repository;

import java.util.List;

import com.iwcn.iwcn_p1.model.Event;
import com.iwcn.iwcn_p1.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface EventRepository extends JpaRepository<Event, Long>{

    //Sentencia SQL atómica con la precondición de que queden plazas libres (más eficiente que  usar un nivel de aislamiento de las transacciones SERIALIZABLE. 
    //Eso implica que la primera transacción consigue la entrada y la segunda detecta que ya no hay disponibles)
    @Transactional
    @Modifying
    @Query("UPDATE Event e SET e.reservedSeats=e.reservedSeats+1 "+ 
    "WHERE e.id = :id AND e.reservedSeats+1 <= e.maxSeats")
    public int reserveSeat(@Param("id") long id);

    @Transactional
    @Modifying
    @Query("UPDATE Event e SET e.reservedSeats=e.reservedSeats-1 "+ 
    "WHERE e.id = :id AND e.reservedSeats-1 >= 0")
    void removeSeatReservation(@Param("id") long id);

    public List<Event> findByOrganizer(User user);
}
