package jobportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    // WRITE_ONLY: Jackson can deserialize it from JSON (login/register request bodies)
    // but it is never included in any JSON response sent to the client.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String role;

    public User() {}

    // ── UserDetails ───────────────────────────────────────────────────────────

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Role stored as "USER" or "RECRUITER"; Spring Security expects "ROLE_USER" / "ROLE_RECRUITER"
        String authority = (role != null) ? "ROLE_" + role.toUpperCase() : "ROLE_USER";
        return List.of(new SimpleGrantedAuthority(authority));
    }

    /** Spring Security uses getUsername() as the principal name — mapped to email here */
    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired()    { return true; }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked()     { return true; }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired(){ return true; }

    @Override
    @JsonIgnore
    public boolean isEnabled()              { return true; }

    // ── Getters / Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    @JsonIgnore  // guard: never serialize the hash via getter path
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
