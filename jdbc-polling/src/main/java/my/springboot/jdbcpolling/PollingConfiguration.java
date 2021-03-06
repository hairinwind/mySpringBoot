package my.springboot.jdbcpolling;

import my.springboot.jdbcpolling.domain.Test;
import my.springboot.jdbcpolling.rowmapper.TestRowMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class PollingConfiguration {

    @Value("${jdbcPolling.query}")
    private String jdbcPollingQuery;

    @Bean
    @InboundChannelAdapter(value = "jdbcPollingInboundFlow",
            poller = @Poller(fixedDelay = "${jdbcPolling.poller.fixed.delay}"),
            autoStartup = "${jdbcPolling.autoStartup}")
    public MessageSource<List<Test>> jdbcPollingJdbcPollingChannelAdapter(JdbcTemplate jdbcTemplate) {
//        JdbcPollingChannelAdapter jdbcPollingChannelAdapter = new JdbcPollingChannelAdapter
//                (dataSource, jdbcPollingQuery);
//        jdbcPollingChannelAdapter.setRowMapper(new TestRowMapper());
//        return jdbcPollingChannelAdapter;
        return () -> {
            // return a Message, then no need to do class cast in serviceActivator
            List<Test> testList = jdbcTemplate.query(jdbcPollingQuery, new TestRowMapper());
            return new GenericMessage(testList);
        };
    }

    @Bean(name = "jdbcPolling-inbound-channel")
    public MessageChannel jdbcPollingChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow jdbcPollingDbPollingFlow() {
        return IntegrationFlows.from("jdbcPollingInboundFlow")
                .channel(jdbcPollingChannel())
                .get();
    }
}
