package com.appsdeveloperblog.app.ws.io.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Invoice")
public class InvoiceEntity implements Serializable {

	
	private static final long serialVersionUID = 6852303495085348525L;


	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false)
	private String invoiceId;
	
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date dateTime;
	
	@Column(nullable=false)
	private String providerName;
	
	
	@Column(nullable=false)
	private String address;
	
	@Column(nullable=false)
	private Long total;
	
	@Column(nullable=false)
	private Long paid;
	
	@Column(nullable=false)
	private Long remaining;
	
	@Column(nullable=false)
	private String delivered_by;
	
	@OneToMany(mappedBy="invoice",cascade=CascadeType.ALL,fetch=FetchType.EAGER,orphanRemoval=true)
	List<InvoiceLineEntity> invoiceLines;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
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

	public List<InvoiceLineEntity> getInvoiceLines() {
		return invoiceLines;
	}

	public void setInvoiceLines(List<InvoiceLineEntity> invoiceLines) {
		this.invoiceLines = invoiceLines;
	}
	

	
	



	
	
	
	
}
