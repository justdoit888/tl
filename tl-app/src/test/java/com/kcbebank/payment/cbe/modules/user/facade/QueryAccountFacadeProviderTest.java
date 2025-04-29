package com.kcbebank.payment.cbe.modules.user.facade;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.kcbebank.payment.cbe.modules.common.domain.UnifyHead;
import com.kcbebank.payment.cbe.modules.user.domain.request.QueryAccountInfoReq;
import com.kcbebank.payment.cbe.modules.user.domain.request.QueryNraAccountInfoReq;
import com.kcbebank.payment.cbe.modules.user.domain.response.QueryAccountInfoResp;
import com.kcbebank.payment.cbe.modules.user.domain.response.QueryNraAccountInfoResp;
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
public class QueryAccountFacadeProviderTest {

    @Resource
    private QueryAccountFacadeProvider queryAccountFacadeProvider;


    @Test
    public void queryAccountInfo() {
        UnifyHead head = getUnifyHead();
        QueryAccountInfoReq queryAccountInfoReq = new QueryAccountInfoReq();
        queryAccountInfoReq.setAccountNo("99345679830285752");

        Response<QueryAccountInfoResp> response = queryAccountFacadeProvider.queryAccountInfo(queryAccountInfoReq, head);
        log.info("个人信息查询结果:{}", JSONObject.toJSON(response));
    }


    @Test
    public void queryNraAccountInfo() {
        QueryNraAccountInfoReq req = new QueryNraAccountInfoReq();
        req.setAccountNo("99345679830385560");

        Response<QueryNraAccountInfoResp> response = queryAccountFacadeProvider.queryNraAccountInfo(req, getUnifyHead());
        log.info("个人信息查询结果:{}", JSONObject.toJSON(response));
    }




    private UnifyHead getUnifyHead() {
        UnifyHead head = new UnifyHead();
        head.setPartnerCode("LLG");
        head.setOutUserId("TEST_2000");
        head.setSequenceNo(UUID.fastUUID().toString());
        head.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return head;
    }


    public static void main(String[] args) {
        QueryNraAccountInfoReq req = new QueryNraAccountInfoReq();
        req.setAccountNo("99345679830385560");
        log.info("NAR账户查询：{}", JSONObject.toJSON(req));

        QueryAccountInfoReq queryAccountInfoReq = new QueryAccountInfoReq();
        queryAccountInfoReq.setAccountNo("99345679830285752");
        log.info("账户查询：{}", JSONObject.toJSON(queryAccountInfoReq));
    }

}
