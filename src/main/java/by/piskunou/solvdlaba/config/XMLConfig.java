package by.piskunou.solvdlaba.config;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

@Configuration
public class XMLConfig {

    @Bean
    public XML xml() throws IOException {
        return new XMLDocument(
                ResourceUtils.getURL("classpath:kafka/consumer.xml").openStream()
        );
    }

}
