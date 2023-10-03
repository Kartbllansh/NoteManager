package kartbllansh.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static kartbllansh.RabbitQueue.*;

@Configuration
public class RabbitConfiguration {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue textMessageQueue() {
        return new Queue(UPDATE_CODEX);
    }

    @Bean
    public Queue answerMessageQueue() {
        return new Queue(ANSWER_CODEX);
    }

    @Bean
    public Queue callBackUpdate(){
        return  new Queue(CALLBACK_UPDATE_CODEX);
    }

}

