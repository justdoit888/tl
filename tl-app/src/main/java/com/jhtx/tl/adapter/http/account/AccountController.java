package com.jhtx.tl.adapter.http.account;

import com.jhtx.tl.adapter.http.account.domain.req.QueryAccountReq;
import com.jhtx.tl.adapter.http.account.domain.resp.QueryAccountResp;
import com.jhtx.tl.application.account.AccountFacadeService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author sunjz
 */
@RestController
@RequestMapping("account")
public class AccountController {

    @Resource
    private AccountFacadeService accountFacadeService;

    /**
     * 根据企业号查询账户信息
     *
     * @return QueryAccountResp
     */
    @RequestMapping("queryAccountByCompanyId")
    public QueryAccountResp queryAccountByCompanyId(@RequestBody QueryAccountReq queryAccountReq){
        accountFacadeService.queryAccountByCompanyId(queryAccountReq.getCompanyId());
        return null;
    }
}
