package com.epam.addressbook.leasing.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
public class LeasingDataGateway {

    private JdbcTemplate jdbcTemplate;

    public LeasingDataGateway(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public LeasingRecord create(LeasingFields fields) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO LEASING (HOUSING_ID, PERSON_ID, ASSIGN_DATE, MONTHS) VALUES (?, ?, ?, ?)", RETURN_GENERATED_KEYS);
                    ps.setLong(1, fields.getHousingId());
                    ps.setLong(2, fields.getPersonId());
                    ps.setDate(3, Date.valueOf(fields.getAssignDate()));
                    ps.setInt(4, fields.getMonths());
                    return ps;
                }, keyHolder);

        return getById(keyHolder.getKey().longValue());
    }

    public List<LeasingRecord> findAllByPersonId(long personId) {
        return jdbcTemplate.query("SELECT ID, HOUSING_ID, PERSON_ID, ASSIGN_DATE, MONTHS FROM LEASING WHERE PERSON_ID = ?", rowMapper, personId);
    }

    private LeasingRecord getById(long id) {
        List<LeasingRecord> leasings = jdbcTemplate.query("SELECT ID, HOUSING_ID, PERSON_ID, ASSIGN_DATE, MONTHS FROM LEASING WHERE ID = ? LIMIT 1", rowMapper, id);
        return (leasings != null && leasings.size() != 0) ? leasings.get(0) : null;
    }

    private RowMapper<LeasingRecord> rowMapper = (rs, num) ->
            new LeasingRecord(rs.getLong("ID"), rs.getLong("HOUSING_ID"), rs.getLong("PERSON_ID"),
                    rs.getDate("ASSIGN_DATE").toLocalDate(), rs.getInt("MONTHS"));
}