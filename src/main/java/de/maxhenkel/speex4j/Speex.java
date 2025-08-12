package de.maxhenkel.speex4j;

import java.io.IOException;

class Speex {

    private static boolean loaded;
    private static Exception error;

    public static void load() throws UnknownPlatformException, IOException {
        if (loaded) {
            if (error != null) {
                if (error instanceof IOException) {
                    throw (IOException) error;
                } else if (error instanceof UnknownPlatformException) {
                    throw (UnknownPlatformException) error;
                }
                throw new RuntimeException(error);
            }
            return;
        }
        try {
            LibraryLoader.load("speex4j");
            loaded = true;
        } catch (UnknownPlatformException | IOException e) {
            error = e;
            throw e;
        }
    }

    public static boolean isLoaded() {
        return loaded && error == null;
    }

}
