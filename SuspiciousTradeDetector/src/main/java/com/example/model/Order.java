package com.example.model;

import java.time.LocalDateTime;

public class Order extends Transaction {
    public Order(long id, double price, double volume, Side side, LocalDateTime timestamp) {
        super(id, price, volume, side, timestamp);
    }
}
