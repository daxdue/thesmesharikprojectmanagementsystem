package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.Task;
import com.smeshariks.pms.entities.User;

import java.util.List;

public interface TaskService {

    Task findTask(Integer id);
    boolean saveTask(Task task);
    void updateTask(Task task);
    void deleteTask(Integer projectId);
    List<Task> findAllTasks();
    List<Task> findTasksByProject(Project project);
    List<Task> findTasksByExecutor(User user);
}
