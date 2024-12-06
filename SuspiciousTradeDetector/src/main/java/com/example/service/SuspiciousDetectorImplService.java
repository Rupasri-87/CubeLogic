package com.example.service;

import com.example.model.Order;
import com.example.model.Side;
import com.example.model.Trade;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class SuspiciousDetectorImplService implements SuspiciousDetectorService{

    @Override
    public List<Trade> findSuspicious(List<Trade> trades, List<Order> orders) {
        List<Trade> suspiciousTrades = new ArrayList<>();

        for (Trade trade : trades) {
            boolean isSuspicious = orders.stream()
                    .anyMatch(order ->
                            isOppositeSide(order, trade) &&
                                    isWithinTimeWindow(order, trade, 30) &&
                                    isPriceSuspicious(order, trade));

            if (isSuspicious) {
                suspiciousTrades.add(trade);
            }
        }

        return suspiciousTrades;
    }

    private boolean isOppositeSide(Order order, Trade trade) {
        return (trade.side == Side.BUY && order.side == Side.SELL) ||
                (trade.side == Side.SELL && order.side == Side.BUY);
    }

    private boolean isWithinTimeWindow(Order order, Trade trade, int minutes) {
        long timeDifference = ChronoUnit.MINUTES.between(order.timestamp, trade.timestamp);
        return timeDifference >= 0 && timeDifference <= minutes;
    }

    private boolean isPriceSuspicious(Order order, Trade trade) {
        double priceThreshold = trade.price * 0.1;
        if (trade.side == Side.BUY) {
            return order.price <= trade.price + priceThreshold;
        } else { // trade.side == Side.SELL
            return order.price >= trade.price - priceThreshold;
        }
    }
}
