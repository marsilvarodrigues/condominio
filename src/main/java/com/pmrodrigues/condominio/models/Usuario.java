package com.pmrodrigues.condominio.models;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="usuarios")
@Getter
@Inheritance(strategy=InheritanceType.JOINED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid", unique = true, nullable = false)
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode.Include
    @Builder.Default
    private String guid =  UUID.randomUUID().toString();

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
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Setter(AccessLevel.PRIVATE)
    @LastModifiedDate
    @Column(name = "updated_date", nullable = false)
    private Date updatedDate;

    @Column(name = "enable" , nullable = false)
    @Builder.Default
    private final boolean enabled = Boolean.TRUE;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Perfil.class)
    @JoinTable(name = "perfils_usuarios" ,
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id" , referencedColumnName = "id")
    )
    @Builder.Default
    private final Set<Perfil> perfis = new HashSet<>();

    @PrePersist
    @SneakyThrows
    public void preInsert() {

        if( StringUtils.isBlank(this.password) ){
            this.password = RandomStringUtils.randomAlphanumeric(10);
        }

        this.password =  new BCryptPasswordEncoder().encode(this.password);
        this.createdDate = new Date();
        this.updatedDate = this.createdDate;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = new Date();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(perfis);
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void adicionarPerfil(Collection<Perfil> perfis) {

        this.getPerfis().clear();
        this.perfis.addAll(perfis);
    }

}
