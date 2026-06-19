package jobportal.controller;

import jobportal.dto.LoginResponse;
import jobportal.model.User;
import jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * POST /auth/register
     * Body: { "name": "...", "email": "...", "password": "...", "role": "USER" | "RECRUITER" }
     * Returns the saved User (password is hidden via @JsonIgnore).
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User saved = userService.saveUser(user);
        return ResponseEntity.ok(saved);
    }

    /**
     * POST /auth/login
     * Body: { "email": "...", "password": "..." }
     * Returns LoginResponse containing user info + JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User request) {
        LoginResponse response = userService.loginUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}
