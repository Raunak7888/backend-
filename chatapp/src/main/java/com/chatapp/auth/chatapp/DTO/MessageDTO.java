package com.chatapp.auth.chatapp.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Content cannot be null")
    private String content;

    private LocalDateTime timestamp;

    @NotNull(message = "Sender ID cannot be null")
    private Long senderId;


    private Long receiverId;  // For one-to-one messages

    private Long tempId;

}
