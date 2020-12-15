package com.smeshariks.pms.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "task_statuses")
@Data
@NoArgsConstructor
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Timestamp timestamp;

    private Integer status;

    @JsonManagedReference
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "task_id")
    private Task task;

    /*
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    private Status status;
     */

    public String getStringValue() {

        String statusStr = "не известен";

        switch (status) {
            case 1:
                statusStr = "не утверждена";
                break;

            case 2:
                statusStr = "в работе";
                break;

            case 3:
                statusStr = "завершена";
                break;

            case 4:
                statusStr = "отклонена";

            case 5:
                statusStr = "ожидает";
                break;

            case 6:
                statusStr = "просрочена";
                break;

        }

        return statusStr;
    }

    public Statuses getStatusValue() {

        Statuses statuses = Statuses.UNKNOWN;

        switch (status) {
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

            case 5:
                statuses = Statuses.WAIT;
                break;

            case 6:
                statuses = Statuses.REJECTED;
                break;
        }
        return statuses;
    }
}
