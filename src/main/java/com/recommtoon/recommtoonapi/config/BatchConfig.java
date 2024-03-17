package com.recommtoon.recommtoonapi.config;

//import com.recommtoon.recommtoonapi.crawler.tasklet.WebtoonCrawlingTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
//@RequiredArgsConstructor
//public class BatchConfig {
//
//    private final WebtoonCrawlingTasklet webtoonCrawlingTasklet;
//
//    @Bean
//    public Job webtoonCrawlingJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new JobBuilder("webtoonCrawlingJob", jobRepository)
//                .start(crawlingStep(jobRepository, transactionManager))
//                .build();
//    }
//
//    @Bean
//    public Step crawlingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("webtoonCrawlingStep", jobRepository)
//                .tasklet(webtoonCrawlingTasklet, transactionManager)
//                .build();
//    }
//}
