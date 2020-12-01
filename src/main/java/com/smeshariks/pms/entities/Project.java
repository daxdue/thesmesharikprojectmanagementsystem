package com.smeshariks.pms.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.smeshariks.pms.utils.TimestampConverter;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    @Column(name = "added")
    private Timestamp added;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "dead_time")
    private Timestamp deadTime;


    @Column(name = "current_status", nullable = false)
    @Getter
    private Integer currentStatus;

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
    @OneToMany(mappedBy = "project")
    @OrderBy("start_time DESC")
    private List<Task> tasks;


    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    private List<ProjectStatus> statuses;

    public String getStartTimestamp() {
        return TimestampConverter.convert(startTime, false);
    }

    public String getDeadTimestamp() {
        return TimestampConverter.convert(deadTime, false);
    }

    public String getAddedTimestamp() {
        return TimestampConverter.convert(added, false);
    }

    public int getActualConst() {
        return (cost.intValue());
    }

    public Statuses getStatus() {

        Statuses statuses = Statuses.UNKNOWN;

        switch (currentStatus) {
            case 1:
                statuses = Statuses.NOT_APPROVED;
                break;

            case 2:
                statuses = Statuses.IN_WORK;
                break;

            case 3:
                statuses = Statuses.COMPLETED;
                break;

            case 4:
                statuses = Statuses.REJECTED;
                break;
        }

        return statuses;
    }

    public void convertToStringTimestamp() {
        if((startTime != null) && (deadTime != null)) {
            tsStart = getStartTimestamp();
            tsStop = getDeadTimestamp();
        }
    }


    public int calculateProgress() {

        double allTasks = 0;
        double completed = 0;
        double progress = 0;

        if(tasks != null) {
            allTasks = tasks.size();
            for(Task task : tasks) {
                if(task.getStatuses() != null) {
                    if(task.getLastStatus().getStatus() == Statuses.COMPLETED.getValue()) {
                        completed++;
                    }
                }
            }

        }


        if((allTasks != 0) && (completed != 0)) {
            progress = (completed / allTasks) * 100;
        }

        return (int)progress;
    }
}
