package com.example.orphanremoval;

import com.example.orphanremoval.order.SalesOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class SalesOrderServiceTest {

    @Autowired
    private SalesOrderService salesOrderService;

    private Long orderId;

    @BeforeEach
    void setUp() {
        orderId = salesOrderService.createOrder();
    }

    @Test
    void 親コレクションから外した注文明細はDBから削除される() {
        salesOrderService.removeLine(orderId, "PEN");

        int currentOrderLineCount = salesOrderService.currentOrderLineCount(orderId);
        long totalLineCount = salesOrderService.totalLineCount();

        assertAll(
            () -> assertThat(currentOrderLineCount).isEqualTo(1),
            () -> assertThat(totalLineCount).isEqualTo(1)
        );
    }
}
