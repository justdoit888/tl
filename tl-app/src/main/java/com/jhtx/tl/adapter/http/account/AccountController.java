package com.jhtx.tl.adapter.http.account;

import com.jhtx.tl.adapter.http.account.domain.req.QueryAccountReq;
import com.jhtx.tl.adapter.http.account.domain.resp.QueryAccountResp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("account")
public class AccountController {

    /**
     * 根据企业号查询账户信息
     *
     * @param queryAccountReq
     * @return QueryAccountResp
     */
    @RequestMapping("queryAccountByCompanyId")
    public QueryAccountResp queryAccountByCompanyId(@RequestBody QueryAccountReq queryAccountReq){

        return null;
    }
}
