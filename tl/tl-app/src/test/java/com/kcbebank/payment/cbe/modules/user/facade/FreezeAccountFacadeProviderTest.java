package com.kcbebank.payment.cbe.modules.user.facade;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.kcbebank.payment.cbe.modules.common.domain.UnifyHead;
import com.kcbebank.payment.cbe.modules.common.domain.UnifyReq;
import com.kcbebank.payment.cbe.modules.user.domain.request.AccountFreezeReq;
import com.kcbebank.payment.cbe.modules.user.domain.request.AccountUnfreezeReq;
import com.kcbebank.payment.cbe.modules.user.domain.request.EnterpriseRegisterReq;
import com.kcbebank.payment.cbe.modules.user.domain.request.PersonalRegisterReq;
import com.kcbebank.payment.cbe.modules.user.domain.response.AccountFreezeResp;
import com.kcbebank.payment.cbe.modules.user.domain.response.AccountUnfreezeResp;
import com.kcbebank.payment.cbe.modules.user.enums.FreezeSceneTypeEnum;
import com.kcbebank.payment.cbe.modules.user.enums.RestrictSourceEnum;
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
public class FreezeAccountFacadeProviderTest {

    @Resource
    private FreezeAccountFacadeProvider freezeFacadeProvider;

    /**
     * 账户冻结
     */
    @Test
    public void freeze() {
        AccountFreezeReq accountFreezeReq = new AccountFreezeReq();
        accountFreezeReq.setFreezeNo(UUID.randomUUID().toString());

        accountFreezeReq.setFreezeScene(FreezeSceneTypeEnum.ACCOUNT_EXCEPTION.getCode());
        accountFreezeReq.setAccountNo("99345679830385560");
        accountFreezeReq.setUserId("722732091942371328");
        accountFreezeReq.setRestrictSource(RestrictSourceEnum.KIN_CHENG.getCode());
        accountFreezeReq.setAccountName("XXX测试公司");


        Response<AccountFreezeResp> response = freezeFacadeProvider.freezeAccount(accountFreezeReq, getUnifyHead());
        log.info("账户冻结结果:{}", JSONObject.toJSON(response));
    }


    public static void main(String[] args) {
        AccountFreezeReq accountFreezeReq = new AccountFreezeReq();
        accountFreezeReq.setFreezeNo(UUID.randomUUID().toString());

        accountFreezeReq.setFreezeScene(FreezeSceneTypeEnum.ACCOUNT_CANCEL_EXCEPTION.getCode());
        accountFreezeReq.setAccountNo("99345679830385560");
        accountFreezeReq.setUserId("722732091942371328");
        accountFreezeReq.setRestrictSource(RestrictSourceEnum.PARTNER.getCode());
        accountFreezeReq.setAccountName("XXX测试公司");

        log.info("账户冻结：{}", JSONObject.toJSON(accountFreezeReq));
    }

    /**
     * 账户解冻
     */
    @Test
    public void unfreeze() {
        AccountUnfreezeReq req = new AccountUnfreezeReq();

        req.setFreezeScene(FreezeSceneTypeEnum.ACCOUNT_EXCEPTION.getCode());
        req.setAccountNo("99345679830385560");
        req.setUserId("722732091942371328");
        req.setUnfreezeNo(UUID.randomUUID().toString());
        req.setRestrictSource(RestrictSourceEnum.PARTNER.getCode());
        req.setAccountName("XXX测试公司");

        Response<AccountUnfreezeResp> response = freezeFacadeProvider.unfreezeAccount(req, getUnifyHead());
        log.info("账户解冻结果:{}", JSONObject.toJSON(response));
    }


    private UnifyHead getUnifyHead() {
        UnifyHead head = new UnifyHead();
        head.setPartnerCode("LLG");
        head.setOutUserId("TEST_2000");

        head.setSequenceNo(UUID.fastUUID().toString());
        head.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return head;
    }

}
