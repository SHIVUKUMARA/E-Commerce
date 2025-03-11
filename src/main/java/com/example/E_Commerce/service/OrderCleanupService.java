package com.example.E_Commerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.E_Commerce.entity.Order;
import com.example.E_Commerce.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCleanupService {
    private final OrderRepository orderRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    @Transactional
    public void cancelExpiredOrders() {
        LocalDateTime now = LocalDateTime.now();
        List<Order> expiredOrders = orderRepository.findByStatusAndOtpExpiryBefore("PROCESSING", now);

        if (!expiredOrders.isEmpty()) {
            for (Order order : expiredOrders) {
                order.setStatus("CANCELED");
                order.setOtp(null);
                order.setOtpExpiry(null);
            }
            orderRepository.saveAll(expiredOrders);
            log.info("Canceled {} expired orders", expiredOrders.size());
        }
    }
}
