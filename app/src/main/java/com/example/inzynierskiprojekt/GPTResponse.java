package com.example.inzynierskiprojekt;

public class GPTResponse {
    public static class Choice{
        public Message message;

        public static class Message{
            public String role;
            public String content;
        }
    }
}
