package com.insurance.policy_service.repository;

import com.insurance.policy_service.entity.Policy;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PolicyRepository implements PanacheRepository<Policy> {
}
