package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.Task;
import com.smeshariks.pms.entities.User;
import com.smeshariks.pms.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task findTask(Integer id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElse(new Task());
    }

    public boolean saveTask(Task task) {

        taskRepository.save(task);
        return true;
    }

    public void updateTask(Task task) {

        if(task != null) {
            taskRepository.save(task);
        }
    }

    public void deleteTask(Integer taskId) {
        taskRepository.deleteById(taskId);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> findTasksByProject(Project project) {
       return taskRepository.findTasksByProjectOrderByStartTimeDesc(project);
    }

    public List<Task> findTasksByExecutor(User user) {
        return taskRepository.findTasksByExecutor(user);
    }

}
