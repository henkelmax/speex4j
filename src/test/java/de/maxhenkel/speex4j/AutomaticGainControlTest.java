package de.maxhenkel.speex4j;

import de.maxhenkel.nativeutils.UnknownPlatformException;
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
        try (AutomaticGainControl agc = new AutomaticGainControl(frameSize, sampleRate)) {
            short[] shorts = new short[frameSize];
            short[] bufferBefore = new short[frameSize];
            short[] bufferAfter = new short[frameSize];
            for (int i = 0; i < shorts.length; i += bufferBefore.length) {
                System.arraycopy(shorts, i, bufferBefore, 0, bufferBefore.length);
                System.arraycopy(shorts, i, bufferAfter, 0, bufferAfter.length);
                agc.agc(bufferAfter);
                assertEquals(bufferBefore.length, bufferAfter.length);
                assertNotEquals(bufferBefore, bufferAfter);
            }
        }
    }

    @Test
    @DisplayName("Set valid target")
    void validTarget() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl agc = new AutomaticGainControl(960, 48000)) {
            agc.setTarget(1);
            agc.setTarget(8000);
            agc.setTarget(32768);
        }
    }

    @Test
    @DisplayName("Set invalid target")
    void invalidTarget() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl agc = new AutomaticGainControl(960, 48000)) {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> agc.setTarget(0));
            assertEquals("Invalid target (must be 1..32768)", e.getMessage());
            IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> agc.setTarget(32769));
            assertEquals("Invalid target (must be 1..32768)", e1.getMessage());
            IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> agc.setTarget(-1));
            assertEquals("Invalid target (must be 1..32768)", e2.getMessage());
            IllegalArgumentException e3 = assertThrows(IllegalArgumentException.class, () -> agc.setTarget(Short.MIN_VALUE));
            assertEquals("Invalid target (must be 1..32768)", e3.getMessage());
        }
    }

    @Test
    @DisplayName("Get target")
    void getTarget() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl denoiser = new AutomaticGainControl(960, 48000)) {
            assertEquals(8000, denoiser.getTarget());
            denoiser.setTarget(1);
            assertEquals(1, denoiser.getTarget());
            denoiser.setTarget(32768);
            assertEquals(32768, denoiser.getTarget());
        }
    }

    @Test
    @DisplayName("Set max gain")
    void setMaxGain() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl agc = new AutomaticGainControl(960, 48000)) {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> agc.setMaxGain(-1));
            assertEquals("Invalid max gain (must be >= 0)", e.getMessage());
            agc.setMaxGain(0);
            agc.setMaxGain(Short.MAX_VALUE);
        }
    }

    @Test
    @DisplayName("Get max gain")
    void getMaxGain() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl agc = new AutomaticGainControl(960, 48000)) {
            assertEquals(30, agc.getMaxGain());
            agc.setMaxGain(1);
            assertEquals(1, agc.getMaxGain());
            agc.setMaxGain(128);
            assertEquals(128, agc.getMaxGain());
        }
    }

    @Test
    @DisplayName("Set increment")
    void setIncrement() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl agc = new AutomaticGainControl(960, 48000)) {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> agc.setIncrement(-1));
            assertEquals("Invalid increment (must be >= 0)", e.getMessage());
            agc.setIncrement(0);
            agc.setIncrement(Short.MAX_VALUE);
        }
    }

    @Test
    @DisplayName("Get increment")
    void getIncrement() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl agc = new AutomaticGainControl(960, 48000)) {
            assertEquals(12, agc.getIncrement());
            agc.setIncrement(1);
            assertEquals(1, agc.getIncrement());
            agc.setIncrement(128);
            assertEquals(128, agc.getIncrement());
        }
    }

    @Test
    @DisplayName("Set decrement")
    void setDecrement() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl agc = new AutomaticGainControl(960, 48000)) {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> agc.setDecrement(1));
            assertEquals("Invalid decrement (must be <= 0)", e.getMessage());
            agc.setDecrement(0);
            agc.setDecrement(-128);
            agc.setDecrement(Short.MIN_VALUE);
        }
    }

    @Test
    @DisplayName("Get decrement")
    void getDecrement() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl agc = new AutomaticGainControl(960, 48000)) {
            assertEquals(-40, agc.getDecrement());
            agc.setDecrement(0);
            assertEquals(0, agc.getDecrement());
            agc.setDecrement(-1);
            assertEquals(-1, agc.getDecrement());
            agc.setDecrement(-128);
            assertEquals(-128, agc.getDecrement());
        }
    }

    @Test
    @DisplayName("Set VAD start probability")
    void setVadProbStart() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl agc = new AutomaticGainControl(960, 48000)) {
            IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> agc.setVadProbStart(-1));
            assertEquals("Invalid VAD start probability (must be 0..100)", e1.getMessage());
            IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> agc.setVadProbStart(101));
            assertEquals("Invalid VAD start probability (must be 0..100)", e2.getMessage());
            agc.setVadProbStart(0);
            agc.setVadProbStart(50);
            agc.setVadProbStart(100);
        }
    }

    @Test
    @DisplayName("Get VAD start probability")
    void getVadProbStart() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl agc = new AutomaticGainControl(960, 48000)) {
            assertEquals(35, agc.getVadProbStart());
            agc.setVadProbStart(0);
            assertEquals(0, agc.getVadProbStart());
            agc.setVadProbStart(50);
            assertEquals(50, agc.getVadProbStart());
            agc.setVadProbStart(100);
            assertEquals(100, agc.getVadProbStart());
        }
    }

    @Test
    @DisplayName("Set VAD continue probability")
    void setVadProbContinue() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl agc = new AutomaticGainControl(960, 48000)) {
            IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> agc.setVadProbContinue(-1));
            assertEquals("Invalid VAD continue probability (must be 0..100)", e1.getMessage());
            IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> agc.setVadProbContinue(101));
            assertEquals("Invalid VAD continue probability (must be 0..100)", e2.getMessage());
            agc.setVadProbContinue(0);
            agc.setVadProbContinue(50);
            agc.setVadProbContinue(100);
        }
    }

    @Test
    @DisplayName("Get VAD continue probability")
    void getVadProbContinue() throws IOException, UnknownPlatformException {
        try (AutomaticGainControl agc = new AutomaticGainControl(960, 48000)) {
            assertEquals(20, agc.getVadProbContinue());
            agc.setVadProbContinue(0);
            assertEquals(0, agc.getVadProbContinue());
            agc.setVadProbContinue(50);
            assertEquals(50, agc.getVadProbContinue());
            agc.setVadProbContinue(100);
            assertEquals(100, agc.getVadProbContinue());
        }
    }

}
