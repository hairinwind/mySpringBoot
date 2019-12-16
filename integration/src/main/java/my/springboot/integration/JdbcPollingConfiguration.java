package my.springboot.integration;

import my.springboot.integration.dao.DocDao;
import my.springboot.integration.dao.DocDaoImpl;
import my.springboot.integration.domain.Doc;
import my.springboot.integration.rowmapper.DocRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@Configuration
public class JdbcPollingConfiguration {

    private static String query = "SELECT * FROM docs";

    @Autowired
    private DataSource dataSource;

    @Bean
    public DocDao docDao() {
        return new DocDaoImpl();
    }

    @Autowired
    DocDao docDao;

    @Bean
    public MessageChannel docJdbcInputChannel() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "docJdbcInputChannel", poller = @Poller(fixedDelay = "10000"))
    public MessageSource<List<Doc>> jdbcPollingChannelAdapter(DataSource dataSource) {
//        JdbcPollingChannelAdapter jdbcPollingChannelAdapter = new JdbcPollingChannelAdapter(dataSource, query);
//        jdbcPollingChannelAdapter.setRowMapper(new DocRowMapper());
//        return jdbcPollingChannelAdapter;

        // the commented code above is using a sql directly, it could be configured in properties file
        //the code below is using the dao method to do the query
        return () -> {
            // MessageSource is a functional interface
            // which only has one function to return Message<T>
            return MessageBuilder.withPayload(this.docDao.findAllDocs()).build();
        };
    }

    @ServiceActivator(inputChannel = "docJdbcInputChannel")
    public void handle(List<Doc> obj) throws IOException {
        List<Doc> docList = (List<Doc>) obj;
        System.out.println("... jdbc serviceActivator, find doc size: " + docList.size());
        docList.forEach(System.out::println);
        System.out.println("======\n");
    }
}
