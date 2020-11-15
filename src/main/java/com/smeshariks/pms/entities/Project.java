package com.smeshariks.pms.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private Double cost;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "dead_time")
    private Timestamp deadTime;

    @Transient
    private String tsStart;

    @Transient
    private String tsStop;

    /*
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "timings_id", referencedColumnName = "id")
    private Timings timings;
    */
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User owner;

    @JsonBackReference
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Task> tasks;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    private List<ProjectStatus> statuses;


}
