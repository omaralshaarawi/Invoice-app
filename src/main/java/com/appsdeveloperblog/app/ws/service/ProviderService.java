package com.appsdeveloperblog.app.ws.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.shared.dto.ProviderDto;

@Service
public interface ProviderService {
	ProviderDto createProvider(ProviderDto provider);

	ProviderDto getProviderById(String id);

	List<ProviderDto> getProviders(int page, int limit);

	ProviderDto upadteProvider(String id, ProviderDto providerDto);

	void deleteProivder(String id);
}
