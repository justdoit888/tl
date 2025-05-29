package com.jhtx.tl.infra.repository.mapper.account;


import com.jhtx.tl.infra.repository.entity.account.Account;
import org.apache.ibatis.annotations.Param;

public interface AccountMapper {

    Account getAccountByCompanyId(@Param("companyId") String companyId);

}
