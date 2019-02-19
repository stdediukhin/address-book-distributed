package com.epam.addressbook.person.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
public class PersonDataGateway {

    private final JdbcTemplate jdbcTemplate;

    public PersonDataGateway(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public PersonRecord create(String firstName, String lastName, String email, String phone) {
        KeyHolder keyholder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO PERSON (FIRST_NAME, LAST_NAME, FULL_NAME, EMAIL, PHONE) VALUES (?, ?, ?, ?, ?)", RETURN_GENERATED_KEYS);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, firstName + " " + lastName);
            ps.setString(4, email);
            ps.setString(5, phone);
            return ps;
        }, keyholder);

        return getById(keyholder.getKey().longValue());
    }

    public PersonRecord getById(long id) {
        List<PersonRecord> persons = jdbcTemplate.query("SELECT ID, FIRST_NAME, LAST_NAME, FULL_NAME, EMAIL, PHONE FROM PERSON WHERE ID = ? LIMIT 1", rowMapper, id);

        return (persons != null && persons.size() != 0) ? persons.get(0) : null;
    }

    private RowMapper<PersonRecord> rowMapper = (rs, num) -> new PersonRecord(
            rs.getLong("ID"),
            rs.getString("FIRST_NAME"),
            rs.getString("LAST_NAME"),
            rs.getString("FULL_NAME"),
            rs.getString("EMAIL"),
            rs.getString("PHONE")
    );
}