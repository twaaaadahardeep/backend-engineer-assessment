package com.midas.app.services;

import com.midas.app.exceptions.ResourceNotFoundException;
import com.midas.app.models.Account;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.workflows.CreateAccountWorkflow;
import com.midas.app.workflows.UpdateAccountWorkflow;
import com.midas.generated.model.UpdateAccountDto;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.workflow.Workflow;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final Logger logger = Workflow.getLogger(AccountServiceImpl.class);

  @Autowired
  private WorkflowService workflowService;

  private final AccountRepository accountRepository;

  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(Account details) {
    var options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(CreateAccountWorkflow.QUEUE_NAME)
            .setWorkflowId(details.getEmail())
            .build();

    logger.info("initiating workflow to create account for email: {}", details.getEmail());

    var client = WorkflowClient.newInstance(workflowService.getService());

    var workflow = client.newWorkflowStub(CreateAccountWorkflow.class, options);

    return workflow.createAccount(details);
  }

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  @Override
  public List<Account> getAccounts() {
    return accountRepository.findAll();
  }

  @Override
  public Account updateAccount(String accountId, UpdateAccountDto updateAccountDto) {
    Optional<Account> existingAccountOptional =
        accountRepository.findById(UUID.fromString(accountId));

    if (existingAccountOptional.isEmpty()) {
      throw new ResourceNotFoundException();
    }

    Account existingAccount = existingAccountOptional.get();

    if (StringUtils.hasText(updateAccountDto.getFirstName())) {
      existingAccount.setFirstName(updateAccountDto.getFirstName());
    }
    if (StringUtils.hasText(updateAccountDto.getLastName())) {
      existingAccount.setLastName(updateAccountDto.getLastName());
    }
    if (StringUtils.hasText(updateAccountDto.getEmail())) {
      existingAccount.setEmail(updateAccountDto.getEmail());
    }

    var options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(UpdateAccountWorkflow.QUEUE_NAME)
            .setWorkflowId(existingAccount.getEmail())
            .build();

    logger.info("initiating workflow to update account for email: {}", existingAccount.getEmail());

    var client = WorkflowClient.newInstance(workflowService.getService());
    var workflow = client.newWorkflowStub(UpdateAccountWorkflow.class, options);
    return workflow.updateAccount(existingAccount);
  }
}
