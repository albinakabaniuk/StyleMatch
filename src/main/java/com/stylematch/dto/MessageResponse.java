package com.stylematch.dto;

public class MessageResponse {
    private String message;

    public MessageResponse() {}
    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static MessageResponseBuilder builder() {
        return new MessageResponseBuilder();
    }

    public static class MessageResponseBuilder {
        private String message;
        public MessageResponseBuilder message(String message) { this.message = message; return this; }
        public MessageResponse build() { return new MessageResponse(message); }
    }
}
