package com.kcbebank.payment.cbe.modules.trade.facade;

import com.kcbebank.payment.cbe.common.constant.PreConstants;
import com.kcbebank.payment.cbe.modules.common.enums.PartnerEnums;
import com.kcbebank.payment.cbe.modules.common.domain.UnifyHead;

import java.util.Date;

/**
 * @author liuyajun
 * @date 2024/1/22
 * @description 请求头生成器
 */
public class HeadTools {

    public static String outUserId = "TEST_Enterprise";
    public static String ip = "127.0.0.1";
    public static String mac = "00:00:00:00:00:00";

    public static UnifyHead getHead(){
        return UnifyHead.builder()
                .outUserId(outUserId)
                .partnerCode(PartnerEnums.LLG.getPartnerCode())
                .sequenceNo(PreConstants.SYSTEM_CODE + new Date().getTime())
                .timestamp("" + new Date().getTime())
                .ip(ip)
                .mac(mac).build();
    }
}
