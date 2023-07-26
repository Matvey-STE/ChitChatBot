package com.matveyvs.chitchatbot.appconfig;

import com.matveyvs.chitchatbot.service.WebHookBotService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig{
    private String botName;
    private String webHookPath;
    private String botToken;
    @Bean
    public WebHookBotService webHookBotConfig() {
        WebHookBotService webHookBotConfig = new WebHookBotService();
        webHookBotConfig.setBotName(botName);
        webHookBotConfig.setWebHookPath(webHookPath);
        webHookBotConfig.setBotToken(botToken);
        return webHookBotConfig;
    }
    //message source get info from messages.properties based on language
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
