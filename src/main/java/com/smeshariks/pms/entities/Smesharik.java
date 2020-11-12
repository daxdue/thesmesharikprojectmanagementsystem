package com.smeshariks.pms.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "smeshariks")
@Data
@NoArgsConstructor
public class Smesharik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NaturalId
    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "position_id", insertable = false, updatable = false)
    private ProjectRole projectRole;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "credentials_id", insertable = false, updatable = false)
    private SmesharikCredentials smesharikCredentials;

}
