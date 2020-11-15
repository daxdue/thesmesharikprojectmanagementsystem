package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Task;
import java.util.List;

public interface TaskService {

    Task findTask(Integer id);
    boolean saveTask(Task task);
    void updateTask(Task task);
    void deleteTask(Integer projectId);
    List<Task> findAllTasks();
}
