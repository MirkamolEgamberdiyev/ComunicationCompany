package pdp.uz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pdp.uz.security.JwtFilter;
import pdp.uz.service.UserService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilter jwtFilter;

    private static final String[] WHITE_LIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    @Autowired
    private UserService userDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(WHITE_LIST).permitAll()
                .antMatchers("/api/auth").permitAll()
                .antMatchers(HttpMethod.GET, "/api/tariff/**", "/api/package/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/payment/**").permitAll()
                .anyRequest()
                .authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/swagger-resources/**");
        web.ignoring().antMatchers("/v2/api-docs/**");
        web.ignoring().antMatchers("/swagger.json");
        web.ignoring().antMatchers("/swagger-ui.html");
        web.ignoring().antMatchers("/webjars/**");
        web.ignoring().antMatchers("/api/auth");
    }


    public Boolean sendMessage(String recipient, String link) {
        try {
            Properties properties = new Properties();
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.debug", "true");
//        return mailSender;

            Properties newProperties = new Properties();
            newProperties.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
            newProperties.put("mail.smtp.port", "587");
            newProperties.put("mail.smtp.starttls.enable", "true");
            newProperties.put("mail.smtp.auth", "true");
            String username = "a158d78b65a1df";
            String password = "471c9eefc37f12";
            Session session = getSession(newProperties, username, password);
            Message message = new MimeMessage(session);
            message.setSubject("Test uchun subjecct");
            String mess = String.format("<h1><a href='/%s'>Tasdiqlang</a></h1>", link);
            message.setContent(mess, "text/html");
            message.setFrom(new InternetAddress(username));
//            String recipient = "farruxmashrapov92@gmail.com";
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }


    private Session getSession(Properties newProperties, String username, String password) {
        return Session.getInstance(newProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }



}
