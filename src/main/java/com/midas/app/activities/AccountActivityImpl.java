package com.midas.app.activities;

import com.midas.app.models.Account;
import com.midas.app.providers.payment.PaymentProvider;
import com.midas.app.repositories.AccountRepository;
import io.temporal.spring.boot.ActivityImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@ActivityImpl(taskQueues = {"create-account-workflow", "update-account-workflow"})
public class AccountActivityImpl implements AccountActivity {
  private final PaymentProvider paymentProvider;
  private final AccountRepository accountRepository;

  @Override
  public Account saveAccount(Account account) {
    return accountRepository.save(account);
  }

  @Override
  public Account createPaymentAccount(Account account) {
    return paymentProvider.createAccount(account);
  }

  @Override
  public Account updatePaymentAccount(Account account) {
    return paymentProvider.updateAccount(account);
  }
}
