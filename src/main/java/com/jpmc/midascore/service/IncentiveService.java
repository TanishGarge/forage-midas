package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveService {
    private static final Logger logger = LoggerFactory.getLogger(IncentiveService.class);

    @Autowired
    private RestTemplate restTemplate;

    private static final String INCENTIVE_API_URL = "http://localhost:8080/incentive";

    public Incentive getIncentive(Transaction transaction) {
        try {
            // Spring will automatically serialize Transaction to JSON and deserialize response to Incentive
            Incentive incentive = restTemplate.postForObject(INCENTIVE_API_URL, transaction, Incentive.class);
            logger.info("Received incentive amount: {} for transaction from senderId {} to recipientId {}",
                    incentive.getAmount(), transaction.getSenderId(), transaction.getRecipientId());
            return incentive;
        } catch (Exception e) {
            logger.error("Error calling incentive API: ", e);
            // Return zero incentive on error
            return new Incentive(0.0f);
        }
    }
}
