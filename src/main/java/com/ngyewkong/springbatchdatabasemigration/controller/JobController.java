package com.ngyewkong.springbatchdatabasemigration.controller;

import com.ngyewkong.springbatchdatabasemigration.request.JobParamsRequest;
import com.ngyewkong.springbatchdatabasemigration.service.MigrationJobService;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/job/")
public class JobController {

    // autowired Migration Job Service
    @Autowired
    private MigrationJobService migrationJobService;

    // GetMapping with route /api/v1/job/start/{jobName}
    // {jobName} is being accessed using @PathVariable annotation jobName will be populated by {jobName}
    // @RequestBody will pass the json body into the List<JobParamsRequest>
    @GetMapping("start/{jobName}")
    public String startJob(@PathVariable String jobName,
                           @RequestBody(required = false) List<JobParamsRequest> jobParamsList) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        migrationJobService.startJob(jobName, jobParamsList);

        return jobName + " started...";
    }

    // Stop Job
    // need to use JobOperator object to stop
    // .stop() method takes in job executionId
    @Autowired
    private JobOperator jobOperator;

    @GetMapping("/stop/{jobExecutionId}")
    public String stopJob(@PathVariable long jobExecutionId) throws NoSuchJobExecutionException, JobExecutionNotRunningException {
        jobOperator.stop(jobExecutionId);

        return "Job Stopped...";
    }
}
