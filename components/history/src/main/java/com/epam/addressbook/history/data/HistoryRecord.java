package com.epam.addressbook.history.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRecord {
    private long id;
    private long housingId;
    private String name;
    private String description;
}