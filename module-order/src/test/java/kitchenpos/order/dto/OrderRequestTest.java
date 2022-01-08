package kitchenpos.order.dto;

import kitchenpos.common.CustomLocalValidatorFactoryBean;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.validate.MenuGroupMustExistValidator;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.dto.validate.OrderTableMustExistValidator;
import kitchenpos.ordertable.domain.OrderTableRepository;
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
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRequestTest {
    private OrderTableRepository orderTableRepository = mock(OrderTableRepository.class);
    private List<ConstraintValidator<?,?>> customConstraintValidators;
    private ValidatorFactory customValidatorFactory;
    private Validator validator;

    @BeforeEach
    void setUp() {
        customConstraintValidators = Lists.newArrayList(new OrderTableMustExistValidator(orderTableRepository));
        customValidatorFactory = new CustomLocalValidatorFactoryBean(customConstraintValidators);
        validator = customValidatorFactory.getValidator();
    }

    @DisplayName("orderTableMustExistValidator 테스트")
    @Test
    void orderTableMustExistValidateTest() {
        when(orderTableRepository.existsById(any())).thenReturn(false);
        OrderRequest orderRequest = new OrderRequest(1L, Lists.newArrayList());
        final Collection<ConstraintViolation<OrderRequest>> constraintViolations = validator.validate(orderRequest);
        assertEquals("주문 테이블이 반드시 존재해야 합니다.", constraintViolations.iterator().next().getMessage());
    }
}