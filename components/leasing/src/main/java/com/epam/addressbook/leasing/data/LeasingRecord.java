package com.epam.addressbook.leasing.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeasingRecord {
    private long id;
    private long housingId;
    private long personId;
    private LocalDate assignDate;
    private int months;
}