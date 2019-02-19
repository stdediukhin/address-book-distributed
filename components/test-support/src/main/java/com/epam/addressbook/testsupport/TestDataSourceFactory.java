package com.epam.addressbook.testsupport;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;

public class TestDataSourceFactory {
    public static DataSource create(String databaseName) {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/" + databaseName + "?useSSL=false&useTimezone=true&serverTimezone=UTC&useLegacyDatetimeCode=false");
        dataSource.setUser("address_book");

        return dataSource;
    }
}