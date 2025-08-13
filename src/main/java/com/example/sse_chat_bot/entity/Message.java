package com.example.sse_chat_bot.entity;

import com.example.sse_chat_bot.utils.SenderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String Content;
    @Column(nullable = false)
    private SenderType senderType;
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private OffsetDateTime updatedAt;
}
