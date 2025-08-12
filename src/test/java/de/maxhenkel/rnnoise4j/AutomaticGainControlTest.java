package de.maxhenkel.rnnoise4j;

import de.maxhenkel.speex4j.AutomaticGainControl;
import de.maxhenkel.speex4j.UnknownPlatformException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AutomaticGainControlTest {

    @Test
    @DisplayName("AGC")
    void agc() throws IOException, UnknownPlatformException {
        int frameSize = 960;
        int sampleRate = 48000;
        try (AutomaticGainControl denoiser = new AutomaticGainControl(frameSize, sampleRate)) {
            short[] shorts = new short[frameSize];
            short[] bufferBefore = new short[frameSize];
            short[] bufferAfter = new short[frameSize];
            for (int i = 0; i < shorts.length; i += bufferBefore.length) {
                System.arraycopy(shorts, i, bufferBefore, 0, bufferBefore.length);
                System.arraycopy(shorts, i, bufferAfter, 0, bufferAfter.length);
                denoiser.agc(bufferAfter);
                assertEquals(bufferBefore.length, bufferAfter.length);
                assertNotEquals(bufferBefore, bufferAfter);
            }
        }
    }

    @Test
    @DisplayName("Set valid target")
    void validTarget() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl denoiser = new AutomaticGainControl(960, 48000)) {
            denoiser.setTarget(1);
            denoiser.setTarget(8000);
            denoiser.setTarget(32768);
        }
    }

    @Test
    @DisplayName("Set invalid target")
    void invalidTarget() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl denoiser = new AutomaticGainControl(960, 48000)) {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> denoiser.setTarget(0));
            assertEquals("Invalid target (must be 1..32768)", e.getMessage());
            IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> denoiser.setTarget(32769));
            assertEquals("Invalid target (must be 1..32768)", e1.getMessage());
            IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> denoiser.setTarget(-1));
            assertEquals("Invalid target (must be 1..32768)", e2.getMessage());
            IllegalArgumentException e3 = assertThrows(IllegalArgumentException.class, () -> denoiser.setTarget(Short.MIN_VALUE));
            assertEquals("Invalid target (must be 1..32768)", e3.getMessage());
        }
    }

}
