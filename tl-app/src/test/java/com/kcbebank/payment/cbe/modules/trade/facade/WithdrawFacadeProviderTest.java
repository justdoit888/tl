package com.kcbebank.payment.cbe.modules.trade.facade;
import java.math.BigDecimal;

import com.kcbebank.payment.cbe.common.constant.PreConstants;
import com.kcbebank.payment.cbe.common.util.ObjectMapperUtils;
import com.kcbebank.payment.cbe.modules.trade.domain.request.WithdrawApplyReq;
import com.kcbebank.payment.cbe.modules.trade.enums.BankAccountType;
import com.kcbebank.payment.cbe.modules.user.enums.CertTypeEnum;
import com.kcbebank.payment.cbe.modules.user.enums.CurrencyEnum;
import com.qihoo.finance.msf.common.domain.Response;
import com.qihoo.finance.msf.common.snowflake.service.SnowflakeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
class WithdrawFacadeProviderTest {

    @Resource
    private SnowflakeService snowflakeService;

    private String userId = "721187472767188992";
    private String vaAcctNo = "99345679830310790";
    private String vaAcctName = "王晓东";
    private String vaAcctEnName = "englishName";

    @Resource
    private WithdrawFacadeProvider withdrawFacadeProvider;

    @Test
    void withdrawApply() throws Exception {

        WithdrawApplyReq withdrawApplyReq = new WithdrawApplyReq();
        withdrawApplyReq.setUserId(userId);
        withdrawApplyReq.setPartnerTradeId(snowflakeService.generateId(PreConstants.SYSTEM_CODE));
        withdrawApplyReq.setAccountNo(vaAcctNo);
        withdrawApplyReq.setAccountName(vaAcctName);
        withdrawApplyReq.setAccountEnName(vaAcctEnName);
        withdrawApplyReq.setReceAcctNo("62260900000000481");
        withdrawApplyReq.setReceAcctName("王晓东");
        withdrawApplyReq.setAmount(new BigDecimal("10"));
        withdrawApplyReq.setCurrency(CurrencyEnum.CNY.getCode());
        withdrawApplyReq.setReceAcctType(BankAccountType.PERSON.getCode());
        withdrawApplyReq.setReceCertType(CertTypeEnum.RESIDENT.getCode());
        withdrawApplyReq.setReceCertNo("111111222233332222");
        withdrawApplyReq.setReceTel("12222222222");
        withdrawApplyReq.setReceBankName(null);
        withdrawApplyReq.setReceBankCode(null);
        withdrawApplyReq.setVerifyYn("Y");
        withdrawApplyReq.setRemark("这个一个提现测试");



        log.info("{}", ObjectMapperUtils.toJSON(withdrawApplyReq));
        Response response = withdrawFacadeProvider.withdrawApply(withdrawApplyReq, HeadTools.getHead());
        log.info("{}", ObjectMapperUtils.toJSON(response));

    }
}