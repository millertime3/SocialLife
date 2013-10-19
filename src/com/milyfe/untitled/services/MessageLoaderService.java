package com.milyfe.untitled.services;

import com.milyfe.untitled.model.Message;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 9/21/13
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MessageLoaderService {
    List<Message> loadMessagesFromAccessableServices();
}
