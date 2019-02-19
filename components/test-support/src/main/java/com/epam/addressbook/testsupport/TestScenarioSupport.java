package com.epam.addressbook.testsupport;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.TimeZone;

public class TestScenarioSupport {

    public final JdbcTemplate template;
    public final DataSource dataSource;

    public TestScenarioSupport(String dbName) {
        dataSource = TestDataSourceFactory.create(dbName);
        template = new JdbcTemplate(dataSource);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void clearAllDatabases() {
        clearTables("address_book_accommodation_test", "ACCOMMODATION");
        clearTables("address_book_history_test", "HISTORY");
        clearTables("address_book_registration_test", "HOUSING", "ACCOUNT", "PERSON");
        clearTables("address_book_leasing_test", "LEASING");
    }

    private static void clearTables(String databaseName, String... tableNames) {
        JdbcTemplate template = new JdbcTemplate(TestDataSourceFactory.create(databaseName));

        for (String tableName : tableNames) {
            template.execute("DELETE FROM " + tableName);
        }
    }
}