package com.jhtx.tl.infra.repository.dao.account.impl;

import com.jhtx.tl.infra.repository.dao.account.AccountDao;
import com.jhtx.tl.infra.repository.entity.account.Account;
import com.jhtx.tl.infra.repository.mapper.account.AccountMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class AccountDaoImpl implements AccountDao {

    @Resource
    AccountMapper accountMapper;

    @Override
    public Account queryAccountByCompanyId(String companyId) {
        return accountMapper.getAccountByCompanyId(companyId);
    }
}
