package com.milyfe.untitled.utils;

import com.milyfe.untitled.model.Message;
import com.milyfe.untitled.model.MessageType;
import twitter4j.Status;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/20/13
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class TwitterUtils {

    public static Message getMessageFromStatus(Status status) {
        Message message = new Message(status.getCreatedAt(),MessageType.TWEET);
        message.setBody(status.getText());
        message.setFrom(status.getUser().getScreenName());
        message.setFromPicture(status.getUser().getBiggerProfileImageURL());
        return message;
    }
}
