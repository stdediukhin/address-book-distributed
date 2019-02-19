package com.epam.addressbook.account.data;

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
public class AccountDataGateway {
    private final JdbcTemplate jdbcTemplate;

    public AccountDataGateway(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public AccountRecord create(long personId, String loginName, String loginPassword) {
        KeyHolder keyholder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO ACCOUNT (PERSON_ID, LOGIN_NAME, LOGIN_PASSWORD) VALUES (?, ?, ?)", RETURN_GENERATED_KEYS
            );

            ps.setLong(1, personId);
            ps.setString(2, loginName);
            ps.setString(3, loginPassword);
            return ps;
        }, keyholder);

        long id = keyholder.getKey().longValue();

        List<AccountRecord> accounts = jdbcTemplate.query("SELECT ID, PERSON_ID, LOGIN_NAME, LOGIN_PASSWORD FROM ACCOUNT WHERE ID = ? LIMIT 1", rowMapper, id);

        return (accounts != null && accounts.size() != 0) ? accounts.get(0) : null;
    }

    public List<AccountRecord> findAllByPersonId(long personId) {
        return jdbcTemplate.query(
                "SELECT ID, PERSON_ID, LOGIN_NAME, LOGIN_PASSWORD FROM ACCOUNT WHERE PERSON_ID = ? ORDER BY LOGIN_NAME ASC",
                rowMapper, personId
        );
    }

    private RowMapper<AccountRecord> rowMapper = (rs, num) ->
            new AccountRecord(rs.getLong("ID"), rs.getLong("PERSON_ID"), rs.getString("LOGIN_NAME"), rs.getString("LOGIN_PASSWORD"));
}