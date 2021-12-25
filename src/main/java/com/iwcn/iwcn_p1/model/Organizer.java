package com.iwcn.iwcn_p1.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // Contructor needed to load data from DB
@AllArgsConstructor

@Entity
@DiscriminatorValue("1")
public class Organizer extends User {

    public interface Events {}

    @OneToMany(mappedBy = "organizer",fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Events.class)
    private List<Event> events;

    @Builder
    public Organizer(Long id, String username, String email, String encodedPassword, List<Event> events) {
        super(id, username, email, encodedPassword);
        this.events = events;
    }
}