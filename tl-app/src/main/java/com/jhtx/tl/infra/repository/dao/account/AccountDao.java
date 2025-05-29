package com.jhtx.tl.infra.repository.dao.account;

import com.jhtx.tl.infra.repository.entity.account.Account;

public interface AccountDao {

    Account queryAccountByCompanyId(String companyId);
}
