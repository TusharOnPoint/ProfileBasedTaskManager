package com.tushar.taskmanagerapp.service;

public interface EmailService {
    public void send(String to, String subject, String body);
}
