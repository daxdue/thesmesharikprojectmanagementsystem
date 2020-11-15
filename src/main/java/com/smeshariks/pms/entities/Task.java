package com.smeshariks.pms.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "timings_id")
    private Timings timings;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;


}
