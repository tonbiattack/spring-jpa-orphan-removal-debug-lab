package com.example.orphanremoval.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sales_order_lines")
public class SalesOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productCode;

    @ManyToOne
    private SalesOrder salesOrder;

    protected SalesOrderLine() {
    }

    public SalesOrderLine(String productCode) {
        this.productCode = productCode;
    }

    public Long getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    void assignTo(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }
}
