package com.appsdeveloperblog.app.ws.ui.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
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

import com.appsdeveloperblog.app.shared.dto.ProviderDto;
import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.service.ProviderService;
import com.appsdeveloperblog.app.ws.ui.model.request.ProviderDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.ProviderRest;

@RestController
@RequestMapping("providers") // http://localhost:8888/invoice-app-ws/providers
public class providerController {

	@Autowired
	ProviderService providerService;

	// Creates A Provider
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ProviderRest createProvider(@RequestBody ProviderDetailsRequestModel providerDetails) {
		ProviderRest returnValue = new ProviderRest();
		if (providerDetails.getName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		ModelMapper modelMapper = new ModelMapper();
		ProviderDto providerDto = modelMapper.map(providerDetails, ProviderDto.class);

		ProviderDto createdProvider = providerService.createProvider(providerDto);

		returnValue = modelMapper.map(createdProvider, ProviderRest.class);

		return returnValue;

	}

	// Gets A Provider
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ProviderRest getProvider(@PathVariable String id) {
		ProviderRest returnValue = new ProviderRest();

		ProviderDto providerDto = providerService.getProviderById(id);
		if (providerDto == null)
			throw new UserServiceException("Provider " + id + "NOT FOUND");
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(providerDto, ProviderRest.class);
		return returnValue;
	}

	// Gets all Providers
	@Secured("ADMIN")
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<ProviderRest> getUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int limit) {
		List<ProviderRest> returnValue = new ArrayList<>();
		List<ProviderDto> providers = providerService.getProviders(page, limit);
		for (ProviderDto providerDto : providers) {
			ProviderRest providerModel = new ProviderRest();
			ModelMapper modelMapper = new ModelMapper();
			providerModel = modelMapper.map(providerDto, ProviderRest.class);
			returnValue.add(providerModel);
		}
		return returnValue;
	}

	// Updates A Provider
	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ProviderRest updateProvider(@RequestBody ProviderDetailsRequestModel providerDetails,
			@PathVariable String id) {
		ProviderRest returnValue = new ProviderRest();
		if (providerDetails.getName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		ProviderDto providerDto = new ProviderDto();
		BeanUtils.copyProperties(providerDetails, providerDto);
		ProviderDto updateUser = providerService.upadteProvider(id, providerDto);
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(updateUser, ProviderRest.class);
		return returnValue;
	}

	// Deletes A Provider
	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel deleteProvider(@PathVariable String id,
			@RequestParam(required = false) boolean confirm) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperatioName("DELETE");
		if (confirm == false) {
			returnValue.setOperationResult("PLEASE CONFRIM DELEATION");
			return returnValue;
		}
		providerService.deleteProivder(id);
		returnValue.setOperationResult("SUCCESS");
		return returnValue;
	}

}
