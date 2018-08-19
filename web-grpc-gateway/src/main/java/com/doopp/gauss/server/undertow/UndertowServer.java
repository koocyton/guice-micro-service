package com.doopp.gauss.server.undertow;

import com.doopp.gauss.server.configuration.ApplicationProperties;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.protocols.http2.Http2Channel;
import io.undertow.server.HttpHandler;

import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.ServletContainerInitializerInfo;
import io.undertow.servlet.util.ImmediateInstanceFactory;

import static io.undertow.Handlers.path;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import javax.net.ssl.*;
import javax.servlet.ServletContainerInitializer;
import java.security.KeyStore;
import java.util.HashSet;

public class UndertowServer implements InitializingBean, DisposableBean {

    private String webAppName;
    private Resource webAppRoot;
    private String host = "127.0.0.1";
    private int port = 8088;
    private int sslPort = 8089;
    private Resource jksFile;
    private String jksPassword;
    private String jksSecret;
    private ServletContainerInitializer servletContainerInitializer;

    private Undertow server;
    private DeploymentManager manager;

    @Override
    public void afterPropertiesSet() throws Exception {
        // web servlet
        InstanceFactory<? extends ServletContainerInitializer> instanceFactory = new ImmediateInstanceFactory<>(servletContainerInitializer);
        ServletContainerInitializerInfo sciInfo = new ServletContainerInitializerInfo(WebAppServletContainerInitializer.class, instanceFactory, new HashSet<>());
        DeploymentInfo deploymentInfo = Servlets.deployment()
                .addServletContainerInitalizer(sciInfo)
                // .addServlet(Servlets.servlet("default", DefaultServlet.class))
                .setResourceManager(new FileResourceManager(webAppRoot.getFile(), 0))
                .setClassLoader(UndertowServer.class.getClassLoader())
                .setContextPath(webAppName)
                .setDeploymentName(webAppName + "-war");

        manager = Servlets.defaultContainer().addDeployment(deploymentInfo);
        manager.deploy();
        HttpHandler httpHandler = path()
                .addPrefixPath("/", manager.start());
                // .addPrefixPath("/game-socket", websocket(new GameSocketConnectionCallback()));

        Undertow.Builder builder = Undertow.builder();

        if (this.jksFile!=null) {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(getKeyManagers(), null, null);
            builder.addHttpsListener(sslPort, host, sslContext);
        }

        server = builder.addHttpListener(port, host).setHandler(httpHandler).build();
        server.start();

        if (this.jksFile!=null) {
            System.out.print("\n >>> Undertow web server started at http://" + host + ":" + port + " and https://" + host + ":" + sslPort + "\n\n");
        }
        else {
            System.out.print("\n >>> Undertow web server started at http://" + host + ":" + port);
        }
    }

    @Override
    public void destroy() throws Exception {
        server.stop();
        manager.stop();
        manager.undeploy();
        System.console().printf("Undertow web server on port " + port + " stopped");
    }

    private KeyManager[] getKeyManagers() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(jksFile.getInputStream(), this.jksPassword.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, this.jksSecret.toCharArray());
            return keyManagerFactory.getKeyManagers();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setServletContainerInitializer(ServletContainerInitializer servletContainerInitializer) {
        this.servletContainerInitializer = servletContainerInitializer;
    }

    public void setApplicationProperties(ApplicationProperties properties) {
        this.webAppName = properties.s("server.webAppName");
        this.webAppRoot = properties.r("server.webAppRoot");
        this.host = properties.s("server.host");
        this.port = properties.i("server.port");
        //this.sslPort = properties.i("server.sslPort");
        //this.jksFile = properties.r("server.jks.file");
        //this.jksPassword = properties.s("server.jks.password");
        //this.jksSecret = properties.s("server.jks.secret");
    }
}
