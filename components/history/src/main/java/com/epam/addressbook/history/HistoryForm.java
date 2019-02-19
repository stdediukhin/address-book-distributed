package com.epam.addressbook.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryForm {
    private long housingId;
    private String name;
    private String description;
}