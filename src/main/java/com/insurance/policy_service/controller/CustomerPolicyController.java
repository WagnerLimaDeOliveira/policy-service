package com.insurance.policy_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.insurance.policy_service.dto.PolicyRequest;
import com.insurance.policy_service.dto.PolicyUpdate;
import com.insurance.policy_service.entity.Policy;
import com.insurance.policy_service.service.PolicyService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.UUID;

@Path("/customer")
public class CustomerPolicyController {

    @Inject
    private PolicyService policyService;

    @GET
    @Path("{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Policy> getPoliciesByCustomerId(@PathParam("customerId") Long customerId) {
        return policyService.getPoliciesByCustomerId(customerId);
    }

    @POST
    @Path("{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Policy createPolicy(@PathParam("customerId") Long customerId, PolicyRequest policyRequest) throws JsonProcessingException {
        return policyService.createPolicy(customerId, policyRequest);
    }

    @PUT
    @Path("{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Policy updatePolicy(@PathParam("customerId") Long customerId, @QueryParam("policyNumber") String policyNumber
            , PolicyUpdate policyUpdate) {
        return policyService.updatePolicy(customerId, policyNumber, policyUpdate);
    }

    @DELETE
    @Path("{customerId}/policy/{policyNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public long deletePolicy(@PathParam("customerId") Long customerId,
                             @PathParam("policyNumber") UUID policyNumber) throws JsonProcessingException {
     return policyService.deletePolicy(customerId, policyNumber);
    }
}
