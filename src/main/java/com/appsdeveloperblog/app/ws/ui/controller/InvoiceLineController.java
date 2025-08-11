package com.appsdeveloperblog.app.ws.ui.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
import com.appsdeveloperblog.app.shared.dto.InvoiceLineDto;
import com.appsdeveloperblog.app.ws.io.entity.InvoiceEntity;
import com.appsdeveloperblog.app.ws.io.repository.InvoiceLineRepository;
import com.appsdeveloperblog.app.ws.io.repository.InvoiceRepository;
import com.appsdeveloperblog.app.ws.service.InvoiceLineService;
import com.appsdeveloperblog.app.ws.service.InvoiceService;
import com.appsdeveloperblog.app.ws.ui.model.request.InvoiceLineDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.ui.model.response.InvoiceLineRest;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;

@RestController
@RequestMapping("invoiceLine") // http://localhost:8888/invoice-app-ws/invoiceLine
public class InvoiceLineController {

	@Autowired
	InvoiceService invoiceService;

	@Autowired
	InvoiceLineService invoiceLineService;

	@Autowired
	InvoiceLineRepository invoiceLineRepository;

	@Autowired
	InvoiceRepository invoiceRepository;

	// Creates An InvoiceLine
	@PostMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public InvoiceLineRest createInvoiceLine(@PathVariable String id,
			@RequestBody InvoiceLineDetailsRequestModel invoiceLineDetails) {
		InvoiceLineRest returnValue = new InvoiceLineRest();

		ModelMapper modelMapper = new ModelMapper();
		InvoiceLineDto invoiceLineDto = modelMapper.map(invoiceLineDetails, InvoiceLineDto.class);
		if (invoiceRepository.findByInvoiceId(id) == null) {
			throw new UsernameNotFoundException("No Invoice With this ID" + id);
		}
		boolean missingField = invoiceLineService.missingField(invoiceLineDto);
		if (missingField == false)
			throw new UsernameNotFoundException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		InvoiceLineDto createdProvider = invoiceLineService.createInvoiceLine(invoiceLineDto, id);
		returnValue = modelMapper.map(createdProvider, InvoiceLineRest.class);

		return returnValue;

	}

	// Gets An InvoiceLine by ID
	@PreAuthorize("hasAuthority('READ_AUTHORITY')")
	@GetMapping(path = "/{id}/{line}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public InvoiceLineRest getInvoiceLineById(@PathVariable String id, @PathVariable String line) {
		InvoiceLineRest returnValue = new InvoiceLineRest();

		InvoiceDto invoiceDto = invoiceService.getInvoiceById(id);
		if (invoiceDto == null)
			throw new UsernameNotFoundException("Invoice with " + id + "NOT FOUND");

		InvoiceLineDto invoiceLineDto = invoiceLineService.getInvoiceByLine(line);
		if (invoiceLineDto == null)
			throw new UsernameNotFoundException("line with " + id + "NOT FOUND");

		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(invoiceLineDto, InvoiceLineRest.class);
		return returnValue;
	}

	// Updates An Invoice
	@PutMapping(path = "/{id}/{line}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public InvoiceLineRest updateInvoiceLine(@PathVariable String id, @PathVariable String line,
			@RequestBody InvoiceLineDetailsRequestModel invoiceDetails) {
		InvoiceLineRest returnValue = new InvoiceLineRest();
		InvoiceLineDto invoiceLineDto = new InvoiceLineDto();

		InvoiceDto invoiceDto = invoiceService.getInvoiceById(id);
		if (invoiceDto == null)
			throw new UsernameNotFoundException("Invoice with " + id + "NOT FOUND");

		invoiceLineDto = invoiceLineService.getInvoiceByLine(line);
		if (invoiceLineDto == null)
			throw new UsernameNotFoundException("line with " + id + "NOT FOUND");

		InvoiceEntity invoiceEntity = invoiceRepository.findByInvoiceId(id);
		invoiceEntity.setTotal(invoiceEntity.getTotal() - invoiceLineDto.getTotalValue());

		ModelMapper modelMapper = new ModelMapper();
		invoiceLineDto = modelMapper.map(invoiceDetails, InvoiceLineDto.class);

		InvoiceLineDto updateInvoiceLine = invoiceLineService.updateInvoiceLine(line, invoiceLineDto);
		invoiceEntity.setTotal(invoiceEntity.getTotal() + updateInvoiceLine.getTotalValue());
		invoiceEntity.setRemaining(invoiceEntity.getTotal() - invoiceEntity.getPaid());
		invoiceEntity = invoiceRepository.save(invoiceEntity);
		returnValue = modelMapper.map(updateInvoiceLine, InvoiceLineRest.class);
		return returnValue;
	}

	// Deletes An Invoice Line
	@DeleteMapping(path = "/{id}/{line}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel deleteInvoiceLine(@PathVariable String id, @PathVariable String line,
			@RequestParam(required = false) boolean confirm) {
		InvoiceDto invoiceDto = invoiceService.getInvoiceById(id);
		if (invoiceDto == null)
			throw new UsernameNotFoundException("Invoice with " + id + "NOT FOUND");
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperatioName("DELETE");
		if (confirm == false) {
			returnValue.setOperationResult("PLEASE CONFRIM DELEATION");
			return returnValue;
		}
		invoiceLineService.deleteInvoiceLine(line);
		returnValue.setOperationResult("SUCCESS");
		return returnValue;
	}

}
