package kitchenpos.product.dto;

import kitchenpos.common.CustomLocalValidatorFactoryBean;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
@ExtendWith(MockitoExtension.class)
class ProductRequestTest {

    private List<ConstraintValidator<?,?>> customConstraintValidators;
    private ValidatorFactory customValidatorFactory;
    private Validator validator;

    @BeforeEach
    void setUp() {
        customConstraintValidators = Lists.newArrayList();
        customValidatorFactory = new CustomLocalValidatorFactoryBean(customConstraintValidators);
        validator = customValidatorFactory.getValidator();
    }

    @DisplayName("price not null 테스트")
    @Test
    void priceNotnullValidateTest() {
        ProductRequest productRequest = new ProductRequest(3L, "말도안되는상품", null);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertEquals("가격은 빈 값이 들어올 수 없습니다.", violations.iterator().next().getMessage());
    }

}