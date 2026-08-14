package com.example.orphanremoval.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalesOrderService {

    private static final Logger logger = LoggerFactory.getLogger(SalesOrderService.class);

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderLineRepository salesOrderLineRepository;

    public SalesOrderService(
        SalesOrderRepository salesOrderRepository,
        SalesOrderLineRepository salesOrderLineRepository
    ) {
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderLineRepository = salesOrderLineRepository;
    }

    @Transactional
    public Long createOrder() {
        SalesOrder salesOrder = new SalesOrder("SO-001");
        salesOrder.addLine("PEN");
        salesOrder.addLine("NOTE");
        SalesOrder savedOrder = salesOrderRepository.saveAndFlush(salesOrder);
        logger.info("Created orderId={}, lineCount={}", savedOrder.getId(), savedOrder.getLines().size());
        return savedOrder.getId();
    }

    @Transactional
    public void removeLine(Long orderId, String productCode) {
        SalesOrder salesOrder = salesOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("order not found: " + orderId));
        salesOrder.removeLine(productCode);
        salesOrderRepository.flush();
        logger.info("Removed line from orderId={}, productCode={}", orderId, productCode);
    }

    @Transactional(readOnly = true)
    public int currentOrderLineCount(Long orderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("order not found: " + orderId));
        return salesOrder.getLines().size();
    }

    @Transactional(readOnly = true)
    public long totalLineCount() {
        return salesOrderLineRepository.count();
    }
}
