package com.mm.csvparserservice.service;

import com.mm.csvparserservice.dto.BalanceDto;
import com.mm.csvparserservice.entity.Balance;

import java.time.Month;
import java.util.List;

public interface BalanceService {
    Balance persistBalance(Balance balance);
    List<BalanceDto> getAllBalanceDtosForMonth(Month month);
}
