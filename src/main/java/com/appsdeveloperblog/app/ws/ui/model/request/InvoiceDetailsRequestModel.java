package com.appsdeveloperblog.app.ws.ui.model.request;

import java.util.Date;
import java.util.List;

public class InvoiceDetailsRequestModel {

	private String providerName;
	private Date dateTime;
	private String address;
	private Long paid;
	private String delivered_by;
	List<InvoiceLineDetailsRequestModel> invoiceLines;

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getPaid() {
		return paid;
	}

	public void setPaid(Long paid) {
		this.paid = paid;
	}

	public String getDelivered_by() {
		return delivered_by;
	}

	public void setDelivered_by(String delivered_by) {
		this.delivered_by = delivered_by;
	}

	public List<InvoiceLineDetailsRequestModel> getInvoiceLines() {
		return invoiceLines;
	}

	public void setInvoiceLines(List<InvoiceLineDetailsRequestModel> invoiceLines) {
		this.invoiceLines = invoiceLines;
	}

}
