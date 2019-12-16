package my.springboot.integration.dao;

import my.springboot.integration.domain.Doc;
import my.springboot.integration.rowmapper.DocRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class DocDaoImpl implements DocDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Doc> findAllDocs() {
        String sql = "SELECT id, rev, content FROM DOCS";
        return jdbcTemplate.query(sql, new DocRowMapper());
    }
}
