package com.apica.UserService.constant;

public enum Topic {
    USER_EVENTS("user-events");
    private final String topic;

    Topic(String s) {
        this.topic = s;
    }
    public String getTopic() {
        return topic;
    }
}
