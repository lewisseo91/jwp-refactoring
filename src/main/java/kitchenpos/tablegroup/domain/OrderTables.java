package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void ungroup() {
        for (OrderTable orderTable: orderTables) {
            orderTable.ungroupTableGroup();
        }

        orderTables = new ArrayList<>();
    }
}