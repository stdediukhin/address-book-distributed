package com.epam.addressbook.account.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRecord {
    private long id;
    private long personId;
    private String loginName;
    private String loginPassword;
}