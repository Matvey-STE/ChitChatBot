package com.matveyvs.ChitChatBot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
@Configuration
public class BotConfig{
    @Value("${bot.name}")
    String botName;
    @Value("${bot.key}")
    String botKey;
}
