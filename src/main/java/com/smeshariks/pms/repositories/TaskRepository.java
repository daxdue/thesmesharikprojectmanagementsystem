package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.Task;
import com.smeshariks.pms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {

    List<Task> findTasksByProjectOrderByStartTimeDesc(Project project);
    List<Task> findTasksByExecutor(User user);
}
