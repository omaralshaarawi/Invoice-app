package com.appsdeveloperblog.app.ws.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.shared.dto.InvoiceDto;

@Service
public interface InvoiceService {
	InvoiceDto createInvoice(InvoiceDto invoice);

	InvoiceDto getInvoiceById(String id);

	List<InvoiceDto> getInvoiceByProviderName(int page,int limit,String providerName);

	List<InvoiceDto> getInvoiceByDateTime(int page, int limit,Date dateTime);

	List<InvoiceDto> getInvoices(int page, int limit);

	InvoiceDto updateInvoice(String id, InvoiceDto invoiceDto);

	void deleteInvoice(String id);
}
