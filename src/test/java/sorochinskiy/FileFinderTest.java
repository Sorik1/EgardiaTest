package sorochinskiy;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class FileFinderTest {

    private FileFinder finder;
    private Set<String> testKeySet;


    @Before
    public void setUp() throws Exception {
        finder = new FileFinder();
        testKeySet = new HashSet<>();
        testKeySet.add("00050A0112AC");
        testKeySet.add("00050A010203");
        testKeySet.add("00050A01EFAC");
    }

    @Test
    public void testCreateHwAddressToImgEntriesMap() throws Exception {
        String directoryPath = getClass().getResource("/data").toString().substring(6);
        Map<String, List<ImgEntry>> imgsMap = finder.createHwAddressToImgEntriesMap(directoryPath);
        assertNotNull(imgsMap);
        assertEquals(testKeySet, imgsMap.keySet());

    }

    @Test
    public void testToString() throws Exception {
        String directoryPath = getClass().getResource("/data").toString().substring(6);
        Scanner scaner = new Scanner(getClass().getResourceAsStream("/testOutput.txt"));
        StringBuilder testOutput = new StringBuilder();
        while (scaner.hasNext()) {
            testOutput.append(scaner.nextLine()).append("\n");
        }
        testOutput.delete(testOutput.length()-1, testOutput.length());
        finder.createHwAddressToImgEntriesMap(directoryPath);
        assertEquals(testOutput.toString(), finder.toString());
    }

    @Test(expected = NoSuchFileException.class)
    public void testCreateMapFileNotFoundException() throws IOException {
        String directoryPath = "dummyPath";
        finder.createHwAddressToImgEntriesMap(directoryPath);
    }

    @Test
    public void testNoFilesMatched() throws IOException{
        String directoryPath = getClass().getResource("/dataNoMatchedFiles").toString().substring(6);
        Map<String, List<ImgEntry>> imgsMap = finder.createHwAddressToImgEntriesMap(directoryPath);
        assertNotNull(imgsMap);
        assertTrue(imgsMap.isEmpty());
    }

    @Test
    public void testNoFilesMatchedToString() throws IOException {
        String directoryPath = getClass().getResource("/dataNoMatchedFiles").toString().substring(6);
        finder.createHwAddressToImgEntriesMap(directoryPath);
        assertEquals(FileFinder.FILES_NOT_FOUND_MESSAGE + directoryPath, finder.toString());
    }

    @Test
    public void testMapNotInitialized() throws IOException {
        assertEquals(FileFinder.ERROR_NOT_INITIALISED_MESSAGE, finder.toString());
    }

}