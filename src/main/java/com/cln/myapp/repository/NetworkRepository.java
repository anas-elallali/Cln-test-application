package com.cln.myapp.repository;

import com.cln.myapp.domain.Network;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Network entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NetworkRepository extends JpaRepository<Network, Long> {
}
