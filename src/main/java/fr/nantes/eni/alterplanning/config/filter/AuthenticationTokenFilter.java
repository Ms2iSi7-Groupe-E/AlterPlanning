package fr.nantes.eni.alterplanning.config.filter;

import fr.nantes.eni.alterplanning.dao.mysql.entity.UserEntity;
import fr.nantes.eni.alterplanning.service.dao.UserDAOService;
import fr.nantes.eni.alterplanning.util.JwtTokenUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ughostephan on 24/06/2017.
 */
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private UserDAOService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final String authorizationToken = request.getHeader(this.tokenHeader);
        String token = null;
        String tokenType = "";
        if (StringUtils.isNotEmpty(authorizationToken)) {
            try {
                tokenType = authorizationToken.split(" ")[0];
                token = authorizationToken.split(" ")[1];
            } catch (Exception e) {
                token = null;
            }
        }

        final Integer id = jwtTokenUtil.getIdFromToken(token);

        if (authorizationToken != null && id == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token is not valid");
            return;
        }

        if (authorizationToken != null && !"Bearer".equals(tokenType)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token format is not valid. " +
                    "Authorization header should have a token formatted like: Bearer <token>");
            return;
        }

        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // It is not compelling necessary to load the use details from the database. You could also store the information
            // in the token and read it from it. It's up to you ;)
            final UserEntity user = userService.findById(id);

            if (user != null && !user.isActive()) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Identifiants incorrects");
                return;
            }
            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
            // the database compellingly. Again it's up to you ;)
            if (user != null && jwtTokenUtil.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token has expired or as not match to a registered user");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
