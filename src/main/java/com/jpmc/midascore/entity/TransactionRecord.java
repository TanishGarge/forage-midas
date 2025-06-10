package com.jpmc.midascore.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId", nullable = false)
    private UserRecord sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipientId", nullable = false)
    private UserRecord recipient;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private LocalDateTime timeStamp;

    public  TransactionRecord() {}

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.timeStamp = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public float getAmount() {
        return amount;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    public UserRecord getSender() {
        return sender;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setRecipient(UserRecord recipient) {
        this.recipient = recipient;
    }

    public void setSender(UserRecord sender) {
        this.sender = sender;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}

