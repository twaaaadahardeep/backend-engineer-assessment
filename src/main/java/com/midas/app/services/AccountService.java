package com.midas.app.services;

import com.midas.app.models.Account;
import com.midas.generated.model.UpdateAccountDto;
import java.util.List;

public interface AccountService {
  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  Account createAccount(Account details);

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  List<Account> getAccounts();

  /**
   * updateAccount updates an account in the system or provider.
   *
   * @param id the provider ID for the customer.
   * @param updateAccountDto details of the customer to be updated.
   * @return Account
   */
  Account updateAccount(String id, UpdateAccountDto updateAccountDto);
}
