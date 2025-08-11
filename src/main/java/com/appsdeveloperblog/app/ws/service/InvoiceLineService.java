package com.appsdeveloperblog.app.ws.service;

import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.shared.dto.InvoiceLineDto;

@Service
public interface InvoiceLineService {
	InvoiceLineDto createInvoiceLine(InvoiceLineDto invoiceLine, String id);

	boolean missingField(InvoiceLineDto invoiceLine);

	InvoiceLineDto getInvoiceByLine(String id);

	InvoiceLineDto updateInvoiceLine(String id, InvoiceLineDto invoiceLineDto);

	void deleteInvoiceLine(String line);
}
