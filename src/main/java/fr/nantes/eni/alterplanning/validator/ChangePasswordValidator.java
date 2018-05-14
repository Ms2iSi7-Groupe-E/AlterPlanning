package fr.nantes.eni.alterplanning.validator;


import fr.nantes.eni.alterplanning.model.bean.User;
import fr.nantes.eni.alterplanning.model.form.ChangePasswordForm;
import fr.nantes.eni.alterplanning.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by ughostephan on 24/06/2017.
 */
public class ChangePasswordValidator implements Validator{

    private final String uid;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordValidator(String uid, UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uid = uid;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(Class clazz) {
        return ChangePasswordForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final ChangePasswordForm request = (ChangePasswordForm) target;

        if (request.getOld_password().equals(request.getNew_password())) {
            errors.rejectValue("old_password", null, "should be different of new_password");
            errors.rejectValue("new_password", null, "should be different of old_password");
        }

        if (StringUtils.isNotEmpty(request.getOld_password())) {
            final User user = userService.findById(uid);

            if (!passwordEncoder.matches(request.getOld_password(), user.getPassword())) {
                errors.rejectValue("old_password", null, "not match with database");
            }
        }
    }

}
