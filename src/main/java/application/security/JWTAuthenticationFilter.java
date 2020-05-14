package application.security;

import org.springframework.security.core.Authentication;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException{

        System.out.println("JWTAuthenticationFilter.doFilter");

        Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest) servletRequest);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}