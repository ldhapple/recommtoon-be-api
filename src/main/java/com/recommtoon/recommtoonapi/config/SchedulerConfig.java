package com.recommtoon.recommtoonapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final JobLauncher jobLauncher;
    private final Job webtoonCrawlingJob;

    @Scheduled(cron = "0 0 4 1 * ?")
    public void runNewWebtoonCrawlingJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("WebtoonCrawlingJob", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(webtoonCrawlingJob, params);
    }
}
