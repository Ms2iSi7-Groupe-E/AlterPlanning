package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.util.DataEnvelop;
import fr.nantes.eni.alterplanning.util.JwtTokenUtil;
import fr.nantes.eni.alterplanning.bean.User;
import fr.nantes.eni.alterplanning.model.AuthenticationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity createAuthenticationToken(@Valid @RequestBody AuthenticationModel model, BindingResult result) {

        if (result.hasErrors()) {
            return DataEnvelop.CreateEnvelop(HttpStatus.BAD_REQUEST, "Bad request", result);
        }

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        model.getEmail(),
                        model.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final String token = jwtTokenUtil.generateToken((User) authentication.getPrincipal());

        // Return the token
        return DataEnvelop.CreateEnvelop(token);
    }

}
