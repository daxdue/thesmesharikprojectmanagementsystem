package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.TaskStatus;
import com.smeshariks.pms.repositories.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskStatusServiceImpl implements TaskStatusService{

    private TaskStatusRepository taskStatusRepository;

    @Autowired
    public TaskStatusServiceImpl(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    public TaskStatus findTaskStatus(Integer id) {
        Optional<TaskStatus> taskStatus = taskStatusRepository.findById(id);
        return taskStatus.orElse(new TaskStatus());
    }

    public boolean saveTaskStatus(TaskStatus taskStatus) {
        Optional<TaskStatus> taskStatusDB = taskStatusRepository.findById(taskStatus.getId());

        if(taskStatusDB != null) {
            return false;
        }

        taskStatusRepository.save(taskStatus);
        return true;
    }

    public void updateTaskStatus(TaskStatus taskStatus) {
        if(taskStatus != null) {
            taskStatusRepository.save(taskStatus);
        }
    }

    public void deleteTaskStatus(Integer taskStatusId) {
        taskStatusRepository.deleteById(taskStatusId);
    }

    public List<TaskStatus> findAllTaskStatuses() {
        return taskStatusRepository.findAll();
    }
}

