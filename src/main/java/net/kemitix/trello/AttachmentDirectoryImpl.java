package net.kemitix.trello;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AttachmentDirectoryImpl implements AttachmentDirectory {

    private static final Logger LOG =
            Logger.getLogger(
                    AttachmentDirectoryImpl.class.getName());
    private static final String ILLEGAL_CHARS = "[\\\\/:*?\"<>|]";

    private Path dir;
    private List<File> toDelete = new ArrayList<>();

    public void init() throws IOException {
        dir = Files.createTempDirectory("attachments");
        LOG.info("Attachments directory: " + dir);
    }

    @Override
    public File createFile(File fileName) {
        String cleanFilename = fileName.getName()
                .replaceAll(ILLEGAL_CHARS, "");
        File file = dir.resolve(cleanFilename).toFile();
        LOG.info("Created attachment: " + file);
        toDelete.add(file);
        return file;
    }

    public void deleteFiles() {
        toDelete.stream()
                .peek(file -> LOG.info("Deleting: " + file))
                .map(File::delete)
                .filter(deleted -> !deleted)
                .forEach(r -> LOG.warning("Could not delete file"));
        if (dir.toFile().delete()) {
            LOG.info("Deleted directory: " + dir);
        } else {
            LOG.warning("Could not delete directory: " + dir);
        }
    }
}
