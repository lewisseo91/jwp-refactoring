package kitchenpos.order.dto.validate;

import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component // mandatory!!! (or one of its descendants) to enable `@Autowired`
@Scope("request") // not mandatory, but probably "good" for "validation"
public class OrderTableMustExistValidator implements ConstraintValidator<OrderTableMustExist, Long> {
    @Autowired
    private OrderTableRepository orderTableRepository;

    public OrderTableMustExistValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {

        return orderTableRepository.existsById(value);
    }
}
