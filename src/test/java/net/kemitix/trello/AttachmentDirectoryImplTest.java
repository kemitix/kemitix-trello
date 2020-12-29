package net.kemitix.trello;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class AttachmentDirectoryImplTest
        implements WithAssertions {

    AttachmentDirectoryImpl attachmentDirectory =
            new AttachmentDirectoryImpl();

    @BeforeEach
    public void setUp() throws IOException {
        attachmentDirectory.init();
    }

    @Test
    @DisplayName("Creates safe filenames")
    public void createsSafeFilenames() {
        //given
        String filename = "x\\y";
        //when
        File result = attachmentDirectory.createFile(new File(filename));
        //then
        assertThat(result.getName()).isEqualTo("xy");
    }
}
