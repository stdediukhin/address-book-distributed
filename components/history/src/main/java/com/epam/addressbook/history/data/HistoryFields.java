package com.epam.addressbook.history.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistoryFields {
    private long housingId;
    private String name;
    private String description;
}