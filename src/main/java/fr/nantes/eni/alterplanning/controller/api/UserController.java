package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.model.bean.User;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.mailer.UserMailer;
import fr.nantes.eni.alterplanning.model.form.AddUserForm;
import fr.nantes.eni.alterplanning.model.form.ChangePasswordForm;
import fr.nantes.eni.alterplanning.model.form.UpdateUserForm;
import fr.nantes.eni.alterplanning.service.UserService;
import fr.nantes.eni.alterplanning.validator.ChangePasswordValidator;
import fr.nantes.eni.alterplanning.validator.UserValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMailer mailer;

    @GetMapping("")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{uid}")
    public User getUserById(@PathVariable(name = "uid") String uid) throws RestResponseException {
        final User u = userService.findById(uid);

        if (u == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "User not found");
        }

        return u;
    }

    @GetMapping("/me")
    public User getCurrentUser() throws RestResponseException {
        final User userFromToken = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userFromToken == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "User not found");
        }

        return userFromToken;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody AddUserForm form, BindingResult result) throws RestResponseException {

        new UserValidator(userService).validate(form, result);

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Bad request", result);
        }

        // Define new User
        final User userToAdd = new User();
        userToAdd.setFirstname(form.getFirstname());
        userToAdd.setLastname(form.getLastname());
        userToAdd.setEmail(form.getEmail());
        userToAdd.setBirthday(form.getBirthdayDate());
        userToAdd.setCity(form.getCity());
        userToAdd.setCountry(form.getCountry());
        userToAdd.setPassword(passwordEncoder.encode(form.getPassword()));
        userToAdd.setCreated_at(new Date());
        userToAdd.setEnabled(true);

        // Create user
        final User userAdded = userService.create(userToAdd);

        // Sen mail to New User
        mailer.notifyNewUser(userAdded, form.getPassword());

        return userAdded;
    }

    @PutMapping("/{uid}")
    public String updateUser(@Valid @RequestBody UpdateUserForm form, BindingResult result,
                                     @PathVariable(name = "uid") String uid) throws RestResponseException {

        new UserValidator(userService)
                .validate(form, result);

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Bad request", result);
        }

        // Find user to update
        final User userToUpdate = userService.findById(uid);

        if (userToUpdate == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Set properties to update
        if (StringUtils.isNotEmpty(form.getFirstname())) {
            userToUpdate.setFirstname(form.getFirstname());
        }

        if (StringUtils.isNotEmpty(form.getLastname())) {
            userToUpdate.setLastname(form.getLastname());
        }

        if (StringUtils.isNotEmpty(form.getEmail())) {
            userToUpdate.setEmail(form.getEmail());
        }

        if (StringUtils.isNotEmpty(form.getBirthday())) {
            userToUpdate.setBirthday(form.getBirthdayDate());
        }

        if (StringUtils.isNotEmpty(form.getCity())) {
            userToUpdate.setCity(form.getCity());
        }

        if (StringUtils.isNotEmpty(form.getCountry())) {
            userToUpdate.setCountry(form.getCountry());
        }

        // Update user
        userService.update(userToUpdate);

        return "User successfully updated";
    }

    @PutMapping("/{uid}/change-password")
    public String changePassword(@Valid @RequestBody ChangePasswordForm form,
                                 BindingResult result,
                                 @PathVariable(name = "uid") String uid) throws RestResponseException {

        // User from Token
        final User userFromToken = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userFromToken.getUid().equals(uid)) {
            throw new RestResponseException(HttpStatus.UNAUTHORIZED, "You can't update someone else password");
        }

        new ChangePasswordValidator(uid, userService, passwordEncoder)
                .validate(form, result);

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Bad request", result);
        }

        // Find user to update
        final User userToUpdate = userService.findById(uid);

        if (userToUpdate == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Change password
        userToUpdate.setPassword(passwordEncoder.encode(form.getNew_password()));

        // Update User
        userService.update(userToUpdate);

        // Send Mail to notify password change
        mailer.notifyChangePassword(userToUpdate, form.getNew_password());

        return "Password successfully updated";
    }

    @DeleteMapping("/{uid}")
    public String deleteUser(@PathVariable(name = "uid") String uid) throws RestResponseException {

        // User from Token
        final User userFromToken = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userFromToken.getUid().equals(uid)) {
            throw new RestResponseException(HttpStatus.UNAUTHORIZED, "You can't delete yourself");
        }

        // Find user to delete
        final User u = userService.findById(uid);

        if (u == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Delete User
        userService.delete(uid);

        return "User successfully deleted";
    }
}
