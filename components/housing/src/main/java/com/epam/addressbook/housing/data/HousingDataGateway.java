package com.epam.addressbook.housing.data;

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
public class HousingDataGateway {

    private final JdbcTemplate jdbcTemplate;

    public HousingDataGateway(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public HousingRecord create(HousingFields fields) {
        KeyHolder keyholder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO HOUSING (ACCOUNT_ID, NAME, ADDRESS_LINE_1, ADDRESS_LINE_2, CITY, STATE, ZIP5, ACTIVE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", RETURN_GENERATED_KEYS);
            ps.setLong(1, fields.getAccountId());
            ps.setString(2, fields.getName());
            ps.setString(3, fields.getAddressLine1());
            ps.setString(4, fields.getAddressLine2());
            ps.setString(5, fields.getCity());
            ps.setString(6, fields.getState());
            ps.setString(7, fields.getZip5());
            ps.setBoolean(8, true);
            return ps;
        }, keyholder);

        return findById(keyholder.getKey().longValue());
    }

    public List<HousingRecord> findAllByAccountId(Long accountId) {
        return jdbcTemplate.query(
                "SELECT ID, ACCOUNT_ID, NAME, ADDRESS_LINE_1, ADDRESS_LINE_2, CITY, STATE, ZIP5, ACTIVE FROM HOUSING WHERE ACCOUNT_ID = ? ORDER BY NAME ASC",
                rowMapper, accountId
        );
    }

    public HousingRecord findById(long id) {
        List<HousingRecord> housings = jdbcTemplate.query("SELECT ID, ACCOUNT_ID, NAME, ADDRESS_LINE_1, ADDRESS_LINE_2, CITY, STATE, ZIP5, ACTIVE FROM HOUSING WHERE ID = ? ORDER BY NAME ASC", rowMapper, id);
        return (housings != null && housings.size() != 0) ? housings.get(0) : null;
    }

    private RowMapper<HousingRecord> rowMapper =
            (rs, num) -> new HousingRecord(
                    rs.getLong("ID"),
                    rs.getLong("ACCOUNT_ID"),
                    rs.getString("NAME"),
                    rs.getString("ADDRESS_LINE_1"),
                    rs.getString("ADDRESS_LINE_2"),
                    rs.getString("CITY"),
                    rs.getString("STATE"),
                    rs.getString("ZIP5"),
                    rs.getBoolean("ACTIVE"));
}