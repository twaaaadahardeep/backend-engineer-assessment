package com.midas.app.services;

import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {
    public WorkflowServiceStubs getService() {
        WorkflowServiceStubs service = WorkflowServiceStubs.newServiceStubs(
                WorkflowServiceStubsOptions.newBuilder()
                        .setTarget("temporal:7233")
                        .build()
        );
        return service;
    }
}
