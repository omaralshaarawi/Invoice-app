package com.appsdeveloperblog.app.ws.ui.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.shared.dto.InvoiceDto;
import com.appsdeveloperblog.app.ws.service.InvoiceService;
import com.appsdeveloperblog.app.ws.ui.model.request.InvoiceDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.ui.model.response.InvoiceRest;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;

@RestController
@RequestMapping("invoice") // http://localhost:8888/inovice-app-ws/invoice
public class invoiceController {

	@Autowired
	InvoiceService invoiceService;

	// Creates An Invoice
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public InvoiceRest createInvoice(@RequestBody InvoiceDetailsRequestModel invoiceDetails) {
		InvoiceRest returnValue = new InvoiceRest();

		if (invoiceDetails.getProviderName().isEmpty())
			throw new UsernameNotFoundException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		if (invoiceDetails.getAddress().isEmpty())
			throw new UsernameNotFoundException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		if (invoiceDetails.getPaid() == null)
			throw new UsernameNotFoundException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		if (invoiceDetails.getDelivered_by().isEmpty())
			throw new UsernameNotFoundException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		ModelMapper modelMapper = new ModelMapper();

		InvoiceDto invoiceDto = modelMapper.map(invoiceDetails, InvoiceDto.class);

		InvoiceDto createdProvider = invoiceService.createInvoice(invoiceDto);

		returnValue = modelMapper.map(createdProvider, InvoiceRest.class);

		return returnValue;

	}

	// Gets An Invoice by ID
	@PreAuthorize("hasAuthority('READ_AUTHORITY')")
	@GetMapping(path = "/id/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public InvoiceRest getInvoiceById(@PathVariable String id) {
		InvoiceRest returnValue = new InvoiceRest();

		InvoiceDto invoiceDto = invoiceService.getInvoiceById(id);
		if (invoiceDto == null)
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(invoiceDto, InvoiceRest.class);
		return returnValue;
	}

	// Gets An Invoices by ProviderName
	@PreAuthorize("hasAuthority('READ_AUTHORITY')")
	@GetMapping(path = "/provider-name/{providerName}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public List<InvoiceRest> getIvoiceByProviderName(@PathVariable String providerName,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int limit) {
		List<InvoiceRest> returnValue = new ArrayList<>();

		List<InvoiceDto> invoices = invoiceService.getInvoiceByProviderName(page,limit,providerName);
		if (invoices == null)
			throw new UsernameNotFoundException("No Invoices with this ProviderName" + providerName);
		for (InvoiceDto invoiceDto : invoices) {
			InvoiceRest invoiceModel = new InvoiceRest();
			ModelMapper modelMapper = new ModelMapper();
			invoiceModel = modelMapper.map(invoiceDto, InvoiceRest.class);
			returnValue.add(invoiceModel);
		}
		return returnValue;
	}

	// Gets An Invoices by Date
	@PreAuthorize("hasAuthority('READ_AUTHORITY')")
	@GetMapping(path = "/date/{dateTime}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public List<InvoiceRest> getInvoiceByDateTime(@PathVariable Date dateTime,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int limit) {
		List<InvoiceRest> returnValue = new ArrayList<>();

		List<InvoiceDto> invoices = invoiceService.getInvoiceByDateTime(page,limit,dateTime);
		if (invoices == null)
			throw new UsernameNotFoundException("No Invoices with this Date" + dateTime);
		for (InvoiceDto invoiceDto : invoices) {
			InvoiceRest invoiceModel = new InvoiceRest();
			ModelMapper modelMapper = new ModelMapper();
			invoiceModel = modelMapper.map(invoiceDto, InvoiceRest.class);
			returnValue.add(invoiceModel);
		}
		return returnValue;
	}

	// Gets all invoices
	@PreAuthorize("hasAuthority('READ_AUTHORITY')")
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<InvoiceRest> getUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int limit) {
		List<InvoiceRest> returnValue = new ArrayList<>();
		List<InvoiceDto> invoices = invoiceService.getInvoices(page, limit);
		for (InvoiceDto invoiceDto : invoices) {
			InvoiceRest invoiceModel = new InvoiceRest();
			ModelMapper modelMapper = new ModelMapper();
			invoiceModel = modelMapper.map(invoiceDto, InvoiceRest.class);
			returnValue.add(invoiceModel);
		}
		return returnValue;
	}

	// Updates An Invoice
	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public InvoiceRest updateInvoice(@RequestBody InvoiceDetailsRequestModel invoiceDetails, @PathVariable String id) {
		InvoiceRest returnValue = new InvoiceRest();
		InvoiceDto invoiceDto = new InvoiceDto();
		ModelMapper modelMapper = new ModelMapper();
		invoiceDto = modelMapper.map(invoiceDetails, InvoiceDto.class);
		InvoiceDto updateInvoice = invoiceService.updateInvoice(id, invoiceDto);
		returnValue = modelMapper.map(updateInvoice, InvoiceRest.class);
		return returnValue;
	}

	// Deletes An Invoice
	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel deleteInvoice(@PathVariable String id,
			@RequestParam(required = false) boolean confirm) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperatioName("DELETE");
		if (confirm == false) {
			returnValue.setOperationResult("PLEASE CONFRIM DELEATION");
			return returnValue;
		}
		invoiceService.deleteInvoice(id);
		returnValue.setOperationResult("SUCCESS");
		return returnValue;
	}

}
