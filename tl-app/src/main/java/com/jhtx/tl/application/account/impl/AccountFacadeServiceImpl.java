package com.jhtx.tl.application.account.impl;

import com.jhtx.tl.application.account.AccountFacadeService;
import com.jhtx.tl.infra.repository.dao.account.AccountDao;
import com.jhtx.tl.infra.repository.entity.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * sunjz
 */
@Slf4j
@Service
public class AccountFacadeServiceImpl implements AccountFacadeService {

    @Resource
    private AccountDao accountDao;

    @Override
    public void queryAccountByCompanyId(String companyId) {
       Account account = accountDao.queryAccountByCompanyId(companyId);
       log.info("响应参数为：{}", account.toString() );
    }
}
