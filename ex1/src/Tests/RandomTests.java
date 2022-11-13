import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

public class RandomTests {
    private static String expectedOutputsPath = "C:\\Users\\lilac\\Documents\\Compilation\\compilation\\ex1\\input\\";
    private static String outputsPath = "C:\\Users\\lilac\\Documents\\Compilation\\compilation\\ex1\\output\\";

    @Test
    void runTests() {
        int numOfExpectedOutput = Objects.requireNonNull((new File(expectedOutputsPath)).listFiles()).length;
        int numOfOutputs = Objects.requireNonNull((new File(outputsPath)).listFiles()).length;
        assertThat(numOfExpectedOutput).isEqualTo(numOfOutputs);

        String filename;
        File expectedOutputFile, outputFile;
        for (int i = 0; i < numOfExpectedOutput; i++) {
            filename = "Output" + i + ".txt";

            expectedOutputFile = new File(expectedOutputsPath + filename);
            outputFile = new File(outputsPath + filename);

            assertThat(expectedOutputFile).hasSameTextualContentAs(outputFile);
        }
    }
}
