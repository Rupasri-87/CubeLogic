package com.example.detector;

import com.example.model.Side;
import com.example.model.Trade;
import com.example.model.Order;
import com.example.service.SuspiciousDetectorImplService;
import com.example.service.SuspiciousDetectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SuspiciousDetectorImplTest {
    private SuspiciousDetectorService detector;

    @BeforeEach
    void setUp() {
        detector = new SuspiciousDetectorImplService();
    }

    @Test
    void testNoSuspiciousTradesWhenNoOrders() {
        List<Trade> trades = List.of(
                new Trade(1, 100.0, 50.0, Side.BUY, LocalDateTime.now())
        );
        List<Order> orders = new ArrayList<>();

        List<Trade> result = detector.findSuspicious(trades, orders);

        assertEquals(0, result.size(), "No suspicious trades should be detected.");
    }

    @Test
    void testDetectSuspiciousTrade() {
        List<Trade> trades = List.of(
                new Trade(1, 100.0, 50.0, Side.BUY, LocalDateTime.now())
        );
        List<Order> orders = List.of(
                new Order(101, 95.0, 30.0, Side.SELL, LocalDateTime.now().minusMinutes(15))
        );

        List<Trade> result = detector.findSuspicious(trades, orders);

        assertEquals(1, result.size(), "One suspicious trade should be detected.");
        assertEquals(trades.get(0), result.get(0), "The detected trade should match the input trade.");
    }

    @Test
    void testNoSuspiciousTradesWhenPriceOutOfRange() {
        List<Trade> trades = List.of(
                new Trade(1, 100.0, 50.0, Side.BUY, LocalDateTime.now())
        );
        List<Order> orders = List.of(
                new Order(101, 120.0, 30.0, Side.SELL, LocalDateTime.now().minusMinutes(15))
        );

        List<Trade> result = detector.findSuspicious(trades, orders);

        assertEquals(0, result.size(), "No suspicious trades should be detected when price is out of range.");
    }

    @Test
    void testNoSuspiciousTradesWhenTimeWindowExceeded() {
        List<Trade> trades = List.of(
                new Trade(1, 100.0, 50.0, Side.BUY, LocalDateTime.now())
        );
        List<Order> orders = List.of(
                new Order(101, 95.0, 30.0, Side.SELL, LocalDateTime.now().minusMinutes(40))
        );

        List<Trade> result = detector.findSuspicious(trades, orders);

        assertEquals(0, result.size(), "No suspicious trades should be detected when time window is exceeded.");
    }

    @Test
    void testPriceExactlyAt10PercentThreshold() {
        List<Trade> trades = List.of(
                new Trade(1, 100.0, 50.0, Side.BUY, LocalDateTime.now())
        );
        List<Order> orders = List.of(
                new Order(101, 110.0, 20.0, Side.SELL, LocalDateTime.now().minusMinutes(15)) // Exactly 10% higher
        );

        List<Trade> result = detector.findSuspicious(trades, orders);

        assertEquals(1, result.size(), "Trade should be detected as suspicious when price is at 10% threshold.");
    }
    @Test
    void testTimeExactlyAt30MinuteThreshold() {
        List<Trade> trades = List.of(
                new Trade(1, 100.0, 50.0, Side.BUY, LocalDateTime.now())
        );
        List<Order> orders = List.of(
                new Order(101, 95.0, 30.0, Side.SELL, LocalDateTime.now().minusMinutes(30)) // Exactly 30 minutes before
        );

        List<Trade> result = detector.findSuspicious(trades, orders);

        assertEquals(1, result.size(), "Trade should be detected as suspicious when time is at 30-minute threshold.");
    }

    @Test
    void testNoOppositeSideOrders() {
        List<Trade> trades = List.of(
                new Trade(1, 100.0, 50.0, Side.BUY, LocalDateTime.now())
        );
        List<Order> orders = List.of(
                new Order(101, 95.0, 30.0, Side.BUY, LocalDateTime.now().minusMinutes(15)) // Same side as trade
        );

        List<Trade> result = detector.findSuspicious(trades, orders);

        assertEquals(0, result.size(), "No trades should be detected as suspicious when there are no opposite-side orders.");
    }

    @Test
    void testNoTrades() {
        List<Trade> trades = new ArrayList<>();
        List<Order> orders = List.of(
                new Order(101, 95.0, 30.0, Side.SELL, LocalDateTime.now().minusMinutes(15))
        );

        List<Trade> result = detector.findSuspicious(trades, orders);

        assertEquals(0, result.size(), "No suspicious trades should be detected when there are no trades.");
    }
    @Test
    void testNoOrders() {
        List<Trade> trades = List.of(
                new Trade(1, 100.0, 50.0, Side.BUY, LocalDateTime.now())
        );
        List<Order> orders = new ArrayList<>();

        List<Trade> result = detector.findSuspicious(trades, orders);

        assertEquals(0, result.size(), "No suspicious trades should be detected when there are no orders.");
    }
    @Test
    void testLargeVolumeOfTradesAndOrders() {
        List<Trade> trades = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            trades.add(new Trade(i, 100.0 + i, 50.0, Side.BUY, LocalDateTime.now().minusMinutes(i % 30)));
            orders.add(new Order(1000 + i, 95.0 + i, 30.0, Side.SELL, LocalDateTime.now().minusMinutes(i % 30)));
        }

        List<Trade> result = detector.findSuspicious(trades, orders);

        assertEquals(1000, result.size(), "All trades should be detected as suspicious in this synthetic test.");
    }

    @Test
    void testSameOrderMatchesMultipleTrades() {
        List<Trade> trades = List.of(
                new Trade(1, 100.0, 50.0, Side.BUY, LocalDateTime.now()),
                new Trade(2, 100.0, 20.0, Side.BUY, LocalDateTime.now().minusMinutes(5))
        );
        List<Order> orders = List.of(
                new Order(101, 95.0, 30.0, Side.SELL, LocalDateTime.now().minusMinutes(10))
        );

        List<Trade> result = detector.findSuspicious(trades, orders);

        assertEquals(2, result.size(), "One order should match multiple trades if conditions are met.");
    }
}
