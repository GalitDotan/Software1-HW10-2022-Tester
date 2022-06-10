package il.ac.tau.cs.sw1.bufferedIO;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class MyBufferReaderTester {
    public static final String INPUT_FOLDER = "resources/";

    public static final String FILE1 = INPUT_FOLDER + "rocky1.txt";
    public static final String FILE2 = INPUT_FOLDER + "f1.txt";
    public static final String FILE3 = INPUT_FOLDER + "f2.txt";
    public static final String FILE4 = INPUT_FOLDER + "rocky2.txt";
    public static final String FILE5 = INPUT_FOLDER + "rocky3.txt";

    public static final String TEXT1 = "Now somewhere in the Black mining Hills of Dakota\n" +
            "There lived a young boy named Rocky Raccoon, \n" +
            "And one day his woman ran off with another guy, \n" +
            "Hit young Rocky in the eye.\n" +
            "Rocky didn't like that\n" +
            "\n" +
            "He said, I'm gonna get that boy.\n" +
            "So one day he walked into town\n" +
            "Booked himself a room in the local saloon.\n" +
            "in the rocky rocky\n";

    public static final String TEXT2 = "a\n" +
            "b\n" +
            "b\n" +
            "c\n" +
            "d";

    public static final String TEXT3 = "a\n" +
            "b\n" +
            "b\n" +
            "c\n" +
            "d\n" +
            "\n";

    @Test
    public void testBF() throws IOException {
        this.testCompareWithBufferedReader(FILE1, true);
        this.testCompareWithBufferedReader(FILE2, false);
        this.testCompareWithBufferedReader(FILE3, true);
        this.testCompareWithBufferedReader(FILE4, true);
        this.testCompareWithBufferedReader(FILE5, true);
    }

    public void testCompareWithBufferedReader(String filename, boolean endNL) throws IOException {
        int[] checkBuffers = {3, 10, 1000, 1};
        for (int bufferSize : checkBuffers) {
            FileReader fReader1 = new FileReader(new File(filename));
            FileReader fReader2 = new FileReader(new File(filename));
            IBufferedReader myBF = new MyBufferReader(fReader1, bufferSize);
            BufferedReader otherBF = new BufferedReader(fReader2, bufferSize);
            String expected = otherBF.readLine();
            String actual = myBF.getNextLine();
            while (expected != null) {
                assertEquals(expected, actual);
                expected = otherBF.readLine();
                actual = myBF.getNextLine();
            }
            if (endNL) {
                assertEquals("", actual);
            } else {
                assertNull(actual);
            }


            // after end of file - returns null
            for (int i = 0; i < 5; i++) {
                actual = myBF.getNextLine();
                assertNull(actual);
            }

            fReader1.close();
            myBF.close();
            otherBF.close();
        }
    }

    public void testLines(String filename, String text) throws IOException {
        String[] linesArr = text.split("\n");
        ArrayList<String> lines = new ArrayList<String>(Arrays.asList(linesArr));
        if (text.charAt(text.length() - 1) == '\n') {
            lines.add("");
        }

        int[] checkBuffers = {3, 10, 1000, 1};
        for (int bufferSize : checkBuffers) {
            FileReader fReader = new FileReader(new File(filename));
            IBufferedReader bR = new MyBufferReader(fReader, bufferSize);
            for (int i = 0; i < lines.size(); i++) {
                String expected = lines.get(i);
                String actual = bR.getNextLine();
                assertEquals(expected, actual);
            }

            // make sure next call is returning null
            String actual = bR.getNextLine();
            assertNull(actual);
            fReader.close();
            bR.close();
        }
    }

    @Test
    public void testMain() throws IOException {
        this.testLines(FILE1, TEXT1);
        this.testLines(FILE2, TEXT2);
        this.testLines(FILE3, TEXT3);
        this.testLines(FILE4, TEXT1); // same text as 1
    }
}
