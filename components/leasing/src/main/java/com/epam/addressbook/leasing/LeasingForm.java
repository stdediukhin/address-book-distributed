package com.epam.addressbook.leasing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeasingForm {
    private long housingId;
    private long personId;
    private String assignDate;
    private int months;
}