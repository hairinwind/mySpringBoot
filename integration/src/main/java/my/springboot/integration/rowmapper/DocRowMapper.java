package my.springboot.integration.rowmapper;

import my.springboot.integration.domain.Doc;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DocRowMapper implements RowMapper<Doc> {

    @Override
    public Doc mapRow(ResultSet resultSet, int i) throws SQLException {
        if(resultSet != null) {
            Doc doc = new Doc();
            doc.setId(resultSet.getInt("id"));
            doc.setRev(resultSet.getInt("rev"));
            doc.setContent(resultSet.getString("content"));
            return doc;
        }
        return null;
    }
}

