/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.unifiprotect.internal.thing;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link UniFiProtectG4CameraThingConfig}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectG4CameraThingConfig extends UniFiProtectBaseThingConfig {
    @Override
    public String toString() {
        return "UniFiProtectG4CameraThingConfig [mac=" + mac + "]";
    }
}
