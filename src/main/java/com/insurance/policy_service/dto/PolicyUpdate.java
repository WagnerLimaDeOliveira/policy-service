package com.insurance.policy_service.dto;

import java.time.Instant;

public class PolicyUpdate {
    public Instant startDate;
    public Instant endDate;
    public double premium;
    public PolicyStatus policyStatus;
}
