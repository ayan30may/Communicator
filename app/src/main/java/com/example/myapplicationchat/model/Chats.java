package com.example.myapplicationchat.model;

public class Chats {
    private String dateTime;
    private String textMessage;
    private String type;
    private String sender;
    private String receiver;
    private int feeling;
    private String messageId;
    private String imageUrl;



    public Chats() {
    }

    public Chats(String dateTime, String textMessage,String imageUrl, String type, String sender, String receiver, String messageId) {
        this.dateTime = dateTime;
        this.textMessage = textMessage;
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.messageId = messageId;
        this.imageUrl = imageUrl;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
