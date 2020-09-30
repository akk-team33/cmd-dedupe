package de.team33.cmds.dedupe.patterns;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import static java.lang.String.format;

public class Resources {

    private static final String NOT_LOAD = "could not load <%s> referenced by <%s>";

    public static String load(final Class<?> refClass, final String resourcePath) {
        //noinspection OverlyBroadCatchBlock
        try (final InputStream in = refClass.getResourceAsStream(resourcePath)) {
            return load(in);
        } catch (final Exception e) {
            throw new IllegalStateException(format(NOT_LOAD, resourcePath, refClass), e);
        }
    }

    private static String load(final InputStream in) throws IOException {
        try (final Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            return load(reader);
        }
    }

    private static String load(final Reader reader) throws IOException {
        final StringBuilder result = new StringBuilder(0);
        final char[] buffer = new char[512];
        for (int count = reader.read(buffer); 0 < count; count = reader.read(buffer)) {
            result.append(buffer, 0, count);
        }
        return result.toString();
    }
}
