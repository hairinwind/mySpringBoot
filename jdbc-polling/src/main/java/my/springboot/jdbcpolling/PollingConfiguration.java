package my.springboot.jdbcpolling;

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
import org.springframework.messaging.MessageChannel;

import javax.sql.DataSource;

@Configuration
public class PollingConfiguration {

    @Value("${jdbcPolling.query}")
    private String jdbcPollingQuery;

    @Bean
    @InboundChannelAdapter(value = "jdbcPollingInboundFlow",
            poller = @Poller(fixedDelay = "${jdbcPolling.poller.fixed.delay}"),
            autoStartup = "${jdbcPolling.autoStartup}")
    public MessageSource<Object> jdbcPollingJdbcPollingChannelAdapter(DataSource dataSource) {
        JdbcPollingChannelAdapter jdbcPollingChannelAdapter = new JdbcPollingChannelAdapter
                (dataSource, jdbcPollingQuery);
        jdbcPollingChannelAdapter.setRowMapper(new TestRowMapper());
        return jdbcPollingChannelAdapter;
    }

    @Bean(name = "jdbcPolling-inbound-channel")
    public MessageChannel jdbcPollingChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public MessageChannel jdbcPollingInboundFlow() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow jdbcPollingDbPollingFlow() {
        return IntegrationFlows.from("jdbcPollingInboundFlow")
                .channel(jdbcPollingChannel())
                .get();
    }
}
