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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectDiscoveryService}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectDiscoveryService extends AbstractDiscoveryService {

    private static final String UNIFI_PROTECT_VENDOR = "UniFi Protect";
    private static final int TIMEOUT = 30;
    private UniFiProtectNvrThingHandler bridge;
    // private ScheduledFuture<?> scanningJob;

    private final Logger logger = LoggerFactory.getLogger(UniFiProtectDiscoveryService.class);

    public UniFiProtectDiscoveryService(UniFiProtectNvrThingHandler bridge) throws IllegalArgumentException {
        super(UniFiProtectBindingConstants.SUPPORTED_DEVICE_THING_TYPES_UIDS, TIMEOUT);
        this.bridge = bridge;
        new UniFiProtectScan();
        logger.debug("Initializing UniFiProtect Discovery Nvr: {}", bridge);
        activate(null);
    }

    @SuppressWarnings({ "null", "unused" })
    @Override
    protected void startScan() {

        if (bridge == null) {
            logger.debug("Can't start scanning for devices, UniFiProtect bridge handler not found!");
            return;
        }

        if (!bridge.getThing().getStatus().equals(ThingStatus.ONLINE)) {
            logger.debug("Bridge is OFFLINE, can't scan for devices!");
            return;
        }

        if (bridge.getNvr() == null) {
            logger.debug("Failed to start discovery scan due to no nvr exists in the bridge");
            return;
        }

        logger.debug("Starting scan of UniFiProtect Server {}", bridge.getThing().getUID());

        bridge.getNvr().getCameraInsightCache().getCameras()
                .forEach(camera -> logger.debug("Found Camera: {}", camera));

        for (Thing thing : bridge.getThing().getThings()) {
            if (thing instanceof UniFiProtectCamera) {
                logger.debug("Found existing camera already!");
            }
        }
        ThingUID bridgeUid = bridge.getThing().getUID();
        for (UniFiProtectCamera camera : bridge.getNvr().getCameraInsightCache().getCameras()) {
            ThingUID thingUID = new ThingUID(UniFiProtectBindingConstants.THING_TYPE_CAMERA, bridgeUid,
                    camera.getMac());

            DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withLabel(camera.getName())
                    .withBridge(bridgeUid).withProperty(Thing.PROPERTY_VENDOR, UNIFI_PROTECT_VENDOR)
                    .withProperty(UniFiProtectBindingConstants.CAMERA_PROP_HOST, camera.getHost())
                    .withProperty(UniFiProtectBindingConstants.CAMERA_PROP_MAC, camera.getMac())
                    .withProperty(UniFiProtectBindingConstants.CAMERA_PROP_NAME, camera.getName()).build();

            thingDiscovered(discoveryResult);
        }
    }

    @Override
    protected synchronized void stopScan() {
        super.stopScan();
        removeOlderResults(getTimestampOfLastScan());
    }

    @Override
    protected void startBackgroundDiscovery() {
        /* Not Implemented */
    }

    // @Override
    // protected void stopBackgroundDiscovery() {
    // if (scanningJob != null && !scanningJob.isCancelled()) {
    // scanningJob.cancel(false);
    // scanningJob = null;
    // }
    // }

    @SuppressWarnings("null")
    @NonNullByDefault
    public class UniFiProtectScan implements Runnable {
        @Override
        public void run() {
            startScan();
        }
    }
}
