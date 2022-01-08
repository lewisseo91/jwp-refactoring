package kitchenpos;

import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DtoTest {

    @Autowired
    private Validator validator;


    @DisplayName("dto 유효값 테스트")
    @Test
    void dtoValidateTest() {
        MenuRequest menuRequest = new MenuRequest("테스트", null, 1L, Lists.emptyList());
        final Collection<ConstraintViolation<MenuRequest>> constraintViolations = validator.validate(menuRequest);
        assertEquals("가격은 빈 값이 들어올 수 없습니다.", constraintViolations.iterator().next().getMessage());
//        menuService.create(menuRequest);
    }
}
