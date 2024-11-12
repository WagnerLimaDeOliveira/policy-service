package com.insurance.policy_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.policy_service.dto.CustomerPolicy;
import com.insurance.policy_service.dto.PolicyRequest;
import com.insurance.policy_service.dto.PolicyUpdate;
import com.insurance.policy_service.entity.Policy;
import com.insurance.policy_service.repository.PolicyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PolicyService {
    @Inject
    PolicyRepository policyRepository;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    @Channel("generated-policy")
    Emitter<String> policyEmitter;

    @Inject()
    @Channel("deleted-policy-events")
    Emitter<String> deletedPolicyEmitter;

    public List<Policy> getPoliciesByCustomerId(Long customerId) {
        return policyRepository.find("customerId", customerId).stream().toList();
    }

    @Transactional
    public Policy createPolicy(Long customerId, PolicyRequest policyRequest) throws JsonProcessingException {
        Policy policy = new Policy();
        if (customerId != null) {
            policy.setCustomerId(customerId);
            policy.setStartDate(policyRequest.startDate);
            policy.setEndDate(policyRequest.endDate);
            policy.setPremium(policyRequest.premium);
            policy.setStatus(policyRequest.policyStatus);
            policy.setPolicyType(policyRequest.policyType);
            policyRepository.persist(policy);
            CustomerPolicy customerPolicy = new CustomerPolicy(customerId, policy.getPolicyNumber());
            policyEmitter.send(objectMapper.writeValueAsString(customerPolicy));
            return policy;
        } else {
            return null;
        }
    }

    /*TODO: Add Logic to only Update fields which were Updated **/
    @Transactional
    public Policy updatePolicy(Long customerId, String policyNumber, PolicyUpdate policyUpdate) {
        if (customerId != null && policyNumber != null) {
            Policy updatedPolicy = new Policy();
            updatedPolicy.setStartDate(policyUpdate.startDate);
            updatedPolicy.setEndDate(policyUpdate.endDate);
            updatedPolicy.setPremium(policyUpdate.premium);
            updatedPolicy.setStatus(policyUpdate.policyStatus);
            policyRepository.persist(updatedPolicy);
            return updatedPolicy;
        } else {
            return null;
        }
    }

    @Transactional
    public Long deletePolicy(Long customerId, UUID policyNumber) throws JsonProcessingException {
        Long deletedRows = policyRepository.delete("policyNumber", policyNumber);
        CustomerPolicy customerPolicy = new CustomerPolicy(customerId, policyNumber);
        String customerPolicyJson = objectMapper.writeValueAsString(customerPolicy);
        deletedPolicyEmitter.send(customerPolicyJson);
        return deletedRows;
    }
}
