package com.epam.addressbook.leasing.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LeasingFields {
    private long housingId;
    private long personId;
    private LocalDate assignDate;
    private int months;
}