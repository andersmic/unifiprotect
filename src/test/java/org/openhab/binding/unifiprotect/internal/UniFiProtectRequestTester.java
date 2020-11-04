/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.unifiprotect.internal;

import java.io.InputStream;
import java.util.Properties;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectCameraCache;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvr;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvrType;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvrType.Type;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectCameraInstanceCreator;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectJsonParser;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectBootstrapRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectLoginRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectRequest;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectNvrDevice;
import org.slf4j.LoggerFactory;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * The {@link UniFiProtectRequestTester} Test for fetching and parsing clients
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectRequestTester {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

    private String user = "";
    private String host = "";
    private String password = "";
    private int port = 7443;
    private UniFiProtectNvrThingConfig config = new UniFiProtectNvrThingConfig();

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        InputStream stream = this.getClass().getResourceAsStream("credentials.test");
        properties.load(stream);
        stream.close();
        user = properties.getProperty("user");
        password = properties.getProperty("password");
        host = properties.getProperty("host");
        UniFiProtectNvrThingConfig config = new UniFiProtectNvrThingConfig();
        config.setHost(host);
        config.setPort(port);
        config.setPassword(password);
        config.setUserName(user);
        config.setEventsTimePeriodLength(30000);
        Logger rootLogger = (Logger) LoggerFactory.getLogger("org.openhab");
        rootLogger.setLevel(Level.DEBUG);
        rootLogger = (Logger) LoggerFactory.getLogger("org.eclipse.jetty");
        rootLogger.setLevel(Level.WARN);
    }

    @Test
    @Ignore
    public void getBootstrap() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        nvr.getNvrUser();
        logger.debug("Fetch NvrUser: {}", nvr.getNvrUser());
    }

    @Test
    @Ignore
    public void getAnonSnapshot() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.getAnonSnapshot(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get());
    }

    @Test
    @Ignore
    public void getSnapshot() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.getSnapshot(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get());
    }

    @Test
    @Ignore
    public void getHeatmap() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        UniFiProtectCamera camera = cameraInsightCache.getCamera("7483C22FA4A5");
        if (camera != null) {
            nvr.getHeatmap(camera);
        }
    }

    @Test
    @Ignore
    public void getThumbnail() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        UniFiProtectCamera camera = cameraInsightCache.getCamera("7483C22FA4A5");
        if (camera != null) {
            nvr.getThumbnail(camera);
        }
    }

    @Test
    @Ignore
    public void setAlerts() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.turnOnOrOffAlerts(true);
    }

    @Test
    @Ignore
    public void setHighFpsMode() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.turnOnOrOffHighFpsMode(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get(),
                true);
    }

    @Test
    @Ignore
    public void setHDRMode() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.turnOnOrOffHdrMode(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get(), false);
    }

    @Test
    @Ignore
    public void setIrMode() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.setIrMode(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get(),
                UniFiProtectIrMode.ON);
    }

    @Test
    @Ignore
    public void setRecordingMode() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.setRecordingMode(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get(),
                UniFiProtectRecordingMode.MOTION);
    }

    @Test
    @Ignore
    public void setStatusLightOn() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.setStatusLightOn(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get(), true);
    }

    @Test
    @Ignore
    public void rebootCamera() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.rebootCamera(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get());
    }

    @Test
    @Ignore
    public void testCameraParser2() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.setStatusLightOn(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get(), false);
        nvr.rebootCamera(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get());
    }

    @SuppressWarnings("null")
    @Ignore
    public void testCameraParser() throws Exception {
        // HttpClient httpClient = new HttpClient();
        HttpClient httpClient = new HttpClient(new SslContextFactory(true));
        httpClient.setFollowRedirects(false);
        // Start HttpClient
        httpClient.start();
        UniFiProtectCameraInstanceCreator cameraInstanceCreator = new UniFiProtectCameraInstanceCreator();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .registerTypeAdapter(UniFiProtectCamera.class, cameraInstanceCreator).create();
        UniFiProtectLoginRequest request = new UniFiProtectLoginRequest(httpClient, config);
        request.sendRequest();

        String auth = request.getResponse().getHeaders().get("Authorization");
        //
        UniFiProtectRequest request2 = new UniFiProtectBootstrapRequest(httpClient, config,
                new UniFiProtectNvrType(Type.CLOUD_KEY_GEN2_PLUS, "Bearer " + auth, "Authorization"));
        // request2.setPath("/api/bootstrap");
        // request2.setHeader("Authorization", "Bearer " + auth);
        request2.sendRequest();
        String json = request.getResponse().getContentAsString();
        JsonObject jsonObject = UniFiProtectJsonParser.parseJson(gson, json);
        UniFiProtectCamera[] cameras = UniFiProtectJsonParser.getCamerasFromJson(gson, jsonObject);
        UniFiProtectNvrDevice nvrFromJson = UniFiProtectJsonParser.getNvrDeviceFromJson(gson, jsonObject);
    }

}