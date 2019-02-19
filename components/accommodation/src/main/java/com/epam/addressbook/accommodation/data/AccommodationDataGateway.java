package com.epam.addressbook.accommodation.data;

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
public class AccommodationDataGateway {

    private JdbcTemplate jdbcTemplate;

    public AccommodationDataGateway(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public AccommodationRecord create(AccommodationFields fields) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO ACCOMMODATION (HOUSING_ID, PERSON_ID, " +
                    "SINGLE_OWNED, START_DATE, END_DATE) values (?, ?, ?, ?, ?)", RETURN_GENERATED_KEYS);

            ps.setLong(1, fields.getHousingId());
            ps.setLong(2, fields.getPersonId());
            ps.setBoolean(3, fields.isSingleOwned());
            ps.setDate(4, Date.valueOf(fields.getStartDate()));
            ps.setDate(5, Date.valueOf(fields.getEndDate()));

            return ps;
        }, keyHolder);

        return getById(keyHolder.getKey().longValue());
    }

    public List<AccommodationRecord> findAllByHousingId(Long housingId) {
        return jdbcTemplate.query("SELECT ID, HOUSING_ID, PERSON_ID, SINGLE_OWNED, START_DATE, END_DATE " +
                "FROM ACCOMMODATION WHERE HOUSING_ID = ? ORDER BY START_DATE", rowMapper, housingId);
    }

    private AccommodationRecord getById(long id) {
        List<AccommodationRecord> accommodations = jdbcTemplate.query("SELECT  ID, HOUSING_ID, PERSON_ID, " +
                "SINGLE_OWNED, START_DATE, END_DATE FROM ACCOMMODATION WHERE ID = ? LIMIT 1", rowMapper, id);
        return (accommodations != null && accommodations.size() != 0) ? accommodations.get(0) : null;
    }

    private RowMapper<AccommodationRecord> rowMapper =
            (rs, rowNum) -> new AccommodationRecord(rs.getLong("ID"), rs.getLong("HOUSING_ID"), rs.getLong("PERSON_ID"),
                    rs.getBoolean("SINGLE_OWNED"), rs.getDate("START_DATE").toLocalDate(), rs.getDate("END_DATE").toLocalDate());
}