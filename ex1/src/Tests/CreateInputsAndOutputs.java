import com.mifmif.common.regex.Generex;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateInputsAndOutputs {
    private static String inputsPath = "C:\\Stav\\TAU\\Projects\\Compilation\\compilation\\ex1\\input\\";
    private static String outputsPath = "";
    private static String ex1PathLinux = "/mnt/c/Stav/TAU/Projects/Compilation/compilation/ex1";
    private static int numOfStrings = 10;
    private static int numOfInputs = 1;

    private static final List<Generex> generexes = new ArrayList<>();

    public static void main(String[] args){
        CreateInputs();
        CreateOutputs();
    }

    private static void CreateInputs() {

        deleteFolderContent(new File(inputsPath));
        //********** Regex Definitions ***********/
        // String
        generexes.add(new Generex("\\\"[a-zA-Z]*\\\""));
        // ID
        generexes.add(new Generex("[a-zA-Z]+[0-9a-zA-Z]*"));
        // INT
        generexes.add(new Generex("[0-9]+"));
        // Comment type 1
        generexes.add(new Generex("//[+-*/;.?!a-zA-Z0-9(){}\\[\\]]*[\n\r]"));
        // Comment type 2
        // TODO: notice that it can generate "/*____*/___*/" which is NOT valid
        generexes.add(new Generex("/\\*[+-*/;.?!a-zA-Z0-9(){}\\[\\]\n\r]*\\*/"));
        // general regexes
        generexes.add(new Generex("[ \t]"));
        generexes.add(new Generex("[\r\n]"));
        generexes.add(new Generex("[(]"));
        generexes.add(new Generex("[)]"));
        generexes.add(new Generex("[{]"));
        generexes.add(new Generex("[}]"));
        generexes.add(new Generex("[\\[]"));
        generexes.add(new Generex("[\\]]"));
        generexes.add(new Generex("nil"));
        generexes.add(new Generex("+"));
        generexes.add(new Generex("-"));
        generexes.add(new Generex("*"));
        generexes.add(new Generex("/"));
        generexes.add(new Generex(","));
        generexes.add(new Generex("\\."));
        generexes.add(new Generex(";"));
        generexes.add(new Generex("int"));
        generexes.add(new Generex("string"));
        generexes.add(new Generex("void"));
        generexes.add(new Generex(":="));
        generexes.add(new Generex("="));
        generexes.add(new Generex(">"));
        generexes.add(new Generex("\\<"));
        generexes.add(new Generex("array"));
        generexes.add(new Generex("class"));
        generexes.add(new Generex("extends"));
        generexes.add(new Generex("return"));
        generexes.add(new Generex("while"));
        generexes.add(new Generex("if"));
        generexes.add(new Generex("new"));

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

        // TODO: remove comment
        for (int i = 0; i < numOfStrings; i++) {
            Generex randomGenerex = generexes.get(rand.nextInt(generexes.size()));
            String randomString = randomGenerex.random();
            randomInput.append(randomString);
        }
        return randomInput.toString();

        // TODO: delete later (for checking regex
        /*for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 35; j++) {
                Generex randomGenerex = generexes.get(j);
                String randomString = randomGenerex.random();
                randomInput.append(randomString);
                randomInput.append("\n");
            }
        }
        // check comment type 2
        for (int i = 0; i < 3; i++) {
            Generex randomGenerex = generexes.get(4);
            String randomString = randomGenerex.random();
            randomInput.append(randomString);
            randomInput.append("\n");
        }*/
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
