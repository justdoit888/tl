package com.kcbebank.payment.cbe.modules.trade.controller;
import java.math.BigDecimal;

import com.kcbebank.payment.cbe.common.constant.PreConstants;
import com.kcbebank.payment.cbe.common.util.ObjectMapperUtils;

import com.kcbebank.payment.cbe.modules.trade.domain.request.FundApplyReq;
import com.kcbebank.payment.cbe.modules.trade.domain.request.FundCheckReq;
import com.kcbebank.payment.cbe.modules.trade.domain.response.FundApplyResp;
import com.kcbebank.payment.cbe.modules.trade.domain.response.FundCheckResp;
import com.kcbebank.payment.cbe.modules.trade.domain.vo.SwiftReport;
import com.kcbebank.payment.cbe.modules.user.enums.CurrencyEnum;
import com.qihoo.finance.msf.common.domain.Response;
import com.qihoo.finance.msf.common.snowflake.service.SnowflakeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
class FundControllerTest {

    @Resource
    private FundController fundController;

    @Resource
    private SnowflakeService snowflakeService;

    private String vaAcctNo = "99345679830310790";
    private String vaAcctEnName = "englishName";//""wangxiaodong";

    @Test
    void check() {

        FundCheckReq fundCheckReq = new FundCheckReq();
        fundCheckReq.setSequenceNo(snowflakeService.generateId(PreConstants.SYSTEM_CODE));
        fundCheckReq.setReceAcctNo(vaAcctNo);
        fundCheckReq.setReceAcctName(vaAcctEnName);
        fundCheckReq.setPayerAcctNo("fffff");
        fundCheckReq.setPayerAcctName("sssss");
        fundCheckReq.setAmount(new BigDecimal("10"));
        fundCheckReq.setCurrency(CurrencyEnum.CNY.getCode());

        log.info("{}", ObjectMapperUtils.toJSON(fundCheckReq));
        Response<FundCheckResp> checkRes = fundController.check(fundCheckReq);
        log.info("{}", ObjectMapperUtils.toJSON(checkRes));
    }

    @Test
    void apply() {

        FundApplyReq fundApplyReq = new FundApplyReq();
        fundApplyReq.setSequenceNo(snowflakeService.generateId(PreConstants.SYSTEM_CODE));
        fundApplyReq.setPartnerTradeId(snowflakeService.generateId("EBP"));
        fundApplyReq.setSwiftDate("20231230");
        fundApplyReq.setReportDate("20231231");
        fundApplyReq.setPayerAcctNo("ffff");
        fundApplyReq.setPayerAcctName("ssss");
        fundApplyReq.setPayerCountryCode("SGP");
        fundApplyReq.setPayerBankCode("dfs");
        fundApplyReq.setPayerBankCountryCode("CCC");
        fundApplyReq.setReceAcctNo(vaAcctNo);
        fundApplyReq.setReceAcctName(vaAcctEnName);
        fundApplyReq.setReceBankCode("KCB");
        fundApplyReq.setReceCountryCode("CN");
        fundApplyReq.setAmount(new BigDecimal("10"));
        fundApplyReq.setCurrency(CurrencyEnum.CNY.getCode());
        fundApplyReq.setRemark("这是一个入金测试");
        fundApplyReq.setSwiftReport(SwiftReport.builder()
                .senderReference("CN")
                        .bankOperationCode("A")
                        .valueDate("232524")
                        .currency("CNY")
                        .interbankSettledAmount("10")
                        .orderingCustomerNo("66666666666")
                        .orderingCustomerName("customer")
                        .orderingCustomerAddress("customer address")
                        .sendingInstitution("customer bank address name")
                        .beneficiaryCustomerNo("7777777777")
                        .beneficiaryCustomerName("customer 7777777")
                        .beneficiaryCustomerAddress("customer 777777777 address ")
                        .detailsOfCharges("-")
                .build());

        log.info("{}", ObjectMapperUtils.toJSON(fundApplyReq));
        Response<FundApplyResp> apply = fundController.apply(fundApplyReq);
        log.info("{}", apply);

    }
}