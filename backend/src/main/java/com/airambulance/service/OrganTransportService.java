package com.airambulance.service;

import com.airambulance.dto.OrganTransportRequest;
import com.airambulance.dto.OrganTransportResponse;

import java.util.List;

public interface OrganTransportService {
    OrganTransportResponse createOrganTransport(OrganTransportRequest request);
    OrganTransportResponse updateOrganTransport(Long id, OrganTransportRequest request);
    void deleteOrganTransport(Long id);
    OrganTransportResponse getOrganTransportById(Long id);
    List<OrganTransportResponse> getAllOrganTransports();
    List<OrganTransportResponse> getOrganTransportsByHospital(Long hospitalId);
}
