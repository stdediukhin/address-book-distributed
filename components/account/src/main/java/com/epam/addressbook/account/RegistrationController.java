package com.epam.addressbook.account;

import com.epam.addressbook.person.PersonInfo;
import com.epam.addressbook.person.data.PersonRecord;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    private final RegistrationService service;

    public RegistrationController(RegistrationService service) {
        this.service = service;
    }

    @PostMapping("/registration")
    public PersonInfo create(@RequestBody RegistrationForm form) {
        PersonRecord record = service.createPersonWithAccount(form.getLoginName(), form.getLoginPassword(), form.getFirstName(), form.getLastName(), form.getEmail(), form.getPhone());
        return new PersonInfo(record.getId(), record.getFirstName(), record.getLastName(), record.getFullName(), record.getEmail(), record.getPhone(), "registration info");
    }
}