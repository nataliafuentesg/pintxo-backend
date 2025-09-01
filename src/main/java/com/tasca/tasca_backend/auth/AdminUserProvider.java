package com.tasca.tasca_backend.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminUserProvider {
    private final String email;
    private final String name;
    private final String pwdPlain;
    private final String pwdHash;
    private final PasswordEncoder enc;

    public AdminUserProvider(
            @Value("${tasca.auth.admin.email}") String email,
            @Value("${tasca.auth.admin.name}") String name,
            @Value("${tasca.auth.admin.password:}") String pwdPlain,
            @Value("${tasca.auth.admin.password-hash:}") String pwdHash,
            PasswordEncoder enc) {
        this.email = email; this.name = name; this.pwdPlain = pwdPlain; this.pwdHash = pwdHash; this.enc = enc;
    }

    public boolean matches(String inputEmail, String inputPassword) {
        if (inputEmail == null || inputPassword == null) return false;
        if (!inputEmail.equalsIgnoreCase(email)) return false;
        if (pwdHash != null && !pwdHash.isBlank()) return enc.matches(inputPassword, pwdHash);
        return inputPassword.equals(pwdPlain); // MVP
    }

    public String getEmail() { return email; }
    public String getName()  { return name; }
}