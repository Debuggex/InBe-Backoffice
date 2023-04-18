package inbe.project.backoffice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import inbe.project.backoffice.Repositories.UserRepository;
import inbe.project.backoffice.RequestDTO.LoginDTO;
import inbe.project.backoffice.ResponseDTO.LogInResponse;
import inbe.project.backoffice.ResponseDTO.Response;
import inbe.project.backoffice.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        super.setFilterProcessesUrl("/user/login");

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginDTO loginDTO;
        try {

            loginDTO = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);
            log.info(loginDTO.getEmail(), loginDTO.getPassword());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword());
            try {
                return authenticationManager.authenticate(authenticationToken);
            } catch (RuntimeException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("Email Password Combination does not match");

                return null;
            }
        } catch (IOException e) {
            return null;
        }


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String accessToken= JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() +20
                        *60*1000))
                .withIssuer(request.getRequestURI())
                .withClaim("userTypes",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);


        Users users= userRepository.findAll().stream().filter(
                users1 -> users1.getEmail().equals(user.getUsername())
        ).findFirst().get();
        LogInResponse logInResponse = new LogInResponse();

        {
            Response<LogInResponse> response1 = new Response<>();
            logInResponse.setId(users.getId());
            logInResponse.setEmail(users.getEmail());
            logInResponse.setFirstName(users.getFirstName());
            logInResponse.setLastName(users.getLastName());
            logInResponse.setAccessToken(accessToken);
            logInResponse.setRole(users.getRole());

            response1.setResponseCode(1);
            response1.setResponseMessage("LogIn Successfully");
            response1.setResponseBody(logInResponse);
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getOutputStream(), response1);
        }


    }

}
