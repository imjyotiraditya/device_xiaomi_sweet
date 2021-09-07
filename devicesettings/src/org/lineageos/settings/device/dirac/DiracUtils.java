/*
 * Copyright (C) 2018,2020 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings.device.dirac;

import android.content.Context;
import android.media.AudioManager;


public final class DiracUtils {

    protected static DiracSound sDiracSound;
    private static boolean sInitialized;
    private static Context sContext;

    public static void initialize(Context context) {
        if (!sInitialized) {
            sContext = context;
            sDiracSound = new DiracSound(0, 0);
            sInitialized = true;
        }
    }

    protected static void setMusic(boolean enable) {
        sDiracSound.setMusic(enable ? 1 : 0);
    }

    protected static boolean isDiracEnabled() {
        return sDiracSound != null && sDiracSound.getMusic() == 1;
    }

    protected static void setLevel(String preset) {
        String[] level = preset.split("\\s*,\\s*");

        for (int band = 0; band <= level.length - 1; band++) {
            sDiracSound.setLevel(band, Float.valueOf(level[band]));
        }
    }

    protected static void setHeadsetType(int paramInt) {
        sDiracSound.setHeadsetType(paramInt);
    }

    protected static boolean getHifiMode() {
        AudioManager audioManager = sContext.getSystemService(AudioManager.class);
        return audioManager.getParameters("hifi_mode").contains("true");
    }

    protected static void setHifiMode(int paramInt) {
        AudioManager audioManager = sContext.getSystemService(AudioManager.class);
        audioManager.setParameters("hifi_mode=" + (paramInt == 1));
        sDiracSound.setHifiMode(paramInt);
    }
}
