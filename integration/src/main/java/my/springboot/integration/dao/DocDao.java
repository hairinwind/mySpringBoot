package my.springboot.integration.dao;

import my.springboot.integration.domain.Doc;

import java.util.List;

public interface DocDao {

    List<Doc> findAllDocs();
}
