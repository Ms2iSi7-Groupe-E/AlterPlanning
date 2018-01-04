package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.util.DataEnvelop;
import fr.nantes.eni.alterplanning.util.JwtTokenUtil;
import fr.nantes.eni.alterplanning.bean.User;
import fr.nantes.eni.alterplanning.model.AuthenticationModel;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by ughostephan on 23/06/2017.
 */
@RestController
@RequestMapping("/api")
@Api(tags = "Authentication", description = "Endpoint for create JWT Token ")
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth")
    public String createAuthenticationToken(@Valid @RequestBody AuthenticationModel model, BindingResult result) throws RestResponseException {

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

        // Return the token
        return "Bearer " + jwtTokenUtil.generateToken((User) authentication.getPrincipal());
    }

}
