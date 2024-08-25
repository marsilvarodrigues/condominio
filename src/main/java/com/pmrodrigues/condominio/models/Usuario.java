package com.pmrodrigues.condominio.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="usuarios")
@Getter
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid", unique = true, nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private String guid;

    @Column(name = "user_name", unique = true, length = 20, nullable = false)
    @Setter
    private String username;

    @Column(name = "password", nullable = false, length = 20)
    @Setter
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Setter(AccessLevel.PRIVATE)
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Setter(AccessLevel.PRIVATE)
    @LastModifiedDate
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @Column(name = "enable" , nullable = false)
    private boolean enabled = Boolean.TRUE;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "perfis_usuarios" ,
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id" , referencedColumnName = "id")
    )
    private Set<Perfil> perfis = new HashSet<>();

    @PrePersist
    public void preInsert() {
        this.password =  new BCryptPasswordEncoder().encode(this.password);
        this.createdDate = LocalDateTime.now();
        this.updatedDate = this.createdDate;
        this.guid = UUID.randomUUID().toString();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(perfis);
    }


    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

}
