package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Balance;
import java.time.Month;
import java.util.List;

public interface BalanceService {
    Balance persistBalance(Balance balance);
    List<Balance> getAllBalancesForMonth(Month month);
}
