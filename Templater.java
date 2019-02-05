import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

class Templater {
    public static void main(String[] args) throws IOException {

        String snippetDir = args[0];
        String templateDir = args[1];
        String outputDir = args[2];

        File templateFolder = new File(templateDir);
        File[] templateFiles = templateFolder.listFiles();

        for (File file: templateFiles){
            generate(snippetDir, templateDir + "/" + file.getName(), outputDir + "/" + file.getName());
        }

    }

    public static void generate(String snippetDir, String templateFilepath, String outputFilePath) throws IOException{
        // create a stream to the file passed in:
        BufferedReader reader = new BufferedReader(new FileReader(templateFilepath));

        StringBuilder document = new StringBuilder();

        int state = 0;
        StringBuilder snippetFilename = new StringBuilder();
        int chrCount = 0;
        for (int chr = reader.read(); chr != -1; chr = reader.read()) {

            document.append((char) chr);

            switch (state) {
            case 0:
                if (chr == (int) '[') {
                    state = 1;
                }
                break;

            case 1:
                if (chr == (int) '!') {
                    state = 2;
                } else {
                    state = 0;
                }
                break;

            case 2:
                if (chr == (int) ']') {
                    state = 3;
                } else {
                    snippetFilename.append((char) chr);
                }
                break;
            case 3:
                document.replace(chrCount - snippetFilename.length() - 3, chrCount, "");
                chrCount = chrCount - snippetFilename.length() - 3;

                // then append the snippet
                // then add the length of the snippet to the chrcount
                String snippet = getSnippet(snippetDir + "/" + snippetFilename.toString() + ".snip");
                document.append(snippet.toString());
                chrCount += snippet.length();

                snippetFilename.setLength(0);
                state = 0;
            }

            chrCount++;
        }

        reader.close();

        // write generated document to a new file:
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

        writer.write(document.toString());
        writer.close();
    }

    public static String getSnippet(String filepath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filepath));

        StringBuilder snippet = new StringBuilder();

        for (int chr = reader.read(); chr != -1; chr = reader.read()) {
            snippet.append((char) chr);
        }

        reader.close();

        return snippet.toString();
    }
}