package com.doopp.gauss.server.module;

import com.doopp.gauss.common.util.IdWorker;
import com.doopp.gauss.server.application.ApplicationProperties;
import com.google.inject.*;

public class ApplicationModule extends AbstractModule {

	@Override
	public void configure() {
		bind(AccountService.class).to(AccountServiceImpl.class);
	}

	@Singleton
	@Provides
	public IdWorker userIdWorker() {
		return new IdWorker(1, 1);
	}

	@Singleton
	@Provides
	public ApplicationProperties applicationProperties() {
		return new ApplicationProperties();
	}
}
