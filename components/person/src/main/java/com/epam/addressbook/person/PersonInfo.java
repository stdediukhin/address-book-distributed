package com.epam.addressbook.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonInfo {
    private long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private String info;
}