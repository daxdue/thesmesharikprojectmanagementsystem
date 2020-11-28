package com.smeshariks.pms.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "statuses")
@Data
@NoArgsConstructor
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;


    public Statuses get() {
        Statuses statuses = Statuses.UNKNOWN;
        switch (id) {
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
            case 5:
                statuses = Statuses.WAIT;
                break;
            case 6:
                statuses = Statuses.EXPIRED;
                break;
        }

        return statuses;
    }
}
