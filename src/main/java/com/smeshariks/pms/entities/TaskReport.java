package com.smeshariks.pms.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "task_reports")
@Data
@NoArgsConstructor
public class TaskReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String report;

    @Transient
    private Integer task_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private Task task;
}
