package com.smeshariks.pms.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NonNull
    @Size(min = 5, message = "Не менее 5 знаков")
    @Column(name = "username")
    private String username;

    @NonNull
    @Size(min = 8, message = "Не менее 8 знаков")
    @Column(name = "password")
    private String password;

    @NonNull
    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = { @JoinColumn(name = "user_id")},
            inverseJoinColumns = { @JoinColumn(name = "roles_id") }
    )
    private Set<Role> roles;


    @JsonBackReference
    @OneToMany(mappedBy = "owner")
    private List<Project> projects;

    @JsonBackReference
    @OneToMany(mappedBy = "executor")
    private List<Task> tasks;

    @Transient
    private UserRole userRole;

    public UserRole getUserRole() {
        if(roles != null) {

            if(!roles.isEmpty()) {
                for(Role r : roles) {

                    switch (r.getName()) {
                        case "ROLE_ADMIN":
                            userRole = UserRole.ADMIN;
                            break;

                        case "ROLE_CUSTOMER":
                            userRole = UserRole.CUSTOMER;
                            break;

                        case "ROLE_WORKER":
                            userRole = UserRole.WORKER;
                            break;

                        case "ROLE_WAREHOUSEMAN":
                            userRole = UserRole.WAREHOUSEMAN;
                            break;

                        case "ROLE_MANAGER":
                            userRole = UserRole.MANAGER;
                            break;

                    }
                }
            }
        }
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

}
