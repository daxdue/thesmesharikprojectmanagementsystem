package com.smeshariks.pms.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor

public class Role implements GrantedAuthority {

    @Id
    private Integer id;
    private String name;

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @OneToMany(mappedBy = "role")
    private List<User> users;
    /*
    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
    */

    @Override
    public String getAuthority() {
        return getName();
    }

}
