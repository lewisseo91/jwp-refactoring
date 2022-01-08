package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.common.CustomLocalValidatorFactoryBean;
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
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuRequestTest {

    private MenuGroupRepository menuGroupRepository = mock(MenuGroupRepository.class);
    private List<ConstraintValidator<?,?>> customConstraintValidators;
    private ValidatorFactory customValidatorFactory;
    private Validator validator;

    @BeforeEach
    void setUp() {
        customConstraintValidators = Lists.newArrayList(new MenuGroupMustExistValidator(menuGroupRepository));
        customValidatorFactory = new CustomLocalValidatorFactoryBean(customConstraintValidators);
        validator = customValidatorFactory.getValidator();
    }

    @DisplayName("price not null 테스트")
    @Test
    void priceNotnullValidateTest() {
        when(menuGroupRepository.existsById(any())).thenReturn(true);
        MenuRequest menuRequest = new MenuRequest("테스트", null, 1L, Lists.emptyList());
        Set<ConstraintViolation<MenuRequest>> violations = validator.validate(menuRequest);
        assertEquals("가격은 빈 값이 들어올 수 없습니다.", violations.iterator().next().getMessage());
    }


    @DisplayName("menuGroupMustExistValidate 테스트")
    @Test
    void menuGroupMustExistValidateTest() {
        when(menuGroupRepository.existsById(any())).thenReturn(false);
        MenuRequest menuRequest = new MenuRequest("테스트", 1000, 1L, Lists.emptyList());
        final Collection<ConstraintViolation<MenuRequest>> constraintViolations = validator.validate(menuRequest);
        assertEquals("메뉴 그룹이 반드시 존재해야 합니다.", constraintViolations.iterator().next().getMessage());
    }
}
