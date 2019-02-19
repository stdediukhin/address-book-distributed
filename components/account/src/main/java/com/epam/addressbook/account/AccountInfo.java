package com.epam.addressbook.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
    private long id;
    private long personId;
    private String loginName;
    private String loginPassword;
    private String info;
}