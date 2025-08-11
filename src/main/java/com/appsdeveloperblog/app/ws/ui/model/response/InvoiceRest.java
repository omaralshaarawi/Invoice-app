package com.appsdeveloperblog.app.ws.ui.model.response;

import java.util.Date;
import java.util.List;

public class InvoiceRest {
	private String invoiceId;
	private Date dateTime;
	private String providerName;
	private String address;
	private Long total;
	private Long paid;
	private Long remaining;
	private String delivered_by;
	List<InvoiceLineRest> invoiceLines;

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getPaid() {
		return paid;
	}

	public void setPaid(Long paid) {
		this.paid = paid;
	}

	public Long getRemaining() {
		return remaining;
	}

	public void setRemaining(Long remaining) {
		this.remaining = remaining;
	}

	public String getDelivered_by() {
		return delivered_by;
	}

	public void setDelivered_by(String delivered_by) {
		this.delivered_by = delivered_by;
	}

	public List<InvoiceLineRest> getInvoiceLines() {
		return invoiceLines;
	}

	public void setInvoiceLines(List<InvoiceLineRest> invoiceLines) {
		this.invoiceLines = invoiceLines;
	}

}
