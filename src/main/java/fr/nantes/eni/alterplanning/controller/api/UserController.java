package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.dao.mysql.entity.UserEntity;
import fr.nantes.eni.alterplanning.model.form.AddUserForm;
import fr.nantes.eni.alterplanning.model.form.ChangePasswordForm;
import fr.nantes.eni.alterplanning.model.form.UpdateUserForm;
import fr.nantes.eni.alterplanning.model.form.validator.ChangePasswordValidator;
import fr.nantes.eni.alterplanning.model.form.validator.UserValidator;
import fr.nantes.eni.alterplanning.model.response.StringResponse;
import fr.nantes.eni.alterplanning.service.dao.UserDAOService;
import fr.nantes.eni.alterplanning.service.mailer.UserMailer;
import fr.nantes.eni.alterplanning.util.HistoryUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Resource
    private HistoryUtil historyUtil;

    @Resource
    private UserDAOService userDAOService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMailer mailer;

    @GetMapping("")
    public List<UserEntity> getUsers() {
        return userDAOService.findAll();
    }

    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable(name = "id") int id) throws RestResponseException {
        final UserEntity u = userDAOService.findById(id);

        if (u == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }

        return u;
    }

    @GetMapping("/me")
    public UserEntity getCurrentUser() throws RestResponseException {
        final UserEntity userFromToken = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userFromToken == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }

        return userFromToken;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity addUser(@Valid @RequestBody AddUserForm form, BindingResult result) throws RestResponseException {

        new UserValidator(userDAOService).validate(form, result);

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        // Define new User
        final UserEntity userToAdd = new UserEntity();
        userToAdd.setName(form.getName());
        userToAdd.setEmail(form.getEmail());
        userToAdd.setPassword(passwordEncoder.encode(form.getPassword()));

        // Create user
        final UserEntity userAdded = userDAOService.create(userToAdd);

        // Sen mail to New User
        mailer.notifyNewUser(userAdded, form.getPassword());

        historyUtil.addLine("Ajout de l'utilisateur n°" + userAdded.getId() +
                " (" + userAdded.getName() + " - " + userAdded.getEmail() + ")");

        return userAdded;
    }

    @PutMapping("/{id}")
    public StringResponse updateUser(@Valid @RequestBody UpdateUserForm form, BindingResult result,
                                     @PathVariable(name = "id") int id) throws RestResponseException {

        new UserValidator(userDAOService)
                .validate(form, result);

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        // Find user to update
        final UserEntity userToUpdate = userDAOService.findById(id);

        if (userToUpdate == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }

        // Set properties to update
        if (StringUtils.isNotEmpty(form.getName())) {
            userToUpdate.setName(form.getName());
        }

        if (StringUtils.isNotEmpty(form.getEmail())) {
            userToUpdate.setEmail(form.getEmail());
        }

        // Update user
        userDAOService.update(userToUpdate);

        historyUtil.addLine("Modification de l'utilisateur n°" + userToUpdate.getId());

        return new StringResponse("Utilisateur modifié avec succès");
    }

    @PutMapping("/{id}/change-password")
    public StringResponse changePassword(@Valid @RequestBody ChangePasswordForm form,
                                         BindingResult result,
                                         @PathVariable(name = "id") int id) throws RestResponseException {

        // User from Token
        final UserEntity userFromToken = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userFromToken.getId() != id) {
            throw new RestResponseException(HttpStatus.UNAUTHORIZED, "Vous ne pouvez pas mettre à jour le mot de passe de quelqu'un d'autre");
        }

        new ChangePasswordValidator(id, userDAOService, passwordEncoder)
                .validate(form, result);

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        // Find user to update
        final UserEntity userToUpdate = userDAOService.findById(id);

        if (userToUpdate == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }

        // Change password
        userToUpdate.setPassword(passwordEncoder.encode(form.getNew_password()));

        // Update User
        userDAOService.update(userToUpdate);

        // Send Mail to notify password change
        mailer.notifyChangePassword(userToUpdate, form.getNew_password());

        historyUtil.addLine("Réinitialisation du mot de passe de l'utilisateur n°"
                + userToUpdate.getId() + " (" + userToUpdate.getName() + " - " + userToUpdate.getEmail() + ")");

        return new StringResponse("Mot de passe mis à jour avec succès");
    }

    @DeleteMapping("/{id}")
    public StringResponse deactivateUser(@PathVariable(name = "id") int id) throws RestResponseException {

        // User from Token
        final UserEntity userFromToken = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userFromToken.getId() != id) {
            throw new RestResponseException(HttpStatus.UNAUTHORIZED, "Vous ne pouvez pas vous désactiver vous même");
        }

        // Find user to delete
        final UserEntity u = userDAOService.findById(id);

        if (u == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }

        // Delete User
        // userDAOService.delete(id);

        u.setActive(false);

        // Update User
        userDAOService.update(u);

        historyUtil.addLine("Désactivation de l'utilisateur n°" + id +
                " (" + u.getName() + " - " + u.getEmail() + ")");

        return new StringResponse("Utilisateur désactivé avec succès");
    }
}
