package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Task;
import com.smeshariks.pms.entities.TaskReport;
import com.smeshariks.pms.entities.TaskStatus;
import com.smeshariks.pms.repositories.TaskReportRepository;
import com.smeshariks.pms.repositories.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskStatusServiceImpl implements TaskStatusService{

    private TaskStatusRepository taskStatusRepository;
    private TaskReportRepository taskReportRepository;

    @Autowired
    public TaskStatusServiceImpl(TaskStatusRepository taskStatusRepository, TaskReportRepository taskReportRepository) {
        this.taskStatusRepository = taskStatusRepository;
        this.taskReportRepository = taskReportRepository;
    }

    public TaskStatus findTaskStatus(Integer id) {
        Optional<TaskStatus> taskStatus = taskStatusRepository.findById(id);
        return taskStatus.orElse(new TaskStatus());
    }

    public boolean saveTaskStatus(TaskStatus taskStatus) {
        /*Optional<TaskStatus> taskStatusDB = taskStatusRepository.findById(taskStatus.getId());

        if(taskStatusDB != null) {
            return false;
        }
        */
        taskStatusRepository.save(taskStatus);
        return true;
    }

    public boolean saveTaskReport(TaskReport taskReport) {
        taskReportRepository.save(taskReport);
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

    public TaskStatus findLastStatus(Task task) {
        return taskStatusRepository.findByTaskOrderByTimestampDesc(task);
    }
}

