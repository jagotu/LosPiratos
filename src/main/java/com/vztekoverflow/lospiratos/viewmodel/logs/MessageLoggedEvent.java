package com.vztekoverflow.lospiratos.viewmodel.logs;

public class MessageLoggedEvent extends LoggedEvent {
    @Override
    public String getTextualDescription(LogFormatter f) {
        return sender + ": " + message;
    }

    private String sender;
    private String message;

    public MessageLoggedEvent(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
