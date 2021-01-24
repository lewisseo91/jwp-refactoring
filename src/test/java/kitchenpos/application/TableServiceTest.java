package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @MockBean
    private OrderDao orderDao;

    @DisplayName("테이블을 생성한다")
    @Test
    void create() {
        OrderTable orderTable = 테이블을_생성한다(1l, 0, true);

        assertAll(
                () -> assertEquals(orderTable.getNumberOfGuests(), 0),
                () -> assertEquals(orderTable.isEmpty(), true)
        );
    }

    @DisplayName("테이블들을 조회한다")
    @Test
    void list() {
        List<OrderTable> list = tableService.list();

        assertThat(list.size()).isGreaterThanOrEqualTo(1);
    }

    @DisplayName("테이블의 상태를 변경할 수 있다")
    @Test
    void changeEmpty() {
        OrderTable orderTable = 테이블을_생성한다(1l, 0, true);
        orderTable.setEmpty(false);

        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);
        OrderTable changeOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        assertThat(changeOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("테이블의 상태를 변경할 수 없다 : OrderStatus가 COOKING, MEAL인 경우")
    @Test
    void changeEmptyException() {
        OrderTable orderTable = 테이블을_생성한다(1l, 0, true);
        orderTable.setEmpty(false);

        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 고객수를 변경할 수 있다")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = 테이블을_생성한다(1l, 0, false);
        orderTable.setNumberOfGuests(10);
        OrderTable changeOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("테이블의 고객수를 변경할 수 없다 : 게스트수가 0미만인 경우")
    @Test
    void changeNumberOfGuestsNumberException() {
        OrderTable orderTable = 테이블을_생성한다(1l, 0, false);
        orderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 고객수를 변경할 수 없다 : 테이블 상태가 비어있는 경우")
    @Test
    void changeNumberOfGuestsStatusException() {
        OrderTable orderTable = 테이블을_생성한다(1l, 0, true);
        orderTable.setNumberOfGuests(10);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable 테이블을_생성한다(Long id, int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTable(id, numberOfGuest, empty));
    }
}













