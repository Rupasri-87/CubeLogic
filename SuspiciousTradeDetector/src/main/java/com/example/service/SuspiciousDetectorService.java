package com.example.service;

import com.example.model.Order;
import com.example.model.Trade;

import java.util.List;

public interface SuspiciousDetectorService {
    List<Trade> findSuspicious(List<Trade> trades, List<Order> orders);
}
