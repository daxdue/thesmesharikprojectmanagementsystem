package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.Task;
import com.smeshariks.pms.entities.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Integer> {

    TaskStatus findTopByTask(Task task);
    TaskStatus findByTaskOrderByTimestampDesc(Task task);
}
