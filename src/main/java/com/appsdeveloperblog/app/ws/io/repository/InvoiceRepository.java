package com.appsdeveloperblog.app.ws.io.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.appsdeveloperblog.app.ws.io.entity.InvoiceEntity;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long>  {
	InvoiceEntity findByInvoiceId(String providerId);
	Page<InvoiceEntity> findByProviderName(String providerName, Pageable pageable);
	Page<InvoiceEntity> findByDateTime(Date dateTime, Pageable pageable);

}
