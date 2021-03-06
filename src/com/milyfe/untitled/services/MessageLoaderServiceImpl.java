package com.milyfe.untitled.services;

import com.milyfe.untitled.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 9/21/13
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageLoaderServiceImpl implements MessageLoaderService {

    private FacebookService facebookService;
    private TwitterService twitterService;
    private List<MessageService> messageServices;

    public MessageLoaderServiceImpl(FacebookService facebookService,TwitterService twitterService) {
        this.facebookService = facebookService;
        this.twitterService = twitterService;
        messageServices = new ArrayList<MessageService>();
        messageServices.add(facebookService);
        messageServices.add(twitterService);
    }

    @Override
    public List<Message> loadMessagesFromAccessableServices() {
        List<Message> messages = new ArrayList<Message>();
        int days = 30;
        for(MessageService messageService : messageServices) {
           if(messageService.hasAuthentication())  {
               messages.addAll(messageService.getMessagesByDays(days));
           }
        }
        return messages;
    }
}
