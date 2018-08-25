package com.doopp.gauss.server;

import com.doopp.gauss.api.service.AccountService;
import com.doopp.gauss.api.service.impl.AccountServiceImpl;
import com.doopp.gauss.common.dao.UserDao;
import com.doopp.gauss.common.utils.IdWorker;
import com.doopp.gauss.server.application.ApplicationProperties;
import com.doopp.gauss.server.database.HikariDataSourceProvider;
import com.doopp.gauss.server.module.RedisModule;
import com.google.inject.*;
import com.google.inject.name.Names;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;

public class KTApplication {

    /**
     * 程序入口
     * @param args 参数
     * @throws Exception 异常
     */
    public static void main(String[] args) throws Exception {
        ApplicationProperties applicationProperties = new ApplicationProperties(args[0]);
        Injector injector = Guice.createInjector(
                new MMyBatisModule(applicationProperties),
                new RedisModule(applicationProperties),
                new ApplicationModule(applicationProperties)
        );
        final GrpcServer server = injector.getInstance(GrpcServer.class);
        server.run();
    }

    /**
     * 数据库
     */
    private static class MMyBatisModule extends MyBatisModule {

        private ApplicationProperties applicationProperties;

        MMyBatisModule(ApplicationProperties applicationProperties) {
            this.applicationProperties = applicationProperties;
        }

        @Override
        protected void initialize() {
            install(JdbcHelper.MySQL);
            bindDataSourceProviderType(HikariDataSourceProvider.class);
            bindTransactionFactoryType(JdbcTransactionFactory.class);
            addMapperClass(UserDao.class);
            Names.bindProperties(binder(), this.applicationProperties);
        }
    }

    /**
     * 应用的配置
     */
    private static class ApplicationModule extends AbstractModule {

        private ApplicationProperties applicationProperties;

        ApplicationModule(ApplicationProperties applicationProperties) {
            this.applicationProperties = applicationProperties;
        }

        @Override
        public void configure() {
            bind(AccountService.class).to(AccountServiceImpl.class).in(Scopes.SINGLETON);
        }

        @Singleton
        @Provides
        public IdWorker userIdWorker() {
            return new IdWorker(1, 1);
        }

        @Singleton
        @Provides
        public ApplicationProperties applicationProperties() {
            return this.applicationProperties;
        }
    }
}
