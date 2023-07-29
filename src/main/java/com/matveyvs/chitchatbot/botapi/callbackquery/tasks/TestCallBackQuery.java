package com.matveyvs.chitchatbot.botapi.callbackquery.tasks;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.botapi.handlers.TestCommandHandler;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.taskservices.BestDefinitionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
@Component
public class TestCallBackQuery implements CallbackQueryHandler {
    private final BestDefinitionService bestDefinitionService;
    private final TestCommandHandler testCommandHandler;
    private final ReplyMessageService replyMessageService;

    public TestCallBackQuery(BestDefinitionService bestDefinitionService, TestCommandHandler testCommandHandler, ReplyMessageService replyMessageService) {
        this.bestDefinitionService = bestDefinitionService;
        this.testCommandHandler = testCommandHandler;
        this.replyMessageService = replyMessageService;
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {

        Integer rightButton = bestDefinitionService.getIndexOfRightAnswer(1);
        System.out.println(rightButton);

        SendMessage reply;
        Message message = callbackQuery.getMessage();

        Integer callBackMessageId = message.getMessageId();
        Long chatId = message.getChatId();
        String callbackData = callbackQuery.getData();

        if (callbackData.contains(String.valueOf(rightButton))){
            reply = replyMessageService.getAndSendReplyMessage(chatId, "callback.reply.success.message");
        } else {
            reply = replyMessageService.getAndSendReplyMessage(chatId, "callback.reply.wrong.message");
        }
        replyMessageService.deleteMessage(chatId,callBackMessageId);

        return reply;
    }

    @Override
    public List<String> getHandlerQueryType() {
        return testCommandHandler.getCallBackQueriesList();
    }
}
