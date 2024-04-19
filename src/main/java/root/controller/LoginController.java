package root.controller;

import root.dto.ResponseDTO;
import root.dto.UserDTO;
import root.entity.User;
import root.repository.UserRepo;
import root.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserRepo userRepo;

    @PostMapping("/login")
    public ResponseDTO<String> login(
        @RequestParam("username") String username,
        @RequestParam("password") String password
    ) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

        // login success, jwt - generate token (string), nguoc lai throw exception
        return ResponseDTO.<String>builder()
            .status(200)
            .data(jwtTokenService.createToken(username))
            .msg("OK").build();
    }

    @GetMapping("/logout")
    public ResponseDTO<String> logout() {
        return ResponseDTO.<String>builder().status(200).msg("OK").build();
    }

    // method security
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    /*public Principal me(Principal principal) {
        // principal:: current user đang đăng nhập
        // String username = principal.getName();
        return principal;
    }*/
    public UserDTO me(Principal principal) {
        Optional<User> user = userRepo.findByUsername(principal.getName());
        return user.map(this::convert).orElse(null);
    }

    private UserDTO convert(User user) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(user, UserDTO.class);
    }

}
