package com.chatapp.auth.chatapp.service;

import com.chatapp.auth.chatapp.DTO.MessageDTO;
import com.chatapp.auth.model.Group;
import com.chatapp.auth.model.Message;
import com.chatapp.auth.model.User;
import com.chatapp.auth.repository.GroupRepository;
import com.chatapp.auth.repository.MessageRepository;
import com.chatapp.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    public MessageService(MessageRepository messageRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Message savePrivateMessage(MessageDTO messageDTO) {
        // Validate MessageDTO before processing
        validateMessageDTO(messageDTO);

        // Log message details for tracking
        logger.info("Received message from user {} to user {} with content: {}", messageDTO.getSenderId(), messageDTO.getReceiverId(), messageDTO.getContent());
        Message savedMessage = null;

        try {
            // Retrieve sender and receiver by their IDs
            User sender = findUserById(messageDTO.getSenderId(), "Sender");
            User receiver = findUserById(messageDTO.getReceiverId(), "Receiver");

            // Create a new Message and set necessary fields
            Message message = new Message();
            message.setContent(messageDTO.getContent());
            message.setTimestamp(LocalDateTime.now());
            message.setSenderId(messageDTO.getSenderId());
            message.setReceiverId(messageDTO.getReceiverId());

            // Save message to the repository
            savedMessage = messageRepository.save(message);

            // Log the successful message save
            logger.info("Message successfully saved with ID: {}", savedMessage.getId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to save message");
        }

        return savedMessage;
    }

    private void validateMessageDTO(MessageDTO messageDTO) {
        // Ensure that the messageDTO is not null and contains the necessary fields
        if (messageDTO == null || messageDTO.getContent() == null || messageDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be null or empty.");
        }

        // Ensure that both sender and receiver IDs are present
        if (messageDTO.getSenderId() == null || messageDTO.getReceiverId() == null) {
            throw new IllegalArgumentException("Both sender and receiver IDs must be provided.");
        }
    }

    private User findUserById(Long userId, String userType) {
        // Find user by ID with improved error handling
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException(userType + " user not found with ID: " + userId);
        }
        return userOptional.get();
    }

    public List<Message> getMessagesBetweenUsers(Long senderId, Long receiverId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return messageRepository.findMessagesBetweenUsers(senderId, receiverId, startDateTime, endDateTime);
    }

    public List<Group> getGroupsMessages(Long groupId, LocalDateTime startDateTime, LocalDateTime endDateTime){
        return groupRepository.findByGroupIdIdAndTimestampBetween(groupId, startDateTime, endDateTime);
    }

}
