package com.example.model;

import java.time.LocalDateTime;

public class Trade extends Transaction {
    public Trade(long id, double price, double volume, Side side, LocalDateTime timestamp) {
        super(id, price, volume, side, timestamp);
    }
}
