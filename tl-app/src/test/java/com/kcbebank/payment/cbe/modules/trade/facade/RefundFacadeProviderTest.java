package com.kcbebank.payment.cbe.modules.trade.facade;
import java.math.BigDecimal;

import com.kcbebank.payment.cbe.common.constant.PreConstants;
import com.kcbebank.payment.cbe.common.util.ObjectMapperUtils;
import com.kcbebank.payment.cbe.modules.trade.domain.request.ReFundApplyReq;
import com.kcbebank.payment.cbe.modules.trade.enums.RefundTypeEnums;
import com.qihoo.finance.msf.common.domain.Response;
import com.qihoo.finance.msf.common.snowflake.service.SnowflakeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class RefundFacadeProviderTest {

    @Resource
    private SnowflakeService snowflakeService;

    private String userId = "721187472767188992";
    private String vaAcctNo = "99345679830310790";
    private String vaAcctName = "王晓东";
    private String vaAcctEnName = "englishName";

    private String origTradeId = "CBE733886551670390784";

    @Resource
    private RefundFacadeProvider refundFacadeProvider;

    @Test
    void refundApply() {

        ReFundApplyReq reFundApplyReq = new ReFundApplyReq();
        reFundApplyReq.setUserId(userId);
        reFundApplyReq.setPartnerTradeId(snowflakeService.generateId(PreConstants.SYSTEM_CODE));
        reFundApplyReq.setOrigTradeId(origTradeId);
        reFundApplyReq.setAccountNo(vaAcctNo);
        reFundApplyReq.setAccountName(vaAcctName);
        reFundApplyReq.setAccountEnName(vaAcctEnName);
        reFundApplyReq.setAmount(new BigDecimal("5"));
        reFundApplyReq.setRefundType(RefundTypeEnums.PART.getCode());
        reFundApplyReq.setReason("测试");
        reFundApplyReq.setRemark("备注");


        log.info("{}", ObjectMapperUtils.toJSON(reFundApplyReq));
        Response response = refundFacadeProvider.refundApply(reFundApplyReq, HeadTools.getHead());
        log.info("{}", ObjectMapperUtils.toJSON(response));

    }
}