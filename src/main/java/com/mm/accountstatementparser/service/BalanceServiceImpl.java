package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Balance;
import com.mm.accountstatementparser.repository.BalanceRepository;
import java.time.Month;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {
  private final BalanceRepository balanceRepository;

  @Override
  public Balance persistBalance(Balance balance) {
    return balanceRepository.save(balance);
  }

  @Override
  public List<Balance> getAllBalancesForMonth(Month month) {
    return balanceRepository.findAllByMonth(month);
  }
}
