package org.jgodeye.blacktech;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;


/**
 * mysql dml 导入 draw.io
 */
public class MysqlCommentsToDrawio {

    public static void main(String[] args) throws Exception {
        InputStream inputStream = SequenceDiagramParser.class.getResourceAsStream("/conf.properties");
        Properties prop = new Properties();
        prop.load(inputStream);

        String input = prop.getProperty("mysql.dml.to.draw.io.input");
        String output = prop.getProperty("mysql.dml.to.draw.io.output");

        generator(input, output);
    }

    private static void generator(String input, String output) throws IOException {
        File sourceFile = new File(input);
        File targetFile = new File(output);

        FileUtils.write(targetFile, "");

        List<String> inputContent = FileUtils.readLines(sourceFile);
        List<String> outputContent = new ArrayList<>();

        for (String inputLine : inputContent) {
            String targetLine = inputLine.replaceAll("`", "");
            if (targetLine.contains("CREATE TABLE")) {
                outputContent.add(targetLine);
            }
            else if (targetLine.contains("ENGINE")) {
                handleEngineLine(targetLine, outputContent);
            }
            else {
                handleFieldLine(targetLine, outputContent);
            }
        }

        FileUtils.writeLines(targetFile, outputContent);
    }

    private static void handleEngineLine(String targetLine, List<String> outputContent) {
        if (!targetLine.contains("COMMENT")) {
            outputContent.add(");");
        }
        String title = targetLine.split("'")[1];
        outputContent.remove(0);
        outputContent.add(0, "CREATE TABLE " + title + " (");
        outputContent.add(");");
    }

    private static void handleFieldLine(String targetLine, List<String> outputContent) {
        String[] lineSplit = targetLine.split("COMMENT '");
        if (lineSplit.length <= 1) {
            return;
        }
//        targetLine = "\t" + "+ " + lineSplit[1].split("'")[0] + ",";
        targetLine = "+ " + lineSplit[1].split("'")[0] + ",";
        outputContent.add(targetLine);
    }
}