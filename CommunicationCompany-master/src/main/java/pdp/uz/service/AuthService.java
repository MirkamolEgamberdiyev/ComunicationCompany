package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.payload.LoginDto;
import pdp.uz.security.JwtProvider;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    public ApiResponse login(LoginDto dto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(user.getUsername(), user.getAuthorities());
            return new ApiResponse("OK", true, token);
        } catch (BadCredentialsException e) {
            return new ApiResponse("Login or password incorrect", false);
        }
    }
}
