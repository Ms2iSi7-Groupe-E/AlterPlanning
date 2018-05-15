package fr.nantes.eni.alterplanning.model.form.validator;

import fr.nantes.eni.alterplanning.service.dao.UserDAOService;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by ughostephan on 24/06/2017.
 */
public class UserValidator implements Validator {

    private final UserDAOService userDAOService;

    public UserValidator(UserDAOService userDAOService) {
        this.userDAOService = userDAOService;
    }

    @Override
    public boolean supports(Class clazz) {
        return IUserMailValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final IUserMailValidator request = (IUserMailValidator) target;

        if (StringUtils.isNotEmpty(request.getEmail())) {
            if (userDAOService.emailAlreadyUsed(request.getEmail())) {
                errors.rejectValue("email",  null, "already used");
            }
        }
    }

}
