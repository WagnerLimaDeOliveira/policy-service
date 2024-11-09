package com.insurance.policy_service.dto;

import java.time.Instant;

public class PolicyRequest {
    public Instant startDate;
    public Instant endDate;
    public double premium;
    public PolicyStatus policyStatus;
    public PolicyType policyType;
}
