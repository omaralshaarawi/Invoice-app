package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.shared.dto.InvoiceDto;
import com.appsdeveloperblog.app.shared.dto.InvoiceLineDto;
import com.appsdeveloperblog.app.ws.io.entity.InvoiceEntity;
import com.appsdeveloperblog.app.ws.io.entity.InvoiceLineEntity;
import com.appsdeveloperblog.app.ws.io.repository.InvoiceLineRepository;
import com.appsdeveloperblog.app.ws.io.repository.InvoiceRepository;
import com.appsdeveloperblog.app.ws.service.InvoiceService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;

@Service
public class InvoiceServiceImpl implements InvoiceService {

	@Autowired
	Utils utils;

	@Autowired
	InvoiceRepository invoiceRepository;

	@Autowired
	InvoiceLineRepository invoiceLineRepository;

	@Override
	public InvoiceDto createInvoice(InvoiceDto invoice) {
		InvoiceEntity invoiceEntity = new InvoiceEntity();
		long total = (long) 0;
		if (invoice.getInvoiceLines() != null) {
			for (int i = 0; i < invoice.getInvoiceLines().size(); i++) {
				InvoiceLineDto invoiceLine = invoice.getInvoiceLines().get(i);
				invoiceLine.setInvoice(invoice);
				invoiceLine.setTotalValue(invoiceLine.getQuantity() * invoiceLine.getPrice());
				total += invoiceLine.getTotalValue();
				String InvoiceLineId = utils.generateInvoiceId(30);
				invoiceLine.setLine(InvoiceLineId);
				invoice.getInvoiceLines().set(i, invoiceLine);
			}
		}
		ModelMapper modelMapper = new ModelMapper();
		invoiceEntity = modelMapper.map(invoice, InvoiceEntity.class);

		String InvoiceId = utils.generateInvoiceId(30);
		invoiceEntity.setInvoiceId(InvoiceId);
		invoiceEntity.setTotal(total);
		invoiceEntity.setRemaining(invoiceEntity.getTotal() - invoiceEntity.getPaid());
		
		if (invoiceEntity.getDateTime() == null) {
			invoiceEntity.setDateTime(new Date());
		}
		InvoiceEntity storedInvoiceDetails = invoiceRepository.save(invoiceEntity);

		InvoiceDto returnValue = new InvoiceDto();
		returnValue = modelMapper.map(storedInvoiceDetails, InvoiceDto.class);
		return returnValue;
	}

	@Override
	public InvoiceDto getInvoiceById(String id) {
		InvoiceDto returnValue = new InvoiceDto();

		InvoiceEntity invoiceEntity = invoiceRepository.findByInvoiceId(id);

		if (invoiceEntity == null)
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(invoiceEntity, InvoiceDto.class);

		return returnValue;
	}

	@Override
	public List<InvoiceDto> getInvoiceByProviderName(int page,int limit,String providerName) {
		List<InvoiceDto> returnValue = new ArrayList<>();
		Pageable pageableRequest = PageRequest.of(page, limit);
		Page<InvoiceEntity> invoicesPage = invoiceRepository.findByProviderName(providerName,pageableRequest);
		List<InvoiceEntity> invoices = invoicesPage.getContent();
		for (InvoiceEntity invoiceEntity : invoices) {
			InvoiceDto invoiceDto = new InvoiceDto();
			ModelMapper modelMapper = new ModelMapper();
			invoiceDto = modelMapper.map(invoiceEntity, InvoiceDto.class);
			returnValue.add(invoiceDto);
		}
		return returnValue;
	}

	@Override
	public List<InvoiceDto> getInvoiceByDateTime(int page,int limit,Date dateTime) {
		List<InvoiceDto> returnValue = new ArrayList<>();
		Pageable pageableRequest = PageRequest.of(page, limit);
		Page<InvoiceEntity> invoicesPage = invoiceRepository.findByDateTime(dateTime,pageableRequest);
		List<InvoiceEntity> invoices = invoicesPage.getContent();
		for (InvoiceEntity invoiceEntity : invoices) {
			InvoiceDto invoiceDto = new InvoiceDto();
			ModelMapper modelMapper = new ModelMapper();
			invoiceDto = modelMapper.map(invoiceEntity, InvoiceDto.class);
			returnValue.add(invoiceDto);
		}
		return returnValue;
	}

	@Override
	public List<InvoiceDto> getInvoices(int page, int limit) {
		List<InvoiceDto> returnValue = new ArrayList<>();
		Pageable pageableRequest = PageRequest.of(page, limit);
		Page<InvoiceEntity> invoicesPage = invoiceRepository.findAll(pageableRequest);
		List<InvoiceEntity> invoices = invoicesPage.getContent();
		for (InvoiceEntity invoiceEntity : invoices) {
			InvoiceDto invoiceDto = new InvoiceDto();
			ModelMapper modelMapper = new ModelMapper();
			invoiceDto = modelMapper.map(invoiceEntity, InvoiceDto.class);
			returnValue.add(invoiceDto);
		}
		return returnValue;
	}

	@Override
	public InvoiceDto updateInvoice(String id, InvoiceDto invoice) {
		InvoiceDto returnValue = new InvoiceDto();
		InvoiceEntity invoiceEntity = invoiceRepository.findByInvoiceId(id);

		if (invoiceEntity == null)
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		if (!invoice.getProviderName().isEmpty())
			invoiceEntity.setProviderName(invoice.getProviderName());
		if (invoice.getDateTime() != null)
			invoiceEntity.setDateTime(invoice.getDateTime());
		if (!invoice.getAddress().isEmpty())
			invoiceEntity.setAddress(invoice.getAddress());
		if (invoice.getPaid() != null)
			invoiceEntity.setPaid(invoice.getPaid());
		if (!invoice.getDelivered_by().isEmpty())
			invoiceEntity.setDelivered_by(invoice.getDelivered_by());

		Long total = (long) 0;
		if (invoice.getInvoiceLines() != null) {
			for (int i = 0; i < invoice.getInvoiceLines().size(); i++) {
				InvoiceLineDto invoiceLine = invoice.getInvoiceLines().get(i);
				InvoiceLineEntity invoiceLineEntity = invoiceLineRepository.findByLine(invoiceLine.getLine());
				
				if (!invoiceLine.getProductName().isEmpty())
					invoiceLineEntity.setProductName(invoiceLine.getProductName());
				if (invoiceLine.getPrice() != null)
					invoiceLineEntity.setPrice(invoiceLine.getPrice());
				if (invoiceLine.getQuantity() != null)
					invoiceLineEntity.setQuantity(invoiceLine.getQuantity());
				
				invoiceLineEntity.setTotalValue(invoiceLine.getQuantity() * invoiceLine.getPrice());
				total += invoiceLineEntity.getTotalValue();
				
				invoiceLineRepository.save(invoiceLineEntity);
			}
		} else
			total = invoiceEntity.getTotal();
		
		invoiceEntity.setTotal(total);
		invoiceEntity.setRemaining(invoiceEntity.getTotal() - invoiceEntity.getPaid());
		
		InvoiceEntity updatedInvoiceDetails = invoiceRepository.save(invoiceEntity);
		
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(updatedInvoiceDetails, InvoiceDto.class);

		return returnValue;
	}

	@Override
	public void deleteInvoice(String id) {
		InvoiceEntity invoiceEntity = invoiceRepository.findByInvoiceId(id);
		if (invoiceEntity == null)
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		invoiceRepository.delete(invoiceEntity);

	}

}
