import com.mifmif.common.regex.Generex;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateInputsAndOutputs {
    private static String inputsPath = "C:\\Users\\lilac\\Documents\\Compilation\\compilation\\ex1\\input\\";
    private static String outputsPath = "";
    private static String ex1PathLinux = "/mnt/c/users/lilac/Documents/Compilation/compilation/ex1";
    private static int numOfStrings = 10;
    private static int numOfInputs = 1;

    private static final List<Generex> generexes = new ArrayList<>();

    public static void main(String[] args){
        CreateInputs();
        CreateOutputs();
    }

    private static void CreateInputs() {
//        deleteFolderContent(new File(inputsPath));

        // TODO: Add generex for each regex
        generexes.add(new Generex("[\\(\\)\\{\\}\\[\\]+\\-*/;.?!]|[a-zA-Z]|[0-9]"));
        generexes.add(new Generex("\\\"[a-zA-Z]*\\\""));
        generexes.add(new Generex("[a-zA-Z]+[0-9a-zA-Z]*"));

        createRandomInputs(numOfInputs);
    }

    public static void CreateOutputs() {
//        deleteFolderContent(new File(outputsPath));
        // TODO: Find a way to execute "make" from java on different files
    }

    private static void createRandomInputs(int numOfInputs) {
        String filename;

        for (int i = 0; i < numOfInputs; i++) {
            filename = "Input" + Integer.toString(i) + ".txt";
            createInputFile(filename);
        }
    }

    private static void createInputFile(String filename) {
        try {
            File inputFile = new File(inputsPath + filename);
            if (inputFile.createNewFile()) {
                try (FileWriter fileWriter = new FileWriter(inputFile)) {
                    String randomInput = getRandomInput();
                    System.out.println(randomInput);
                    fileWriter.write(randomInput);
                }
            }
            else {
                System.out.println("File already exists.");
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static String getRandomInput() {
        Random rand = new Random();
        StringBuilder randomInput = new StringBuilder();

        for (int i = 0; i < numOfStrings; i++) {
            Generex randomGenerex = generexes.get(rand.nextInt(generexes.size()));
            String randomString = randomGenerex.random();
            randomInput.append(randomString);
        }

        return randomInput.toString();
    }

    public static void deleteFolderContent(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f: files) {
                if(f.isDirectory()) {
                    deleteFolderContent(f);
                }
                else {
                    f.delete();
                }
            }
        }
    }
}
