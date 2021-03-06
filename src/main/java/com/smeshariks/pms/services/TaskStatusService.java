package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Task;
import com.smeshariks.pms.entities.TaskReport;
import com.smeshariks.pms.entities.TaskStatus;

import java.util.List;

public interface TaskStatusService {

    TaskStatus findTaskStatus(Integer id);
    boolean saveTaskStatus(TaskStatus taskStatus);
    boolean saveTaskReport(TaskReport taskReport);
    void updateTaskStatus(TaskStatus taskStatus);
    void deleteTaskStatus(Integer taskStatusId);
    List<TaskStatus> findAllTaskStatuses();
    TaskStatus findLastStatus(Task task);
}
