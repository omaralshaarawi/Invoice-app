package com.appsdeveloperblog.app.ws.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.appsdeveloperblog.app.shared.dto.InvoiceDto;
import com.appsdeveloperblog.app.shared.dto.InvoiceLineDto;
import com.appsdeveloperblog.app.ws.io.entity.InvoiceEntity;
import com.appsdeveloperblog.app.ws.io.entity.InvoiceLineEntity;
import com.appsdeveloperblog.app.ws.io.repository.InvoiceLineRepository;
import com.appsdeveloperblog.app.ws.io.repository.InvoiceRepository;
import com.appsdeveloperblog.app.ws.service.impl.InvoiceServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceLineRepository invoiceLineRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private InvoiceEntity invoiceEntity;
    private InvoiceDto invoiceDto;

    @BeforeEach
    void setUp() {
        invoiceEntity = new InvoiceEntity();
        invoiceEntity.setInvoiceId("testId");
        invoiceEntity.setProviderName("Provider A");
        invoiceEntity.setAddress("123 Test Street");
        invoiceEntity.setDelivered_by("jon");
        invoiceEntity.setPaid(100L);
        invoiceEntity.setTotal(200L);
        invoiceEntity.setRemaining(100L);
        

        invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceId("testId");
        invoiceDto.setProviderName("Provider A");
        invoiceDto.setAddress("123 Test Street");
        invoiceDto.setDelivered_by("jon");
        invoiceDto.setPaid(100L);
    }

    @Test
    void testGetInvoiceByInvoiceId_Found() {
        when(invoiceRepository.findByInvoiceId("testId")).thenReturn(invoiceEntity);

        InvoiceDto result = invoiceService.getInvoiceById("testId");

        assertNotNull(result);
        assertEquals("Provider A", result.getProviderName());
        assertEquals("123 Test Street", result.getAddress());
    }

    @Test
    void testGetInvoiceByInvoiceId_NotFound() {
        when(invoiceRepository.findByInvoiceId("notFound")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> invoiceService.getInvoiceById("notFound"));
    }

    @Test
    void testUpdateInvoice_WithAddressAndLines() {
        InvoiceLineEntity lineEntity = new InvoiceLineEntity();
        lineEntity.setLine("line1");
        lineEntity.setPrice(50L);
        lineEntity.setQuantity(2L);
        lineEntity.setProductName("Product X");
        lineEntity.setTotalValue(100L);

        invoiceEntity.setInvoiceLines(List.of(lineEntity));

        InvoiceLineDto lineDto = new InvoiceLineDto();
        lineDto.setLine("line1");
        lineDto.setPrice(60L);
        lineDto.setQuantity(3L);
        lineDto.setProductName("Product Y");

        invoiceDto.setInvoiceLines(List.of(lineDto));

        when(invoiceRepository.findByInvoiceId("testId")).thenReturn(invoiceEntity);
        when(invoiceLineRepository.findByLine("line1")).thenReturn(lineEntity);
        when(invoiceRepository.save(any(InvoiceEntity.class))).thenAnswer(i -> i.getArgument(0));

        InvoiceDto updated = invoiceService.updateInvoice("testId", invoiceDto);

        assertEquals("Product Y", invoiceEntity.getInvoiceLines().get(0).getProductName());
        assertEquals(180L, invoiceEntity.getInvoiceLines().get(0).getTotalValue());
        assertEquals("123 Test Street", updated.getAddress());
    }

    @Test
    void testUpdateInvoice_NotFound() {
        when(invoiceRepository.findByInvoiceId("missingId")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> invoiceService.updateInvoice("missingId", invoiceDto));
    }
}
