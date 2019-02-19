package com.epam.addressbook.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryInfo {
    private long id;
    private long housingId;
    private String name;
    private String description;
    private String info;
}