package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void handleTransaction(Transaction transaction) {
        logger.info("=== RECEIVED TRANSACTION ===");
        logger.info("Transaction: {}", transaction);
        logger.info("Amount: {}", transaction.getAmount());
        logger.info("===========================");

        // Validating Transaction
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        // Handling Null Check
        if(sender == null) {
            logger.warn("Invalid senderId: {} - Transaction Discarded.", transaction.getSenderId());
            return;
        }

        if(recipient == null) {
            logger.warn("Invalid recipientId: {} - Transaction Discarded.", transaction.getRecipientId());
            return;
        }

        if(sender.getBalance() < transaction.getAmount()) {
            logger.warn("Insufficient balance for sender {}, Current Balance: {}, Attempted: {} - Transaction Discarded.", sender.getName(), sender.getBalance(), transaction.getAmount());
            return;
        }

        // Process Transaction
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());

        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord transactionRecord = new TransactionRecord(
                sender, recipient, transaction.getAmount()
        );
        transactionRepository.save(transactionRecord);
        logger.info("Transaction processed successfully. Sender: {} (New Balance: {}), Recipient: {} (New Balance: {})", sender.getName(), sender.getBalance(), recipient.getName(), recipient.getBalance());
    }
}
