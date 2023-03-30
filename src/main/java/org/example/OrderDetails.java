package org.example;

import org.example.data.BillInfo;
import org.example.data.Delivery;

public record OrderDetails(long orderId, Delivery delivery, BillInfo billInfo) {
}
