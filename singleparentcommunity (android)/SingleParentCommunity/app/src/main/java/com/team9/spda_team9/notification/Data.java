package com.team9.spda_team9.notification;

import com.team9.spda_team9.forum.Topic;

public class Data {
    private String Title;
    private String Message;
    private String TopicId;

    public Data(String title, String message, String topicId) {
        Title = title;
        Message = message;
        TopicId = topicId;
    }

    public Data() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTopicId() {
        return TopicId;
    }

    public void setTopicId(String topicId) {
        TopicId = topicId;
    }
}
