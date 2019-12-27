package my.springboot.jdbcpolling.rowmapper;

import my.springboot.jdbcpolling.domain.Test;
import org.h2.mvstore.tx.Transaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestRowMapper implements RowMapper<Test> {

    @Override
    public Test mapRow(ResultSet resultSet, int i) throws SQLException {
        Test test = new Test();
        test.setId(resultSet.getInt("id"));
        return test;
    }
}
