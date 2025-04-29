package com.kcbebank.payment.cbe.modules.trade.service.impl;

import com.kcbebank.payment.cbe.modules.trade.service.CallbackService;
import com.kcbebank.payment.cbe.modules.trade.service.TradeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CallbackServiceImplTest {

    @Resource
    public TradeService tradeService;
    @Resource
    public CallbackService callbackService;

    @Test
    void callbackTrade() {
        callbackService.callbackTradeAsync(tradeService.getTradeByTradeId("LLG","CBE724892475499491328","01"));
        callbackService.callbackTradeAsync(tradeService.getTradeByTradeId("LLG","CBE725167087827288064","02"));
        callbackService.callbackTradeAsync(tradeService.getTradeByTradeId("LLG","CBE725167056734912512","03"));
    }

    @Test
    void callbackAsync() {
    }
}