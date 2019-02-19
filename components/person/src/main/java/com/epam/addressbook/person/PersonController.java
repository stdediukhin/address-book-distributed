package com.epam.addressbook.person;

import com.epam.addressbook.person.data.PersonDataGateway;
import com.epam.addressbook.person.data.PersonRecord;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonDataGateway gateway;

    @GetMapping("/{personId}")
    public PersonInfo getById(@PathVariable long personId) {
        PersonRecord record = gateway.getById(personId);

        return (record != null) ?
                new PersonInfo(record.getId(), record.getFirstName(), record.getLastName(), record.getFullName(), record.getEmail(), record.getPhone(), "Person info") :
                null;
    }
}