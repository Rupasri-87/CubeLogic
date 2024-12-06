package com.example.model;

import java.time.LocalDateTime;

public class Transaction {
    public long id;
    public double price;
    public double volume;
    public Side side;
    public LocalDateTime timestamp;

    public Transaction(long id, double price, double volume, Side side, LocalDateTime timestamp) {
        this.id = id;
        this.price = price;
        this.volume = volume;
        this.side = side;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", price=" + price +
                ", volume=" + volume +
                ", side=" + side +
                ", timestamp=" + timestamp +
                '}';
    }
}
