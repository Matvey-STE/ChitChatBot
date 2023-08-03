package com.matveyvs.chitchatbot.service.testrabbitmq.controller;

import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.WebHookBotService;
import com.matveyvs.chitchatbot.service.testrabbitmq.rabbimqreciever.UpdateProducer;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.matveyvs.chitchatbot.service.testrabbitmq.RabbitQueue.*;

@Component
@Log4j2
public class RabbitMQController {
    private final ReplyMessageService replyMessageService;
    private final WebHookBotService webHookBotService;
    private final UpdateProducer updateProducer;


    public RabbitMQController(ReplyMessageService replyMessageService, WebHookBotService webHookBotService, UpdateProducer updateProducer) {
        this.replyMessageService = replyMessageService;
        this.webHookBotService = webHookBotService;
        this.updateProducer = updateProducer;
    }


    public void processUpdate (Update update){
        if (update == null){
            log.error("Received update is null");
        } else {
            if (update.hasMessage()){
                distributeMessageByType(update);
            } else if (update.hasCallbackQuery()){
                distributeCallbackQuery(update);
            } else {
                log.error("Received unsupported update type " + update);
            }
        }
    }

    private void distributeCallbackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery != null){
            processCallbackQuery(update);
        } else {
            log.error("Received unsupported CALLBACK_QUERY_TYPE" + update);
        }
    }

    private void processCallbackQuery(Update update) {
        updateProducer.produce(CALLBACK_QUERY_MESSAGE, update);
    }

    private void distributeMessageByType(Update update) {
        Message updateMessage = update.getMessage();
        if (updateMessage.hasText()){
            processTextMessage(update);
        }
        else {
            setUnsupportedMessageTypeView(update);
        }
    }
    private void setUnsupportedMessageTypeView(Update update) {
        Long chatId = update.getMessage().getChatId();
        var sendMessage = replyMessageService.getReplyMessage(chatId, "Unsupported type of message");
        setView(sendMessage);
    }
    public void setView(SendMessage sendMessage) {
        webHookBotService.sendAnswerMessage(sendMessage);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);

    }
}
