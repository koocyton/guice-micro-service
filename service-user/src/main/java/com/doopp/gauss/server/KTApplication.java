package com.doopp.gauss.server;

import com.doopp.gauss.api.grpc.UserGrpcImpl;
import com.doopp.gauss.api.service.AccountService;
import com.doopp.gauss.api.service.impl.AccountServiceImpl;
import com.doopp.gauss.common.dao.UserDao;
import com.doopp.gauss.common.utils.IdWorker;
import com.doopp.gauss.server.application.ApplicationProperties;
import com.doopp.gauss.server.database.HikariDataSourceProvider;
import com.doopp.gauss.server.module.RedisModule;
import com.google.inject.*;
import com.google.inject.name.Names;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import java.io.IOException;

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
                new ApplicationModule()
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

        @Override
        public void configure() {
            bind(AccountService.class).to(AccountServiceImpl.class).in(Scopes.SINGLETON);
        }

        @Singleton
        @Provides
        public IdWorker userIdWorker() {
            return new IdWorker(1, 1);
        }
    }

    /**
     * Grpc Server
     */
    private class GrpcServer {

        @Inject
        private Injector injector;

        private Server server;

        private ApplicationProperties applicationProperties;

        GrpcServer(ApplicationProperties applicationProperties) {
            this.applicationProperties = applicationProperties;
        }

        private void start() throws IOException {

            int port = this.applicationProperties.i("server.port");

            this.server = ServerBuilder.forPort(port)
                .addService(injector.getInstance(UserGrpcImpl.class))
                .build()
                .start();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                    System.out.print("*** shutting down gRPC server since JVM is shutting down");
                    GrpcServer.this.stop();
                    System.out.print("*** server shut down");
                }
            });
        }

        private void stop() {
            if (this.server != null) {
                this.server.shutdown();
            }
        }

        /**
         * Await termination on the main thread since the grpc library uses daemon threads.
         */
        private void blockUntilShutdown() throws InterruptedException {
            if (this.server != null) {
                this.server.awaitTermination();
            }
        }

        /**
         * Main launches the server from the command line.
         */
        private void run() throws IOException, InterruptedException {
            this.start();
            this.blockUntilShutdown();
        }
    }
}
