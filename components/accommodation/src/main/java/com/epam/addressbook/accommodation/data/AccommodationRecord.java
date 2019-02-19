package com.epam.addressbook.accommodation.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationRecord {
    private long id;
    private long housingId;
    private long personId;
    private boolean singleOwned;
    private LocalDate startDate;
    private LocalDate endDate;
}