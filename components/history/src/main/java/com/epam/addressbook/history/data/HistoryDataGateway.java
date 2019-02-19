package com.epam.addressbook.history.data;

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
public class HistoryDataGateway {
    private final JdbcTemplate jdbcTemplate;

    public HistoryDataGateway(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public HistoryRecord create(HistoryFields fields) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into HISTORY (HOUSING_ID, NAME, DESCRIPTION) values (?, ?, ?)", RETURN_GENERATED_KEYS);

            ps.setLong(1, fields.getHousingId());
            ps.setString(2, fields.getName());
            ps.setString(3, fields.getDescription());
            return ps;
        }, keyHolder);

        return getById(keyHolder.getKey().longValue());
    }

    public List<HistoryRecord> findAllByHousingId(Long housingId) {
        return jdbcTemplate.query("SELECT ID, HOUSING_ID, NAME, DESCRIPTION FROM HISTORY WHERE HOUSING_ID = ?", rowMapper, housingId);
    }

    private HistoryRecord getById(long id) {
        List<HistoryRecord> histories = jdbcTemplate.query("SELECT ID, HOUSING_ID, NAME, DESCRIPTION FROM HISTORY WHERE ID = ? LIMIT 1", rowMapper, id);

        return (histories != null && histories.size() != 0) ? histories.get(0) : null;
    }

    private RowMapper<HistoryRecord> rowMapper = (rs, num) ->
            new HistoryRecord(rs.getLong("ID"), rs.getLong("HOUSING_ID"), rs.getString("NAME"), rs.getString("DESCRIPTION"));
}