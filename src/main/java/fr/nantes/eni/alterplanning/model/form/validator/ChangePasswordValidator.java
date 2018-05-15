package fr.nantes.eni.alterplanning.model.form.validator;

import fr.nantes.eni.alterplanning.dao.mysql.entity.UserEntity;
import fr.nantes.eni.alterplanning.model.form.ChangePasswordForm;
import fr.nantes.eni.alterplanning.service.dao.UserDAOService;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by ughostephan on 24/06/2017.
 */
public class ChangePasswordValidator implements Validator{

    private final int id;
    private final UserDAOService userDAOService;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordValidator(int id, UserDAOService userDAOService, PasswordEncoder passwordEncoder) {
        this.userDAOService = userDAOService;
        this.id = id;
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
            final UserEntity user = userDAOService.findById(id);

            if (!passwordEncoder.matches(request.getOld_password(), user.getPassword())) {
                errors.rejectValue("old_password", null, "not match with database");
            }
        }
    }

}
