package de.team33.cmds.dedupe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

class Config {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeHierarchyAdapter(Path.class, new PathAdapter())
            .create();

    private List<Path> roots;
    private List<Path> ignore;
    private Path trash;

    static Config read(final Path path) {
        //noinspection OverlyBroadCatchBlock
        try (final Reader reader = Files.newBufferedReader(path, UTF_8)) {
            return GSON.fromJson(reader, Config.class);
        } catch (final Exception e) {
            throw new ImproperArgumentException(path, e);
        }
    }

    static Config init(final Path path) {
        final Config result = new Config();
        final Path root = path.getParent();
        result.roots = singletonList(root);
        result.trash = root.resolve("(trash)");
        result.ignore = asList(
                path,
                result.trash
        );
        //noinspection OverlyBroadCatchBlock
        try (final Writer writer = Files.newBufferedWriter(path, UTF_8, StandardOpenOption.CREATE)) {
            GSON.toJson(result, writer);
        } catch (final Exception e) {
            throw new ImproperArgumentException(path, e);
        }
        return result;
    }

    @Override
    public final String toString() {
        return GSON.toJson(this);
    }

    private static class PathAdapter extends TypeAdapter<Path> {
        @Override
        public final void write(final JsonWriter out, final Path value) throws IOException {
            out.value(value.toString());
        }

        @Override
        public final Path read(final JsonReader in) throws IOException {
            return Paths.get(in.nextString());
        }
    }
}
