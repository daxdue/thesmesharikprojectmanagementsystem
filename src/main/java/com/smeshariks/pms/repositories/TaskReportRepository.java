package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.TaskReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskReportRepository extends JpaRepository<TaskReport, Integer> {
}
