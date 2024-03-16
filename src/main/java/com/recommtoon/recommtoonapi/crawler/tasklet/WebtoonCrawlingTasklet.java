package com.recommtoon.recommtoonapi.crawler.tasklet;

import com.recommtoon.recommtoonapi.crawler.CrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class WebtoonCrawlingTasklet implements Tasklet {
//
//    private final CrawlerService crawlerService;
//
//    @Override
//    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//        crawlerService.newWebtoonCrawling();
//        return RepeatStatus.FINISHED;
//    }
//}
