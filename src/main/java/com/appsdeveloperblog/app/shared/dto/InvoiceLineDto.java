package com.appsdeveloperblog.app.shared.dto;



public class InvoiceLineDto {
	private long id;
	private String productName;
	private Long quantity;
	private Long price;
	private Long totalValue;
	private InvoiceDto invoice;	
	private String line;

	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public InvoiceDto getInvoice() {
		return invoice;
	}
	public void setInvoice(InvoiceDto invoice) {
		this.invoice = invoice;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	
	
	
}
