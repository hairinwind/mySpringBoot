package my.springboot.integration;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.RegexPatternFileListFilter;
import org.springframework.messaging.MessageChannel;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Configuration
public class FilePollingConfiguration {
    @Value("${file.inboundPath}")
    private Resource inboundPath;

    @Value("${file.outboundPath}")
    private Resource outboundPath;

    @Bean
    public MessageChannel fileInputChannel() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "1000"))
    public MessageSource<File> fileReadingMessageSource() throws IOException {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File("C:\\myWorkspace\\mySpringBoot\\integration\\file\\incoming"));
        CompositeFileListFilter<File> filter = new CompositeFileListFilter<>(
                Arrays.asList(new AcceptOnceFileListFilter<>(),
                        new RegexPatternFileListFilter(".*"))
        );
        source.setFilter(filter);
        return source;
    }

//    @Bean
//    @Transformer(inputChannel = "fileInputChannel", outputChannel = "processFileChannel")
//    public FileToStringTransformer fileToStringTransformer() {
//        return new FileToStringTransformer();
//    }

    @ServiceActivator(inputChannel = "fileInputChannel")
    public void handle(Object obj) throws IOException {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(outboundPath.getFile());
        System.out.println(obj);
//        handler.setFileExistsMode(FileExistsMode.REPLACE);
//        return handler;
    }
}
