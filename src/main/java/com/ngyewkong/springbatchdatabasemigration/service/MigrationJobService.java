package com.ngyewkong.springbatchdatabasemigration.service;

import com.ngyewkong.springbatchdatabasemigration.request.JobParamsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Async
@Slf4j
public class MigrationJobService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("studentMigrationJob")
    private Job studentMigrationJob;

    @Autowired
    @Qualifier("subjectMigrationJob")
    private Job subjectMigrationJob;

    public void startJob(String jobName, List<JobParamsRequest> jobParamsRequestList) {
        // job parameters setup
        // Map with <key, value> whereby value is JobParameter type
        // setting params with current time in millisec -> jobInstance will be unique every single time
        Map<String, JobParameter> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis()));

        // use the jobParamsList that is passed in
        // add it into the params HashMap which store as key-value pairs
        // rmb jobParamsRequest.getParamValue returns a String but params has type JobParameter
        // initialize new JobParameter to recast to JobParameter type
        if (jobParamsRequestList != null) {
            jobParamsRequestList.stream()
                    .forEach(jobParamsRequest -> {
                        params.put(jobParamsRequest.getParamKey(),
                                new JobParameter(jobParamsRequest.getParamValue()));
                    });
        }

        // JobParameters take in a HashMap of keyvalue pair
        JobParameters jobParameters = new JobParameters(params);

        try {
            JobExecution jobExecution = null;
            // does a check with the name provided against student
            if (jobName.equals("student")) {
                // jobLauncher.run() returns JobExecution class which we can use to get JobId etc
                // jobLauncher takes in 2 arguments - jobObject & jobParameters
                jobExecution = jobLauncher.run(studentMigrationJob, jobParameters);
            } else if (jobName.equals("subject")) {
                // jobLauncher takes in 2 arguments - jobObject & jobParameters
                jobExecution = jobLauncher.run(subjectMigrationJob, jobParameters);
            }
            log.info("Job Instance ID = " + jobExecution.getJobId());
        } catch (Exception e) {
            log.info("Exception while starting Job");
        }

    }


}
