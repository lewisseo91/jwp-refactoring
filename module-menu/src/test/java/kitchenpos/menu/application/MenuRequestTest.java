package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.CustomLocalValidatorFactoryBean;
import kitchenpos.menu.validate.MenuGroupMustExistValidator;
import kitchenpos.menugroup.domain.MenuGroupRepository;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuRequestTest {

    private MenuGroupRepository menuGroupRepository = mock(MenuGroupRepository.class);
    private final List<ConstraintValidator<?,?>> customConstraintValidators =
            Collections.singletonList(new MenuGroupMustExistValidator(menuGroupRepository));
    private ValidatorFactory customValidatorFactory;
    private Validator validator;

    @BeforeEach
    void setUp() {
        customValidatorFactory =  new CustomLocalValidatorFactoryBean(customConstraintValidators);
        validator = customValidatorFactory.getValidator();
    }


    @DisplayName("dto 유효값 테스트")
    @Test
    void dtoValidateTest() {
        when(menuGroupRepository.existsById(any())).thenReturn(false);
        MenuRequest menuRequest = new MenuRequest("테스트", null, 1L, Lists.emptyList());
        final Collection<ConstraintViolation<MenuRequest>> constraintViolations = validator.validate(menuRequest);
        assertEquals("가격은 빈 값이 들어올 수 없습니다.", constraintViolations.iterator().next().getMessage());
//        menuService.create(menuRequest);
    }
}
