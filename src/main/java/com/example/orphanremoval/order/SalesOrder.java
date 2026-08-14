package com.example.orphanremoval.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales_orders")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL)
    private List<SalesOrderLine> lines = new ArrayList<>();

    protected SalesOrder() {
    }

    public SalesOrder(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public List<SalesOrderLine> getLines() {
        return lines;
    }

    public void addLine(String productCode) {
        SalesOrderLine line = new SalesOrderLine(productCode);
        line.assignTo(this);
        lines.add(line);
    }

    public void removeLine(String productCode) {
        SalesOrderLine line = lines.stream()
            .filter(candidate -> candidate.getProductCode().equals(productCode))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("line not found: " + productCode));
        lines.remove(line);
        line.assignTo(null);
    }
}
