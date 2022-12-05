package com.demo.restapiusermanage.controller;

import com.demo.restapiusermanage.api.AuthRequest;
import com.demo.restapiusermanage.api.AuthResponse;
import com.demo.restapiusermanage.entity.User;
import com.demo.restapiusermanage.response.ResponseHandler;
import com.demo.restapiusermanage.service.UserService;
import com.demo.restapiusermanage.token.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<Object> getAllUsers(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "sortField", required = false, defaultValue = "firstName") String sortField,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(value = "term", required = false, defaultValue = "") String term,
            @RequestParam(value = "perPage", required = false, defaultValue = "3") Integer perPage
    ) {

        return ResponseHandler.responseBuilder(
                "success",
                HttpStatus.OK,
                service.retrieveUser(page, sortField, sortDirection, term));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Long id) {
        return ResponseHandler.responseBuilder("success", HttpStatus.OK, service.findOne(id));
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@Valid @RequestBody User user) {

        return ResponseHandler.responseBuilder("success", HttpStatus.CREATED, service.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id, @RequestBody User userDetails) {
        return ResponseHandler.responseBuilder("success", HttpStatus.OK, service.updateUser(userDetails, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        service.deleteUser(id);
        return new ResponseEntity<String>("User deleted successfully", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request){
        try {

            Authentication au = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            Authentication authentication = authManager.authenticate(au);

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtTokenUtil.generateAccessToken(user);
            AuthResponse response = new AuthResponse(user.getEmail(), accessToken);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
