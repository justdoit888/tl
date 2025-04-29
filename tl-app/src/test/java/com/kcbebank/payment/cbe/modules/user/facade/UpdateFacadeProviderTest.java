package com.kcbebank.payment.cbe.modules.user.facade;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.kcbebank.payment.cbe.modules.common.domain.UnifyHead;
import com.kcbebank.payment.cbe.modules.user.domain.request.EnterpriseUpdateReq;
import com.kcbebank.payment.cbe.modules.user.domain.request.PersonalUpdateReq;
import com.kcbebank.payment.cbe.modules.user.domain.response.EnterpriseUpdateResp;
import com.kcbebank.payment.cbe.modules.user.domain.response.PersonalUpdateResp;
import com.kcbebank.payment.cbe.modules.user.enums.CertTypeEnum;
import com.kcbebank.payment.cbe.modules.user.enums.ChangeTypeEnum;
import com.kcbebank.payment.cbe.modules.user.enums.SceneTypeEnum;
import com.qihoo.finance.msf.common.domain.Response;
import com.qihoo.finance.msf.redis.lock.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author: wangxiaodong
 * @create: 2024/1/13
 * Description:更新服务测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class UpdateFacadeProviderTest {

    @Resource
    private PersonalUpdateFacadeProvider updateFacadeProvider;

    @Resource
    private EnterpriseUpdateFacadeProvider enterpriseUpdateFacadeProvider;

    @Resource
    private RedisLockService redisLockService;

    private static final String LOCK_KEY = "CBE_PERSONAL_UPDATE_LOCK_%S_%S";


    private static final String ENT_LOCK_KEY = "CBE_ENTERPRISE_UPDATE_LOCK_%S_%S";


    @Test
    public void personalUpdate() {
        PersonalUpdateReq req = getPersonalUpdateReq();

        UnifyHead head = new UnifyHead();
        head.setPartnerCode("LLG");
        head.setOutUserId("TEST_1000");
        head.setSequenceNo(UUID.fastUUID().toString());
        head.setTimestamp(String.valueOf(System.currentTimeMillis()));

        String key = String.format(LOCK_KEY, head.getPartnerCode(), req.getUserId());
        redisLockService.unLock(key);

        Response<PersonalUpdateResp> response = updateFacadeProvider.updatePersonalInfo(req, head);
        log.info("个人信息更新结果:{}", JSONObject.toJSON(response));
    }


    @Test
    public void enterpriseUpdate() {
        EnterpriseUpdateReq req = getEnterpriseUpdateReq();
        UnifyHead head = new UnifyHead();
        head.setPartnerCode("LLG");
        head.setOutUserId("TEST_2008");
        head.setSequenceNo(UUID.fastUUID().toString());
        head.setTimestamp(String.valueOf(System.currentTimeMillis()));


        String key = String.format(ENT_LOCK_KEY, head.getPartnerCode(), head.getOutUserId());
        redisLockService.unLock(key);
        Response<EnterpriseUpdateResp> response = enterpriseUpdateFacadeProvider.updateEnterpriseInfo(req, head);

        log.info("企业更新结果:{}", JSONObject.toJSON(response));
    }


    private static PersonalUpdateReq getPersonalUpdateReq() {
        PersonalUpdateReq req = new PersonalUpdateReq();
        req.setChangeType(ChangeTypeEnum.ENTERPRISE.getCode());
        req.setSceneType(SceneTypeEnum.B2B.getCode());
        req.setUserId("722721696917749760");

        req.setRealName("王晓东");
        req.setCertType(CertTypeEnum.RESIDENT.getCode());
        req.setCertNo("211282199502073039");
        req.setCardAddr("cbetest");
        req.setCardFrontFile("/front.png");
        req.setCardBackFile("/back.png");
        req.setContactInfo("18812346789");
        req.setEnName("wangxiaodongupdate1233333456789000000001111");
        return req;
    }


    private static EnterpriseUpdateReq getEnterpriseUpdateReq() {
        EnterpriseUpdateReq req = new EnterpriseUpdateReq();
        req.setUserId("727693460806565888"); //todo
        req.setChangeType(ChangeTypeEnum.ENTERPRISE.getCode());
        req.setSceneType(SceneTypeEnum.B2B.getCode());


        EnterpriseUpdateReq.EnterpriseDto enterpriseDto = new EnterpriseUpdateReq.EnterpriseDto();
        enterpriseDto.setEntName("XXX测试公司");
        enterpriseDto.setCertType(CertTypeEnum.RESIDENT.getCode());
        enterpriseDto.setCertNo("410900100013231");
        enterpriseDto.setEntEnName("enterpriseName");
        enterpriseDto.setCertAddr("cbetest/");
        enterpriseDto.setCertFileName("bizLicense.png");
        enterpriseDto.setCustContact("王晓东");
        enterpriseDto.setCustTel("13898572362");
        enterpriseDto.setAddress("宝信大厦");

        EnterpriseUpdateReq.LegalPersonDto legalPersonDto = new EnterpriseUpdateReq.LegalPersonDto();
        legalPersonDto.setLegalPersonName("王晓东");
        legalPersonDto.setLegalTel("13898572362");
        legalPersonDto.setCardNo("211282199502073039");
        legalPersonDto.setCertType(CertTypeEnum.RESIDENT.getCode());
        legalPersonDto.setCardAddr("cbetest/");
        legalPersonDto.setCardFrontFile("front.png");
        legalPersonDto.setCardBackFile("back.png");

        req.setEntDto(enterpriseDto);
        req.setLegalPersonDto(legalPersonDto);
        return req;
    }
}
