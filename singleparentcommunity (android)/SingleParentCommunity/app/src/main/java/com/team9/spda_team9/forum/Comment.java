package com.team9.spda_team9.forum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Comment implements Serializable {

    private String commentId;
    private String dateTime;
    private String userId;
    private String username;
    private String body;
    private String topicId; //keeps track of which topic the comments belong to
    private String replyTo; //commentId or TopicId, keeps track of which comment/topic it is replying to

    public Comment(){}

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBody() {
        return body;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
}
