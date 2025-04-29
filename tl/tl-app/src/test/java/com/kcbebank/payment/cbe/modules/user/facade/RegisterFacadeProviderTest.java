package com.kcbebank.payment.cbe.modules.user.facade;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.kcbebank.payment.cbe.modules.common.domain.UnifyHead;
import com.kcbebank.payment.cbe.modules.user.domain.request.EnterpriseRegisterReq;
import com.kcbebank.payment.cbe.modules.user.domain.request.PersonalRegisterReq;
import com.kcbebank.payment.cbe.modules.user.domain.response.EnterpriseRegisterResp;
import com.kcbebank.payment.cbe.modules.user.domain.response.PersonalRegisterResp;
import com.kcbebank.payment.cbe.modules.user.enums.*;
import com.qihoo.finance.msf.common.domain.Response;
import com.qihoo.finance.msf.redis.lock.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: wangxiaodong
 * @create: 2024/1/13
 * Description:注册服务测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RegisterFacadeProviderTest {

    @Resource
    private PersonalRegisterFacadeProvider personalRegisterFacadeProvider;

    @Resource
    private EnterpriseRegisterFacadeProvider enterpriseRegisterFacadeProvider;

    @Resource
    private RedisLockService redisLockService;

    public static ThreadLocal<String> tl = new InheritableThreadLocal<>();



    @Test
    public void personalRegister() {
        UnifyHead head = new UnifyHead();
        head.setPartnerCode("LLG");
        head.setOutUserId("TEST_1006");
        head.setSequenceNo(UUID.fastUUID().toString());
        head.setTimestamp(String.valueOf(System.currentTimeMillis()));
        String LOCK_KEY = "CBE_PERSONAL_REG_LOCK_%S_%S";
        String key = String.format(LOCK_KEY, head.getPartnerCode(), head.getOutUserId());
        redisLockService.unLock(key);

        Response<PersonalRegisterResp> response = personalRegisterFacadeProvider.
                personalRegister(getPersonalRegisterReq(), head);
        log.info("个人注册结果:{}", JSONObject.toJSON(response));
    }

    @Test
    public void enterpriseRegister() {
        UnifyHead head = new UnifyHead();
        head.setPartnerCode("LLG");
        head.setOutUserId("TEST_2014");
        head.setSequenceNo(UUID.fastUUID().toString());
        head.setTimestamp(String.valueOf(System.currentTimeMillis()));
        String LOCK_KEY = "CBE_ENTERPRISE_REG_LOCK_%S_%S";
        String key = String.format(LOCK_KEY, head.getPartnerCode(), head.getOutUserId());
        redisLockService.unLock(key);

        Response<EnterpriseRegisterResp> response = enterpriseRegisterFacadeProvider.enterpriseRegister(
                getEnterpriseRegisterReq(), head);
        log.info("企业注册结果:{}", JSONObject.toJSON(response));
    }


    private static PersonalRegisterReq getPersonalRegisterReq() {
        PersonalRegisterReq req = new PersonalRegisterReq();

        req.setRealName("王晓东");
        req.setCertType(CertTypeEnum.RESIDENT.getCode());
        req.setCertNo("211282199502073039");
        req.setCardAddr("cbetest");
        req.setCardFrontFile("/front.png");
        req.setCardBackFile("/back.png");
        req.setContactInfo("18812345678");
        req.setCareerCode(CareerTypeEnum.ENTERPRISE_LEADER.getCode());
        req.setNationality(NationalityEnum.Nationality_organization.getCode());
        req.setSceneType(SceneTypeEnum.B2B.getCode());
        req.setVerifyYn("Y");
        req.setSignYn("Y");
        req.setEnName("englishName");
        req.setUserType("01");
        req.setCurrency("CNY");
        return req;
    }


    private static EnterpriseRegisterReq getEnterpriseRegisterReq() {
        EnterpriseRegisterReq req = new EnterpriseRegisterReq();

        EnterpriseRegisterReq.EnterpriseDto enterpriseDto = new EnterpriseRegisterReq.EnterpriseDto();
        enterpriseDto.setEntName("XXX测试公司");
        enterpriseDto.setCertType(CertTypeEnum.RESIDENT.getCode());
        enterpriseDto.setCertNo("410900100013231");
        enterpriseDto.setEntEnName("enterpriseName");
        enterpriseDto.setCertAddr("cbetest");
        enterpriseDto.setCertFileName("/bizLicense5.png");
        enterpriseDto.setCustContact("王晓东1");
        enterpriseDto.setCustTel("13898572362");
        enterpriseDto.setAddress("宝信大厦");


        EnterpriseRegisterReq.LegalPersonDto legalPersonDto = new EnterpriseRegisterReq.LegalPersonDto();
        legalPersonDto.setLegalPersonName("王晓东");
        legalPersonDto.setLegalTel("13898572361");
        legalPersonDto.setCardNo("211282199502073039");
        legalPersonDto.setCertType(CertTypeEnum.RESIDENT.getCode());
        legalPersonDto.setCardAddr("cbetest");
        legalPersonDto.setCardFrontFile("/front.png");
        legalPersonDto.setCardBackFile("/back.png");


        req.setCurrency("CNY");
        req.setSceneType(SceneTypeEnum.B2B.getCode());
        req.setVerifyYn("Y");
        req.setSignYn("Y");
        req.setUserType(UserTypeEnum.INDIVIDUAL_BUSINESS.getCode());
        req.setEntDto(enterpriseDto);
        req.setLegalPersonDto(legalPersonDto);
        return req;
    }
}
