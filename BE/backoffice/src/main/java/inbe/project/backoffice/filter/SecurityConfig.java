package inbe.project.backoffice.filter;

import inbe.project.backoffice.Repositories.RoleRepository;
import inbe.project.backoffice.Repositories.UserRepository;
import inbe.project.backoffice.domain.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;


    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(encoder());
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.logout().logoutUrl("/user/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID");
        http.apply(new CustomDSL(userRepository));
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/customer/signup/**").permitAll();
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/user/getCustomers/**").hasAnyAuthority("ADMIN","ANALYST","SUPERVISOR");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/user/assignAnalyst/**").hasAnyAuthority("ADMIN","ANALYST","SUPERVISOR");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/user/signup/**").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/form/**").hasAnyAuthority("ADMIN","ANALYST","SUPERVISOR");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/form/**").hasAnyAuthority("ADMIN","ANALYST","SUPERVISOR");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    public static class CustomDSL extends AbstractHttpConfigurer<CustomDSL, HttpSecurity> {

        private final UserRepository userRepository;

        public CustomDSL(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public void configure(HttpSecurity http) {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilter(new CustomAuthenticationFilter(authenticationManager, userRepository));
        }

        public CustomDSL customDsl() {
            return new CustomDSL(userRepository);
        }
    }

}
