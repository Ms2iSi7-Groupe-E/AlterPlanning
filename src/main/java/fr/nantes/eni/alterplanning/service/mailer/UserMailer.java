package fr.nantes.eni.alterplanning.service.mailer;

import fr.nantes.eni.alterplanning.model.entity.UserEntity;
import fr.nantes.eni.alterplanning.service.MailService;
import org.apache.velocity.VelocityContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by ughostephan on 25/06/2017.
 */
@Component
public class UserMailer {

    @Resource
    private MailService service;

    /**
     * Notify new user.
     *
     * @param user          the user
     * @param clearPassword the clear password
     */
    public void notifyNewUser(final UserEntity user, final String clearPassword) {

        VelocityContext context = new VelocityContext();
        context.put("user", user);
        context.put("password", clearPassword);
        final String content = service.resolveTemplate("templates/new-user.vm", context);

        service.sendMailHtml(user.getEmail(), null, "Welcome", content);
    }

    /**
     * Notify change password.
     *
     * @param user the user
     * @param new_password the new password
     */
    public void notifyChangePassword(final UserEntity user, final String new_password) {
        VelocityContext context = new VelocityContext();
        context.put("password", new_password);
        final String content = service.resolveTemplate("templates/change-password.vm", context);

        service.sendMailHtml(user.getEmail(), null, "Password change", content);
    }
}