package com.milyfe.untitled.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/18/13
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "Message")
public class Message extends Model{

    public Message() {
        super();
    }

    public Message(Date createdDate,MessageType messageType) {
        super();
        key = new StringBuilder().append(createdDate.getTime()).append(messageType).toString();
        this.createdDate = createdDate;
        this.type = messageType;
    }

//    @Column(name = "key", unique = true,onUniqueConflict = Column.ConflictAction.REPLACE)
    public String key;
    @Column(name = "messageThreadId")
    public String messageThreadId;
    @Column(name = "fromString")
    public String from;
    @Column(name = "fromPicture")
    public String fromPicture;
    @Column(name = "toString")
    public String to;
    @Column(name = "cc")
    public String cc;
    @Column(name = "bodyString")
    public String body;
    @Column(name = "type")
    public MessageType type;
    @Column(name = "createdDate")
    public Date createdDate;
//    @Column(name = "parentThread")
    public MessageThread parentThread;



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

    public String getFromPicture() {
        return fromPicture;
    }

    public void setFromPicture(String fromPicture) {
        this.fromPicture = fromPicture;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
