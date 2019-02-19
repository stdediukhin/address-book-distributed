package com.epam.addressbook.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationForm {
    private String loginName;
    private String loginPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}