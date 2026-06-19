package jobportal.service;

import jobportal.dto.LoginResponse;
import jobportal.exception.BadRequestException;
import jobportal.exception.ResourceNotFoundException;
import jobportal.model.User;
import jobportal.repository.UserRepository;
import jobportal.util.AuthUtil;
import jobportal.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ── UserDetailsService (required by Spring Security) ──────────────────────

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    // ── CRUD ──────────────────────────────────────────────────────────────────

    public User saveUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new BadRequestException("Email is required");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new BadRequestException("Email already registered");
        }
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole(AuthUtil.ROLE_USER);
        }
        // Hash the plain-text password before persisting
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        user.setRole(userDetails.getRole());
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    // ── Login (returns JWT) ───────────────────────────────────────────────────

    public LoginResponse loginUser(String email, String password) {
        User user = (User) loadUserByUsername(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new jobportal.exception.UnauthorizedException("Invalid email or password");
        }
        String token = jwtUtil.generateToken(user);
        return new LoginResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), token);
    }
}
