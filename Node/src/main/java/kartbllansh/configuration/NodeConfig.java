package kartbllansh.configuration;

import kartbllansh.ButtonForKeyboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NodeConfig {
    @Bean
    public ButtonForKeyboard buttonForKeyboard() {
        return new ButtonForKeyboard("Button Text", "Button Callback Data");
    }
}
