package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.bean.User;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.AuthenticationModel;
import fr.nantes.eni.alterplanning.model.response.StringResponse;
import fr.nantes.eni.alterplanning.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by ughostephan on 23/06/2017.
 */
@RestController
@RequestMapping("/api")
@Api(tags = "Authentication", description = " ")
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth")
    @ApiOperation(value = "Create Authentication Token")
    public StringResponse createAuthenticationToken(@Valid @RequestBody AuthenticationModel model, BindingResult result)
            throws RestResponseException {

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Bad request", result);
        }

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        model.getEmail(),
                        model.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Create the token
        final String token = jwtTokenUtil.generateToken((User) authentication.getPrincipal());
        return new StringResponse(token);
    }

}
