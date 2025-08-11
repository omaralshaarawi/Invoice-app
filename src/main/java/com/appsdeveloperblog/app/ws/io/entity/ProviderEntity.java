package com.appsdeveloperblog.app.ws.io.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="providers")
public class ProviderEntity implements Serializable {

	private static final long serialVersionUID = 5503846073433138804L;
	
	@Id
	@GeneratedValue
	private long id;
	
	
	@Column(nullable=false)
	private String providerId;
	
	@Column(nullable=false,length=50)
	private String name;
	
	@Column(nullable=false,length=11)
	private long phone;
	
	
	@Column(nullable=false)
	private String service;
	
	@Column(nullable=false)
	private String note;
	
	private String address;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

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

	public void setAddress(String address) {
		this.address = address;
	}


	
	
	
	
}
