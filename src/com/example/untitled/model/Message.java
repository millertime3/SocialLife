package com.example.untitled.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/18/13
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Message {
    private String id;
    private String messageThreadId;
    private String from;
    private String to;
    private String cc;
    private String body;
    private MessageType type;
    private Date createdDate;
    private MessageThread parentThread;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageThreadId() {
        return messageThreadId;
    }

    public void setMessageThreadId(String messageThreadId) {
        this.messageThreadId = messageThreadId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessageThread getParentThread() {
        return parentThread;
    }

    public void setParentThread(MessageThread parentThread) {
        this.parentThread = parentThread;
    }
}
