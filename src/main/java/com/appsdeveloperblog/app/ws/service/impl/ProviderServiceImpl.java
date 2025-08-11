package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.shared.dto.ProviderDto;
import com.appsdeveloperblog.app.ws.io.entity.ProviderEntity;
import com.appsdeveloperblog.app.ws.io.repository.ProviderRepository;
import com.appsdeveloperblog.app.ws.service.ProviderService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;

@Service
public class ProviderServiceImpl implements ProviderService {

	@Autowired
	Utils utils;

	@Autowired
	ProviderRepository providerRepository;

	@Override
	public ProviderDto createProvider(ProviderDto provider) {
		ProviderEntity providerEntity = new ProviderEntity();

		ModelMapper modelMapper = new ModelMapper();
		providerEntity = modelMapper.map(provider, ProviderEntity.class);

		String providerId = utils.generateProviderId(30);
		providerEntity.setProviderId(providerId);

		ProviderEntity storedProviderDetails = providerRepository.save(providerEntity);

		ProviderDto returnValue = new ProviderDto();
		returnValue = modelMapper.map(storedProviderDetails, ProviderDto.class);
		return returnValue;
	}

	@Override
	public ProviderDto getProviderById(String id) {

		ProviderDto returnValue = new ProviderDto();

		ProviderEntity providerEntity = providerRepository.findByProviderId(id);

		if (providerEntity == null)
			throw new UsernameNotFoundException("NO PROVIDER FOUND WITH ID " + id);

		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(providerEntity, ProviderDto.class);

		return returnValue;
	}

	@Override
	public List<ProviderDto> getProviders(int page, int limit) {
		List<ProviderDto> returnValue = new ArrayList<>();
		Pageable pageableRequest = PageRequest.of(page, limit);
		Page<ProviderEntity> providersPage = providerRepository.findAll(pageableRequest);
		List<ProviderEntity> providers = providersPage.getContent();
		for (ProviderEntity providerEntity : providers) {
			ProviderDto providerDto = new ProviderDto();
			ModelMapper modelMapper = new ModelMapper();
			providerDto = modelMapper.map(providerEntity, ProviderDto.class);
			returnValue.add(providerDto);
		}
		return returnValue;
	}

	@Override
	public ProviderDto upadteProvider(String id, ProviderDto provider) {

		ProviderDto returnValue = new ProviderDto();

		ProviderEntity providerEntity = providerRepository.findByProviderId(provider.getProviderId());

		if (providerEntity == null)
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

		providerEntity.setName(provider.getName());
		providerEntity.setAddress(provider.getAddress());
		providerEntity.setPhone(provider.getPhone());
		providerEntity.setService(provider.getService());
		providerEntity.setNote(provider.getNote());

		ProviderEntity updatedProviderDetails = providerRepository.save(providerEntity);

		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(updatedProviderDetails, ProviderDto.class);
		return returnValue;
	}

	@Override
	public void deleteProivder(String id) {
		ProviderEntity providerEntity = providerRepository.findByProviderId(id);
		if (providerEntity == null)
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		providerRepository.delete(providerEntity);

	}

}
