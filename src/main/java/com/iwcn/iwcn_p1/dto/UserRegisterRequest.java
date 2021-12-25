package com.iwcn.iwcn_p1.dto;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Contructor needed to load data from DB
@AllArgsConstructor
@Builder
public class UserRegisterRequest {

    private String username;
    private String email;
    private String pass;
    private String role;
}
