package kitchenpos.menu.dto.validate;

import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component // mandatory!!! (or one of its descendants) to enable `@Autowired`
@Scope("request") // not mandatory, but probably "good" for "validation"
public class MenuGroupMustExistValidator implements ConstraintValidator<MenuGroupMustExist, Long> {
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    public MenuGroupMustExistValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {

        return menuGroupRepository.existsById(value);
    }
}
