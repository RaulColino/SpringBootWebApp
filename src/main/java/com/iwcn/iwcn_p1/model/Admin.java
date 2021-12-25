package com.iwcn.iwcn_p1.model;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // Contructor needed to load data from DB

@Entity
@DiscriminatorValue("0")
public class Admin extends User {

    @Builder
    public Admin(Long id, String username, String email, String encodedPassword, List<Ticket> tickets) {
        super(id, username, email, encodedPassword);
    }
}
