package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.dao.mysql.entity.UserEntity;
import fr.nantes.eni.alterplanning.model.form.AuthenticationForm;
import fr.nantes.eni.alterplanning.model.response.TokenResponse;
import fr.nantes.eni.alterplanning.util.JwtTokenUtil;
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
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth")
    public TokenResponse createAuthenticationToken(@Valid @RequestBody AuthenticationForm form, BindingResult result)
            throws RestResponseException {

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Bad request", result);
        }

        // Perform the security
        final Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            form.getEmail(),
                            form.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new RestResponseException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Create the token
        final String token = jwtTokenUtil.generateToken((UserEntity) authentication.getPrincipal());
        return new TokenResponse(token);
    }

}
