package com.epam.addressbook.accommodation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationForm {
    private long housingId;
    private long personId;
    private boolean singleOwned;
    private String startDate;
    private String endDate;
}