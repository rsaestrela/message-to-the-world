package me.estrela.mttw;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@EnableJms
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        JooqAutoConfiguration.class
})
@SpringBootApplication
public class MessageToTheWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageToTheWorldApplication.class, args);
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                      DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper().findAndRegisterModules());
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public Settings jooqSettings() {
        return new Settings().withRenderNameStyle(RenderNameStyle.AS_IS);
    }

}
