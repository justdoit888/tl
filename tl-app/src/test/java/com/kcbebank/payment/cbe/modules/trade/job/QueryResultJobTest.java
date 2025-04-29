package com.kcbebank.payment.cbe.modules.trade.job;

import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class QueryResultJobTest {

    @Resource
    private QueryResultJob queryResultJob;

    @Test
    void queryPayStatus() {
        ReturnT<String> stringReturnT = queryResultJob.queryPayStatus(null);
        log.info("{}", stringReturnT);
    }

    @Test
    void queryRemitStatus() {
        ReturnT<String> stringReturnT = queryResultJob.queryRemitStatus(null);
        log.info("{}", stringReturnT);
    }
}