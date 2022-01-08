package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductRequest {
    private Long id;
    private String name;

    @NotNull(message = "가격은 빈 값이 들어올 수 없습니다.")
    private BigDecimal price;

    public ProductRequest() {
    }

    public ProductRequest(Long id, String name, int price) {
        this(id, name, new BigDecimal(price));
    }

    public ProductRequest(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toProduct() {
        return new Product(name, price);
    }
}
