package com.chatapp.auth.chatapp.controller;

import com.chatapp.auth.chatapp.service.MessageService;
import com.chatapp.auth.model.Group;
import com.chatapp.auth.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/auth/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Endpoint to get messages between two users
    @GetMapping("/user")
    public ResponseEntity<?> getMessagesBetweenUsers(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam boolean isGroup
    ) {
        try {
            // Define the range: from yesterday's start to the current time
            LocalDateTime startDateTime = LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();
            LocalDateTime endDateTime = LocalDateTime.now();

            System.out.println("Getting messages between user " + senderId + " and user " + receiverId +
                    " from " + startDateTime + " to " + endDateTime + ", isGroup: " + isGroup);

            if (isGroup) {
                // Fetch group messages
                List<Group> groupMessages = messageService.getGroupsMessages(receiverId, startDateTime, endDateTime);
                return ResponseEntity.ok(groupMessages);
            } else {
                // Fetch direct messages
                List<Message> userMessages = messageService.getMessagesBetweenUsers(senderId, receiverId, startDateTime, endDateTime);
                return ResponseEntity.ok(userMessages);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()); // Optional: Log the error for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching messages.");
        }
    }




}