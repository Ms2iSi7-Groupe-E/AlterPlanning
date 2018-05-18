package fr.nantes.eni.alterplanning.controller.api;

import com.sun.media.jfxmedia.logging.Logger;
import fr.nantes.eni.alterplanning.dao.mysql.entity.ParameterEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.form.AddParameterForm;
import fr.nantes.eni.alterplanning.model.form.UpdateParameterForm;
import fr.nantes.eni.alterplanning.model.response.StringResponse;
import fr.nantes.eni.alterplanning.service.dao.ParameterDAOService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/parameter")
public class ParameterController {

    @Resource
    private ParameterDAOService parameterDAOService;

    @GetMapping("")
    public List<ParameterEntity> getParameters() {
        return parameterDAOService.findAll();
    }

    @GetMapping("/{key}")
    public ParameterEntity getParameterByKey(@PathVariable(name = "key") String key) throws RestResponseException {

        final ParameterEntity t = parameterDAOService.findById(key);

        if (t == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Parameter not found");
        }

        return t;
    }

    @PostMapping("")
    public ParameterEntity addParameter(@Valid @RequestBody AddParameterForm form, BindingResult result) throws RestResponseException{

        if(result.hasErrors()){
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Bad Request", result);
        }

        //Construct new Parameter
        final ParameterEntity newParam = new ParameterEntity();
        newParam.setKey(form.getKey());
        newParam.setValue(form.getValue());

        //Save Parameter
        return parameterDAOService.create(newParam);
    }

    @PutMapping("/{key}")
    public StringResponse updateParameter(@Valid @RequestBody UpdateParameterForm form, BindingResult result,
                                          @PathVariable(name = "key") String key) throws RestResponseException {

        if(result.hasErrors()){
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Bad Request", result);
        }

        final ParameterEntity parameterToUpdate = parameterDAOService.findById(key);

        if (parameterToUpdate == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Parameter not found");
        }

        //Update and save new Parameter
        parameterToUpdate.setValue(form.getValue());
        parameterDAOService.update(parameterToUpdate);

        return new StringResponse("Parameter updated");
    }


    /**
     * Delete Method is useless
     */

}
