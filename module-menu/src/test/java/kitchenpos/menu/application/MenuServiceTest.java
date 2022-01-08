package kitchenpos.menu.application;

import kitchenpos.common.exception.NegativePriceException;
import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuValidator menuValidator;

    private MenuService menuService;

    private Menu 짜장면;
    private MenuRequest 짜장면_요청;
    private MenuProduct 짜장면_하나;
    private MenuProduct 짜장면_두개;
    private Product 짜장면_상품;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, menuValidator);

        짜장면_상품 = new Product("짜장면", new BigDecimal(1000));
        짜장면_하나 = new MenuProduct(1L, new Menu(), 짜장면_상품.getId(), 1);
        짜장면_두개 = new MenuProduct(2L, new Menu(), 짜장면_상품.getId(), 2);
        짜장면 = new Menu("짜장면", 10000, new MenuGroup(), Lists.newArrayList(짜장면_하나, 짜장면_두개));
        짜장면_요청 = new MenuRequest("짜장면", 10000, 1L, Lists.newArrayList(짜장면_하나, 짜장면_두개));
    }

    @DisplayName("dto 유효값 테스트")
    @Test
    void dtoValidateTest() {
        when(menuGroupRepository.existsById(any())).thenReturn(true);

        MenuRequest menuRequest = new MenuRequest("테스트", null, 1L, Lists.emptyList());
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Collection<ConstraintViolation<MenuRequest>> constraintViolations = validator.validate(menuRequest);
        assertEquals("가격은 빈 값이 들어올 수 없습니다.", constraintViolations.iterator().next().getMessage());
//        menuService.create(menuRequest);
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createMenuTest() {
        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(new MenuGroup()));
        when(menuRepository.save(any())).thenReturn(짜장면);

        // when
        MenuResponse createdMenu = 메뉴를_주문한다(짜장면_요청);

        // then
        assertAll(
                () -> assertThat(createdMenu.getName()).isEqualTo("짜장면")
        );
    }

    @DisplayName("메뉴의 가격은 -가 될 수 없다.")
    @Test
    void createMenuPriceNegativeExceptionTest() {
        assertThatThrownBy(() -> {
            when(menuGroupRepository.findById(any())).thenReturn(Optional.of(new MenuGroup()));

            // given
            final MenuRequest 메뉴_가격_마이너스_요청 = new MenuRequest("메뉴 가격 -", -100, null, null);

            // when
            메뉴를_주문한다(메뉴_가격_마이너스_요청);

            // then
        }).isInstanceOf(NegativePriceException.class);
    }

    @DisplayName("메뉴 그룹이 존재해야 한다.")
    @Test
    void createMenuHasMenuGroupExceptionTest() {
        assertThatThrownBy(() -> {
            //given
            final MenuRequest 메뉴_그룹_없는_요청 = new MenuRequest("테스트 메뉴", 0, null, null);

            // when
            메뉴를_주문한다(메뉴_그룹_없는_요청);

            // then
        }).isInstanceOf(NotFoundEntityException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void getMenusTest() {
        when(menuRepository.findAll()).thenReturn(Lists.newArrayList(짜장면));

        // when
        List<MenuResponse> menus = 메뉴_목록_조회한다();

        // then
        assertThat(menus.get(0).getName()).isEqualTo("짜장면");
    }

    private MenuResponse 메뉴를_주문한다(MenuRequest menuRequest) {
        return menuService.create(menuRequest);
    }

    private List<MenuResponse> 메뉴_목록_조회한다() {
        return menuService.list();
    }
}