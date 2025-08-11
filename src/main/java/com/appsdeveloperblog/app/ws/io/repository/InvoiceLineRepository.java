package com.appsdeveloperblog.app.ws.io.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appsdeveloperblog.app.ws.io.entity.InvoiceEntity;
import com.appsdeveloperblog.app.ws.io.entity.InvoiceLineEntity;

@Repository
public interface InvoiceLineRepository extends JpaRepository<InvoiceLineEntity, Long>  {
	InvoiceLineEntity findByInvoice(InvoiceEntity invoice);
	InvoiceLineEntity findByLine(String line);
}
