package fr.nantes.eni.alterplanning.config.filter;

import fr.nantes.eni.alterplanning.model.entity.UserEntity;
import fr.nantes.eni.alterplanning.service.dao.UserDAOService;
import fr.nantes.eni.alterplanning.util.JwtTokenUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

        String authToken = request.getHeader(this.tokenHeader);
        if (StringUtils.isNotEmpty(authToken)) {
            try {
                authToken = authToken.split(" ")[1];
            } catch (Exception e) {
                authToken = null;
            }
        }

        final Integer id = jwtTokenUtil.getIdFromToken(authToken);

        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // It is not compelling necessary to load the use details from the database. You could also store the information
            // in the token and read it from it. It's up to you ;)
            final UserEntity user = userService.findById(id);

            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
            // the database compellingly. Again it's up to you ;)
            if (jwtTokenUtil.validateToken(authToken, user)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}
