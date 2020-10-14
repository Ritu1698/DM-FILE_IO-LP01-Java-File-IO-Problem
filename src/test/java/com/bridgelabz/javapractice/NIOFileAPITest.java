
package com.bridgelabz.javapractice;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class NIOFileAPITest {
    private static final String HOME = System.getProperty("user.home");
    private static final String PLAY_WITH_NOI = "TempPlayGround";

    @Test
    public void givenPath_whenChecked_thenConfirm() throws IOException {
        Path playPath = Paths.get(HOME + "/" + PLAY_WITH_NOI);
        if (Files.exists(playPath)) FileUtils.deleteFiles(playPath.toFile());
        Assert.assertTrue(Files.notExists(playPath));

        //Create Directory
        Files.createDirectory(playPath);
        Assert.assertTrue(Files.exists(playPath));

        //Create Files To Directory
        IntStream.range(1, 10).forEach(fileIndex -> {
            Path tempFile = Paths.get(playPath + "/temp" + fileIndex);
            Assert.assertTrue(Files.notExists(tempFile));
            try {
                Files.createFile(tempFile);
            } catch (IOException e) {
                Assert.assertTrue(Files.exists(tempFile));
            }
        });

        //List Files, Directories as well as Files with Extension
        Files.list(playPath).filter(Files::isRegularFile).forEach(System.out::println);
        Files.newDirectoryStream(playPath).forEach(System.out::println);
        Files.newDirectoryStream(playPath, path -> path.toFile().isFile() && path.toString().startsWith("temp")).forEach(System.out::println);
    }
}
