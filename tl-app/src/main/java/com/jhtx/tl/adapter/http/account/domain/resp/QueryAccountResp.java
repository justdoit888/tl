package com.jhtx.tl.adapter.http.account.domain.resp;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class QueryAccountResp implements Serializable {

    /**id*/
    private Long id;

    /**关联企业id*/
    private Long companyId;

    /**企业类型（商家merchant，渠道channel）*/
    private String companyType;

    /**账户类型*/
    private String accountType;

    /**账户余额*/
    private BigDecimal balance;

    /**冻结金额*/
    private BigDecimal frozenAmount;

    /**状态（正常opened，冻结frozen、停用closed）*/
    private String status;

    /**创建时间*/
    private LocalDateTime createTime;

    /**更新时间*/
    private LocalDateTime updateTime;

    /**版本号*/
    private int version;

    /**调账金额*/
    private BigDecimal manualRecAmount;

    /**调账类型*/
    private String manualRecType;

    /**调账原因*/
    private String reason;
}
