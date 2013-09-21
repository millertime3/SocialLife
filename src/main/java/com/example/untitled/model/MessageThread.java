package com.example.untitled.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/18/13
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageThread {
    private String id;
    private String smaId;
    private List<String> messageIds;
    private SocialMediaAPI parentSocialMediaAPI;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<String> messageIds) {
        this.messageIds = messageIds;
    }

    public SocialMediaAPI getParentSocialMediaAPI() {
        return parentSocialMediaAPI;
    }

    public void setParentSocialMediaAPI(SocialMediaAPI parentSocialMediaAPI) {
        this.parentSocialMediaAPI = parentSocialMediaAPI;
    }

    public String getSmaId() {
        return smaId;
    }

    public void setSmaId(String smaId) {
        this.smaId = smaId;
    }
}
