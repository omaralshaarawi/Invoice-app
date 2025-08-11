package com.appsdeveloperblog.app.ws.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appsdeveloperblog.app.ws.io.entity.ProviderEntity;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity, Long>  {
	ProviderEntity findByProviderId(String providerId);
}
