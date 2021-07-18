package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CannotFindException extends RuntimeException {

    public CannotFindException(Message message) {
        super(message.showText());
    }
}