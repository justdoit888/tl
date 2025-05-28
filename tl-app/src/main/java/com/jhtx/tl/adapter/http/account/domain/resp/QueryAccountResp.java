package com.jhtx.tl.adapter.http.account.domain.resp;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class QueryAccountResp implements Serializable {

    public Long id;
    public String name;
}
