package com.ra.inventory_management.model.entity;

import com.ra.inventory_management.common.ERoles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Table(name = "user_google")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserGoogle implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 200)
    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @Column(name = "avatar")
    private String avatar;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "delete_fg")
    private Boolean deleteFg;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Roles role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getMenus().stream()
                .map(m -> new SimpleGrantedAuthority(m.getCode()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return "";
    }
}