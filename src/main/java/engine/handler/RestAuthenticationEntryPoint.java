package engine.handler;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import engine.dto.ApiErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {

        String errorMessage;

        if (authException instanceof BadCredentialsException) {
            errorMessage = "Incorrect email or password.";
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            errorMessage = "Full authentication is required to access this resource";
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }

        ApiErrorDto errorDto = ApiErrorDto.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(errorMessage)
                .path(request.getRequestURI())
                .build();

        try (PrintWriter writer = response.getWriter()) {
            JsonMapper jsonMapper = new JsonMapper();
            jsonMapper.registerModule(new JavaTimeModule());

            writer.print(jsonMapper.writeValueAsString(errorDto));
        }
    }
}
