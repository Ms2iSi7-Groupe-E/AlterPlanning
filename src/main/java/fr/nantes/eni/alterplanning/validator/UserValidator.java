package fr.nantes.eni.alterplanning.validator;


import fr.nantes.eni.alterplanning.model.bean.User;
import fr.nantes.eni.alterplanning.model.form.IUserForm;
import fr.nantes.eni.alterplanning.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ughostephan on 24/06/2017.
 */
public class UserValidator implements Validator{

    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class clazz) {
        return IUserForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final IUserForm request = (IUserForm) target;

        if (StringUtils.isNotEmpty(request.getBirthday())) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(User.BIRTHDAY_PATTERN);
                Date birthday = df.parse(request.getBirthday());

                if (birthday.after(new Date())) {
                    errors.rejectValue("birthday", null, "should be in the past");
                }
            } catch (ParseException e) {
                errors.rejectValue("birthday",  null, "not a well-formed date '" + User.BIRTHDAY_PATTERN + "'");
            }
        }

        if (StringUtils.isNotEmpty(request.getEmail())) {
            if (userService.emailAlreadyUsed(request.getEmail())) {
                errors.rejectValue("email",  null, "already used");
            }
        }
    }

}
