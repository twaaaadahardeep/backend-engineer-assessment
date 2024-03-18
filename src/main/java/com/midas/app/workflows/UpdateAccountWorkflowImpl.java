package com.midas.app.workflows;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;

@WorkflowImpl(taskQueues = "update-account-workflow")
public class UpdateAccountWorkflowImpl implements UpdateAccountWorkflow {
  private final RetryOptions retryOptions = RetryOptions.newBuilder().setMaximumAttempts(3).build();
  private final AccountActivity accountActivity =
      Workflow.newActivityStub(
          AccountActivity.class,
          ActivityOptions.newBuilder()
              .setRetryOptions(retryOptions)
              .setStartToCloseTimeout(Duration.ofSeconds(10))
              .build());

  /**
   * updateAccount updates an existing account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account updateAccount(Account details) {
    Account account = accountActivity.updatePaymentAccount(details);
    return accountActivity.saveAccount(account);
  }
}
