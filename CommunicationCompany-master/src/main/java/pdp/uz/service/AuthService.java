package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pdp.uz.config.SecurityConfig;
import pdp.uz.entity.User;
import pdp.uz.entity.enums.RoleNames;
import pdp.uz.payload.LoginDto;
import pdp.uz.payload.UserDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.repository.RoleRepository;
import pdp.uz.repository.UserRepository;
import pdp.uz.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SecurityConfig securityConfig;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;


    public ApiResponse login(LoginDto dto) {

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            User user = (User) authenticate.getPrincipal();

            String token = jwtProvider.generateToken(user.getUsername(), user.getRoles());

            return new ApiResponse("Token", true, token);
        } catch (AuthenticationException exception) {
            return new ApiResponse("Sorry your password invalid", false);
        }

    }

    public ApiResponse register(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            return new ApiResponse("This email already exist", false);
        }
        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleNames(RoleNames.ROLE_USER)));
        user.setEmailCode(UUID.randomUUID().toString());
        String link = "http://localhost:8080/api/verifyEmail?emailCode=%s&email=%s"
                .formatted(user.getEmailCode(), user.getEmail());
        if (securityConfig.sendMessage(user.getEmail(), link)) {
            userRepository.save(user);
            return new ApiResponse("User Saved verify accaunt", true);
        }
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return new ApiResponse("User not verify", false);
//        SecurityContextHolder.getContext().getAuthentication().getPrincipal()

    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailCodeAndEmail(emailCode, email);
        if (!optionalUser.isPresent())
            return new ApiResponse("Accaunt oldin tasdiqlangan", false);

        if (!optionalUser.get().isEnabled()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Accaunt tasdiqlandi", true);
        }
        return new ApiResponse("Accaunt oldin tasdiqlangan", false);

    }
}
