package com.appsdeveloperblog.app.ws.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.shared.dto.InvoiceLineDto;
import com.appsdeveloperblog.app.ws.io.entity.InvoiceEntity;
import com.appsdeveloperblog.app.ws.io.entity.InvoiceLineEntity;
import com.appsdeveloperblog.app.ws.io.repository.InvoiceLineRepository;
import com.appsdeveloperblog.app.ws.io.repository.InvoiceRepository;
import com.appsdeveloperblog.app.ws.service.InvoiceLineService;
import com.appsdeveloperblog.app.ws.service.InvoiceService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;

@Service
public class InvoiceLineServiceImpl implements InvoiceLineService {

	@Autowired
	InvoiceService invoiceService;

	@Autowired
	InvoiceLineRepository invoiceLineRepository;

	@Autowired
	InvoiceRepository invoiceRepository;

	@Autowired
	Utils utils;

	@Override
	public InvoiceLineDto createInvoiceLine(InvoiceLineDto invoiceLine, String id) {
		InvoiceLineEntity invoiceLineEntity = new InvoiceLineEntity();
		ModelMapper modelMapper = new ModelMapper();
		invoiceLineEntity = modelMapper.map(invoiceLine, InvoiceLineEntity.class);
		invoiceLineEntity.setInvoice(invoiceRepository.findByInvoiceId(id));
		invoiceLineEntity.setTotalValue(invoiceLineEntity.getPrice() * invoiceLineEntity.getQuantity());
		String InvoiceLineId = utils.generateInvoiceId(30);
		invoiceLineEntity.setLine(InvoiceLineId);
		InvoiceLineEntity storedinvoiceLineDetails = invoiceLineRepository.save(invoiceLineEntity);

		// update invoice total and remaining
		InvoiceEntity invoiceEntity = new InvoiceEntity();
		invoiceEntity = invoiceRepository.findByInvoiceId(storedinvoiceLineDetails.getInvoice().getInvoiceId());
		invoiceEntity.setTotal(invoiceEntity.getTotal() + storedinvoiceLineDetails.getTotalValue());
		invoiceEntity.setRemaining(invoiceEntity.getTotal() - invoiceEntity.getPaid());
		invoiceRepository.save(invoiceEntity);

		InvoiceLineDto returnValue = new InvoiceLineDto();
		returnValue = modelMapper.map(storedinvoiceLineDetails, InvoiceLineDto.class);
		return returnValue;
	}

	@Override
	public InvoiceLineDto getInvoiceByLine(String id) {
		InvoiceLineDto returnValue = new InvoiceLineDto();

		InvoiceLineEntity invoiceLineEntity = invoiceLineRepository.findByLine(id);

		if (invoiceLineEntity == null)
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(invoiceLineEntity, InvoiceLineDto.class);

		return returnValue;
	}

	@Override
	public InvoiceLineDto updateInvoiceLine(String id, InvoiceLineDto invoiceLineDto) {
		InvoiceLineDto returnValue = new InvoiceLineDto();
		InvoiceLineEntity invoiceLineEntity = invoiceLineRepository.findByLine(id);
		if (invoiceLineEntity == null)
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		if (!invoiceLineDto.getProductName().isEmpty())
			invoiceLineEntity.setProductName(invoiceLineDto.getProductName());
		if (invoiceLineDto.getPrice() != null)
			invoiceLineEntity.setPrice(invoiceLineDto.getPrice());
		if (invoiceLineDto.getQuantity() != null)
			invoiceLineEntity.setQuantity(invoiceLineDto.getQuantity());
		invoiceLineEntity.setTotalValue(invoiceLineEntity.getQuantity() * invoiceLineEntity.getPrice());
		InvoiceLineEntity updatedInvoiceLineDetails = invoiceLineRepository.save(invoiceLineEntity);

		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(updatedInvoiceLineDetails, InvoiceLineDto.class);

		return returnValue;
	}

	@Override
	public void deleteInvoiceLine(String line) {
		InvoiceLineEntity invoiceLineEntity = invoiceLineRepository.findByLine(line);
		if (invoiceLineEntity == null)
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		InvoiceEntity invoiceEntity = invoiceRepository.findByInvoiceId(invoiceLineEntity.getInvoice().getInvoiceId());
		invoiceEntity.setTotal(invoiceEntity.getTotal() - invoiceLineEntity.getTotalValue());
		invoiceEntity.setRemaining(invoiceEntity.getTotal() - invoiceEntity.getPaid());
		invoiceRepository.save(invoiceEntity);
		invoiceEntity.getInvoiceLines().remove(invoiceLineEntity);
		invoiceLineRepository.delete(invoiceLineEntity);
	}

	@Override
	public boolean missingField(InvoiceLineDto invoiceLine) {
		if (invoiceLine.getProductName().isEmpty())
			return false;
		if (invoiceLine.getQuantity() == null)
			return false;
		if (invoiceLine.getPrice() == null)
			return false;
		return true;
	}

}
