package com.kcbebank.payment.cbe.modules.trade.facade;

import com.kcbebank.payment.cbe.modules.trade.domain.request.TradeQueryReq;
import com.qihoo.finance.msf.common.domain.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
class QueryFacadeProviderTest {

    @Resource
    private QueryFacadeProvider queryFacadeProvider;

    private String userId = "721187472767188992";
    private String tradeId = "CBE722289972719124480";
    private String partnerTradeId = "EBP722289970965905409";

    @Test
    void queryByTradeId() {
        TradeQueryReq tradeQueryReq = new TradeQueryReq();
        tradeQueryReq.setUserId(userId);
        tradeQueryReq.setPartnerTradeId(partnerTradeId);
        tradeQueryReq.setTradeId(tradeId);
        Response query = queryFacadeProvider.query(tradeQueryReq, HeadTools.getHead());
        log.info("{}", query);
    }

    @Test
    void queryByPartnerTradeId() {
        TradeQueryReq tradeQueryReq = TradeQueryReq.builder()
                .userId(userId)
                .partnerTradeId(partnerTradeId)
                .build();
        Response query = queryFacadeProvider.query(tradeQueryReq, HeadTools.getHead());
        log.info("{}", query);
    }

    @Test
    void query() {
        TradeQueryReq tradeQueryReq = TradeQueryReq.builder()
                .userId(userId)
                .partnerTradeId(partnerTradeId)
                .tradeId(tradeId)
                .build();
        Response query = queryFacadeProvider.query(tradeQueryReq, HeadTools.getHead());
        log.info("{}", query);
    }


}