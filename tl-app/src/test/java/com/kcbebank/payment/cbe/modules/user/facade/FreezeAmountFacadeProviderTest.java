package com.kcbebank.payment.cbe.modules.user.facade;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.kcbebank.payment.cbe.modules.common.domain.UnifyHead;
import com.kcbebank.payment.cbe.modules.user.domain.request.AmountFreezeReq;
import com.kcbebank.payment.cbe.modules.user.domain.request.AmountUnfreezeReq;
import com.kcbebank.payment.cbe.modules.user.domain.response.AmountFreezeResp;
import com.kcbebank.payment.cbe.modules.user.domain.response.AmountUnfreezeResp;
import com.kcbebank.payment.cbe.modules.user.enums.FreezeSceneTypeEnum;
import com.kcbebank.payment.cbe.modules.user.enums.RestrictSourceEnum;
import com.kcbebank.payment.cbe.modules.user.enums.UnfreezeTypeEnum;
import com.qihoo.finance.msf.common.domain.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author: wangxiaodong
 * @create: 2024/1/13
 * Description:查询服务测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class FreezeAmountFacadeProviderTest {

    @Resource
    private FreezeAmountFacadeProvider freezeAccount;

    /**
     * 金额冻结
     */
    @Test
    public void freeze() {
        AmountFreezeReq amountFreezeReq = new AmountFreezeReq();
        amountFreezeReq.setFreezeAmount("50.01");
        amountFreezeReq.setFreezeScene(FreezeSceneTypeEnum.ACCOUNT_EXCEPTION.getCode());

        String freezeNo = UUID.randomUUID().toString();
        log.info("------------------------------------冻结流水: {}", freezeNo);
        amountFreezeReq.setFreezeNo(freezeNo);
        amountFreezeReq.setRemark("111");
        amountFreezeReq.setAccountNo("99345679830385560");
        amountFreezeReq.setUserId("722732091942371328");
        amountFreezeReq.setRestrictSource(RestrictSourceEnum.PARTNER.getCode());
        amountFreezeReq.setAccountName("王晓东");

        Response<AmountFreezeResp> response = freezeAccount.freezeAmount(amountFreezeReq, getUnifyHead());
        log.info("账户冻结结果:{}", JSONObject.toJSON(response));
    }


    /**
     * 账户解冻
     */
    @Test
    public void unfreeze() {
        AmountUnfreezeReq req = new AmountUnfreezeReq();
        req.setOriFreezeNo("4c09ce2d-9e7d-4bdb-aa1d-246ff0115f24");//todo 每次变更
        req.setAccountNo("99345679830385560");
        req.setUserId("722732091942371328");
        req.setUnfreezeAmount("50.01");
        req.setAccountName("王晓东");


        req.setUnfreezeNo(UUID.randomUUID().toString());
        req.setUnfreezeType(UnfreezeTypeEnum.ALL.getCode());
        req.setRemark("111");
        req.setRestrictSource(RestrictSourceEnum.PARTNER.getCode());
        Response<AmountUnfreezeResp> response = freezeAccount.unfreezeAmount(req, getUnifyHead());
        log.info("账户解冻结果:{}", JSONObject.toJSON(response));
    }


    private UnifyHead getUnifyHead() {
        UnifyHead head = new UnifyHead();
        head.setPartnerCode("LLG");
        head.setOutUserId("TEST_20010");
        head.setSequenceNo(UUID.fastUUID().toString());
        head.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return head;
    }

    public static void main(String[] args) {
        AmountUnfreezeReq req = new AmountUnfreezeReq();
        req.setOriFreezeNo("4c09ce2d-9e7d-4bdb-aa1d-246ff0115f24");//todo 每次变更
        req.setAccountNo("99345679830385560");
        req.setUserId("722732091942371328");
        req.setUnfreezeAmount("50.01");
        req.setAccountName("王晓东");
        req.setUnfreezeNo(UUID.randomUUID().toString());
        req.setUnfreezeType(UnfreezeTypeEnum.ALL.getCode());
        req.setRemark("111");
        req.setRestrictSource(RestrictSourceEnum.PARTNER.getCode());
        log.info("金额解冻：{}", JSONObject.toJSON(req));


        AmountFreezeReq amountFreezeReq = new AmountFreezeReq();
        amountFreezeReq.setFreezeAmount("50.01");
        amountFreezeReq.setFreezeScene(FreezeSceneTypeEnum.ACCOUNT_EXCEPTION.getCode());

        String freezeNo = UUID.randomUUID().toString();
        amountFreezeReq.setFreezeNo(freezeNo);
        amountFreezeReq.setRemark("111");
        amountFreezeReq.setAccountNo("99345679830385560");
        amountFreezeReq.setUserId("722732091942371328");
        amountFreezeReq.setRestrictSource(RestrictSourceEnum.PARTNER.getCode());
        amountFreezeReq.setAccountName("王晓东");
        log.info("金额冻结：{}", JSONObject.toJSON((amountFreezeReq)));
    }

}
