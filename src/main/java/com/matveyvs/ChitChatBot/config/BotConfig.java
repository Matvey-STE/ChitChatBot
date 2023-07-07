package com.matveyvs.ChitChatBot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data // create constructor for class automatically
public class BotConfig{
    @Value("${bot.name}")
    String botName;
    @Value("${bot.key}")
    String botKey;
    @Value("${bot.admin.password}")
    String adminPassword;
}
