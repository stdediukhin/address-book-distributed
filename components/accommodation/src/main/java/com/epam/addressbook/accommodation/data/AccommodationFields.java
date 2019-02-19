package com.epam.addressbook.accommodation.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AccommodationFields {
    private long housingId;
    private long personId;
    private boolean singleOwned;
    private LocalDate startDate;
    private LocalDate endDate;
}