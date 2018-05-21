package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.UserEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.CalendarState;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.form.AddCalendarForm;
import fr.nantes.eni.alterplanning.model.form.AddUserForm;
import fr.nantes.eni.alterplanning.model.form.ChangePasswordForm;
import fr.nantes.eni.alterplanning.model.form.UpdateUserForm;
import fr.nantes.eni.alterplanning.model.form.validator.ChangePasswordValidator;
import fr.nantes.eni.alterplanning.model.form.validator.UserValidator;
import fr.nantes.eni.alterplanning.model.response.StringResponse;
import fr.nantes.eni.alterplanning.service.dao.CalendarDAOService;
import fr.nantes.eni.alterplanning.service.dao.CoursDAOService;
import fr.nantes.eni.alterplanning.service.dao.UserDAOService;
import fr.nantes.eni.alterplanning.service.mailer.UserMailer;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Resource
    private CalendarDAOService calendarDAOService;

    @GetMapping("")
    public List<CalendarEntity> getCalendars() {
        return calendarDAOService.findAll();
    }

    @GetMapping("/{id}")
    public CalendarEntity getCalendarById(@PathVariable(name = "id") int id) throws RestResponseException {
        final CalendarEntity c = calendarDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendar not found");
        }

        return c;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public CalendarEntity addCalendar(@Valid @RequestBody AddCalendarForm form, BindingResult result) throws RestResponseException {

        if (form.getEndDate() != null && form.getStartDate() != null && form.getEndDate().before(form.getStartDate())) {
            result.addError(new FieldError("startDate",  "startDate", "should be before endDate"));
            result.addError(new FieldError("endDate",  "endDate", "should be after startDate"));
        }

        // TODO: check stagiaireId exist
        // TODO: check entrepriseId exist

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Bad request", result);
        }

        // Define new Calendar
        final CalendarEntity calendar = new CalendarEntity();
        calendar.setStagiaireId(form.getStagiaireId());
        calendar.setEntrepriseId(form.getEntrepriseId());
        calendar.setStartDate(form.getStartDate());
        calendar.setEndDate(form.getEndDate());
        calendar.setState(CalendarState.DRAFT);

        return calendarDAOService.create(calendar);
    }
}
