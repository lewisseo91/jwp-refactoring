package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "order_line_item")
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Long menuId;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Order order, MenuId menuId, long quantity) {
        this.order = order;
        this.menuId = menuId.getMenuId();
        this.quantity = quantity;
    }

    public OrderLineItem(Long seq, Order order, MenuId menuId, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId.getMenuId();
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void decideOrder(Order order) {
        this.order = order;
    }
}
