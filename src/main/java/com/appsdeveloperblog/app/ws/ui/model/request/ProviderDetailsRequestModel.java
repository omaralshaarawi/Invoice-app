package com.appsdeveloperblog.app.ws.ui.model.request;

public class ProviderDetailsRequestModel {
	private String name;
	private long phone;
	private String service;
	private String note;
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPhone() {
		return phone;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAddress() {
		return address;
	}

	public void setAddresses(String address) {
		this.address = address;
	}

}
