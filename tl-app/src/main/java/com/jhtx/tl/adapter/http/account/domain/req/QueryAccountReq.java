package com.jhtx.tl.adapter.http.account.domain.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class QueryAccountReq implements Serializable {

    @NotBlank(message = "企业号不能为空")
    public String companyId;
}
