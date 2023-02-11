package com.example.reigie.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenUser {


    public static final String CLAIM_NAME_ID = "CLAIM_NAME_ID";
    public static final String CLAIM_NAME_USERNAME = "CLAIM_NAME_USERNAME";

    private Long id;
    private String username;

    public boolean isAdmin(){
        return username.equals("admin");
    }


}
