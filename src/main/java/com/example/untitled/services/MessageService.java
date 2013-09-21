package com.example.untitled.services;

import com.example.untitled.model.Message;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 9/21/13
 * Time: 1:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MessageService extends RemoteService {
    List<Message> getMessagesByDays(int days);
}
