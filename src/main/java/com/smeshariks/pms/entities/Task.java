package com.smeshariks.pms.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.smeshariks.pms.utils.TimestampConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "dead_time")
    private Timestamp deadTime;

    @Transient
    private String startTimeStr;

    @Transient
    private String deadTimeStr;

    @Transient
    private Integer executorId;

    @Transient
    private Integer projectId;

    /*
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "timings_id")
    private Timings timings;
    */
    @JsonBackReference
    @OneToMany(mappedBy = "task")
    @OrderBy("timestamp DESC")
    private List<TaskStatus> statuses;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "executor_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User executor;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "project_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Project project;

    public String getStart() {
        return TimestampConverter.convert(startTime, false);
    }

    public String getDeadline() {
        return TimestampConverter.convert(deadTime, false);
    }

    public TaskStatus getLastStatus() {

        TaskStatus taskStatus = new TaskStatus();
        if(statuses != null) {
            taskStatus = statuses.get(0);
        }

        return taskStatus;
    }
}
