package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("주문 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private OrderService orderService;
    private Order order;
    private OrderRequest orderRequest;
    private OrderLineItem orderLineItem;
    private OrderLineItem orderLineItem2;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        when(orderTableRepository.save(any())).thenReturn(new OrderTable(1L, 3));

        orderService = new OrderService(orderRepository, orderTableRepository);
        orderLineItem = new OrderLineItem();
        orderLineItem2 = new OrderLineItem();
        order = new Order(1L, new OrderTable(), Lists.newArrayList(orderLineItem, orderLineItem2));

        orderTable = orderTableRepository.save(new OrderTable(3));
        orderRequest = new OrderRequest(orderTable.getId(), Lists.newArrayList(orderLineItem, orderLineItem2));
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createOrderTest() {
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(new OrderTable(1L, new TableGroup(), 3, false)));
        when(orderRepository.save(any())).thenReturn(order);

        // when
        OrderResponse createdOrder = orderService.create(orderRequest);

        // then
        assertAll(
                () -> assertThat(createdOrder.getOrderLineItems().get(0)).isEqualTo(orderLineItem),
                () -> assertThat(createdOrder.getOrderLineItems().get(1)).isEqualTo(orderLineItem2)
        );
    }

    @DisplayName("메뉴에 존재하는 상품들은 모두 존재해야 한다.")
    @Test
    void createOrderExistProductExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            final OrderRequest emptyOrderLineItem = new OrderRequest(orderTable.getId(), Lists.emptyList());

            // when
            OrderResponse createdOrder = orderService.create(emptyOrderLineItem);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 개수가 동일해야 한다.")
    @Test
    void createOrderSameSizeOrderLineItemExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            OrderResponse createdOrder = orderService.create(orderRequest);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재해야 한다.")
    @Test
    void createOrderExistOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            final OrderRequest emptyOrderTableItem = new OrderRequest(null, Lists.newArrayList(orderLineItem, orderLineItem2));

            // when
            OrderResponse createdOrder = orderService.create(emptyOrderTableItem);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재해야 한다.")
    @Test
    void createOrderNotEmptyOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            // todo : illegalargument type 변경
//            when(orderTableRepository.findByOrderTable(any())).thenReturn(Optional.of(new OrderTable(1L, 1L, 3, true)));
//            when(orderRepository.save(any())).thenReturn(new Order(1L, new OrderTable(1L, 1L, 3, true), "MEAL", LocalDateTime.now(), Lists.newArrayList(orderLineItem, orderLineItem2)));
            // given
            final OrderRequest emptyOrderTableItem = new OrderRequest(orderTable.getId(), Lists.newArrayList(orderLineItem, orderLineItem2));

            // when
            OrderResponse createdOrder = orderService.create(emptyOrderTableItem);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void getOrderTest() {
        when(orderRepository.findAll()).thenReturn(Lists.newArrayList(order));

        // when
        List<OrderResponse> createdOrders = orderService.list();

        // then
        assertThat(createdOrders.get(0).getId()).isEqualTo(order.getId());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatusTest() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        // given
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), "MEAL");

        // when
        OrderResponse changedOrders = orderService.changeOrderStatus(2L, orderRequest);

        // then
        assertThat(changedOrders.getId()).isEqualTo(order.getId());
    }

    @DisplayName("주문 id는 반드시 존재한다.")
    @Test
    void changeOrderIdExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            OrderResponse changedOrders = orderService.changeOrderStatus(null, orderRequest);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태는 완료가 아니어야 한다.")
    @Test
    void changeOrderNotCompleteStatusExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            order.changeOrderStatus(OrderStatus.COMPLETION);

            // when
            OrderResponse changedOrders = orderService.changeOrderStatus(2L, orderRequest);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }


}