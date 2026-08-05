package com.airambulance.repository;

import com.airambulance.entity.Hospital;
import com.airambulance.entity.OrganTransport;
import com.airambulance.enums.OrganTransportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganTransportRepository extends JpaRepository<OrganTransport, Long> {
    List<OrganTransport> findBySourceHospitalOrDestinationHospital(Hospital source, Hospital destination);
    List<OrganTransport> findByStatus(OrganTransportStatus status);
}
