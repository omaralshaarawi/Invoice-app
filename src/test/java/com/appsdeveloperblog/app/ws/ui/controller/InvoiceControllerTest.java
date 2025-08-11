package com.appsdeveloperblog.app.ws.ui.controller;

import com.appsdeveloperblog.app.shared.dto.InvoiceDto;
import com.appsdeveloperblog.app.ws.service.InvoiceService;
import com.appsdeveloperblog.app.ws.ui.model.request.InvoiceDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.InvoiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class InvoiceControllerTest {

    @InjectMocks
    invoiceController invoiceController;

    @Mock
    InvoiceService invoiceService;

    InvoiceDto invoiceDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceId("inv123");
        invoiceDto.setProviderName("ABC Supplies");
        invoiceDto.setDateTime(new Date());
        invoiceDto.setTotal(500L);
        invoiceDto.setAddress("123 Street, City");
    }

    @Test
    void testGetInvoice() {
        when(invoiceService.getInvoiceById(anyString())).thenReturn(invoiceDto);

        InvoiceRest invoiceRest = invoiceController.getInvoiceById("inv123");

        assertNotNull(invoiceRest);
        assertEquals("ABC Supplies", invoiceRest.getProviderName());
        assertEquals("inv123", invoiceRest.getInvoiceId());
        verify(invoiceService, times(1)).getInvoiceById("inv123");
    }

    

    @Test
    void testUpdateInvoice() {
        when(invoiceService.updateInvoice(anyString(), any(InvoiceDto.class))).thenReturn(invoiceDto);

        InvoiceDetailsRequestModel requestModel = new InvoiceDetailsRequestModel();
        requestModel.setProviderName("Updated Supplier");
        requestModel.setAddress("New Address");

        InvoiceRest invoiceRest = invoiceController.updateInvoice(requestModel, "inv123");

        assertNotNull(invoiceRest);
        assertEquals("ABC Supplies", invoiceRest.getProviderName()); // mock still has original name
        verify(invoiceService, times(1)).updateInvoice(eq("inv123"), any(InvoiceDto.class));
    }

    @Test
    void testDeleteInvoice() {
        invoiceController.deleteInvoice("inv123",true);
        verify(invoiceService, times(1)).deleteInvoice("inv123");
    }

   
}
