package com.appsdeveloperblog.app.ws.ui.model.response;

public class InvoiceLineRest {
	private String productName;
	private Long quantity;
	private Long price;
	private Long totalValue;
	private String line;

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

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

}
