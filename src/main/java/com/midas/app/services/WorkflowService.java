package com.midas.app.services;

import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {
  /**
   * Creates a new WorkflowServiceStubs for WorkflowClient.
   * @return WorkflowServiceStubs service.
   */
  public WorkflowServiceStubs getService() {
    WorkflowServiceStubs service =
        WorkflowServiceStubs.newServiceStubs(
            WorkflowServiceStubsOptions.newBuilder().setTarget("temporal:7233").build());
    return service;
  }
}
