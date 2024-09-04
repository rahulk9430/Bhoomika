package com.sopra.UserManagement.Controller;

import com.sopra.UserManagement.Entity.User;
import com.sopra.UserManagement.Exception.ResourceNotFoundException;
import com.sopra.UserManagement.Repository.UserRepository;
import com.sopra.UserManagement.SecurityConfig.JwtProvider;
import com.sopra.UserManagement.Service.CustomUserDetailsService;
import com.sopra.UserManagement.response.AuthResponse;
import com.sopra.UserManagement.response.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class AuthController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/signup")
    public AuthResponse createUser(@RequestBody User user) throws Exception{
        String email=user.getEmail();
        String password=user.getPassword();
        String fullName=user.getFullName();
        String encodedPassword = passwordEncoder.encode(password);

        User isExistsEmail = userRepo.findByEmail(email);
        if(isExistsEmail!=null){
            throw new Exception("User is already present with this Email!!!: "+email);
        }
        User createdUser=new User();
        createdUser.setEmail(email);
        createdUser.setPassword(encodedPassword);
        createdUser.setFullName(fullName);

        User savedUser=userRepo.save(createdUser);

        Authentication authentication=new UsernamePasswordAuthenticationToken(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token=jwtProvider.generateToken(authentication);

        AuthResponse res=new AuthResponse();

        res.setJwt(token);
        res.setMessage("signup success!");
        return  res;
    }


//    @PostMapping("/signin")
//    public AuthResponse sigInHandler(@RequestBody LoginRequest loginRequest){
//        String username=loginRequest.getEmail();
//        String password=loginRequest.getPassword();
//
//
//        Authentication authentication=authenticate(username,password);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String token=jwtProvider.generateToken(authentication);
//        AuthResponse res=new AuthResponse();
//        res.setJwt(token);
//        res.setMessage("signin successfully!");
//        return res;
//    }
@PostMapping("/signin")
public ResponseEntity<AuthResponse> signInHandler(@RequestBody LoginRequest loginRequest) {
    String username = loginRequest.getEmail();
    String password = loginRequest.getPassword();

    try {
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setJwt(token);
        res.setMessage("Sign in successful!");

        return ResponseEntity.ok(res);
    } catch (BadCredentialsException e) {
        AuthResponse res = new AuthResponse();
        res.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);

    } catch (Exception e) {
        AuthResponse res = new AuthResponse();
        res.setMessage("An error occurred during authentication.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}


    private Authentication authenticate(String username, String password) {

        UserDetails userDetails=customUserDetailsService.loadUserByUsername(username);
        if(userDetails==null){
            throw new BadCredentialsException("user not found!");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

}