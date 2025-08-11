package com.appsdeveloperblog.app.ws.io.entity;

import java.io.Serializable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="InvoiceLine")
public class InvoiceLineEntity implements Serializable {


	private static final long serialVersionUID = -7041433171870719977L;



	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false)
	private String line;
	
	@Column(nullable=false)
	private String productName;
	
	
	@Column(nullable=false)
	private Long quantity;
	
	@Column(nullable=false)
	private Long price;
	
	@Column(nullable=false)
	private Long totalValue;
	

	
	@ManyToOne
	@JoinColumn(name="Invoice_id")
	private InvoiceEntity invoice;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	
	public Long getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Long totalValue) {
		this.totalValue = totalValue;
	}

	public InvoiceEntity getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceEntity invoice) {
		this.invoice = invoice;
	}

	
	
	
	
	
	
}
