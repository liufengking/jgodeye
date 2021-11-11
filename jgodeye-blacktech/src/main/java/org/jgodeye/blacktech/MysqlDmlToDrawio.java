package org.jgodeye.blacktech;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jgodeye.common.ExceptionQuietly;


/**
 * mysql dml 导入 draw.io
 */
public class MysqlDmlToDrawio {

    public static void main(String[] args) {

        ExceptionQuietly.call(() -> {

            InputStream inputStream = SequenceDiagramParser.class.getResourceAsStream("/conf.properties");
            Properties prop = new Properties();
            prop.load(inputStream);

            String input = prop.getProperty("mysql.dml.to.draw.io.input");
            String output = prop.getProperty("mysql.dml.to.draw.io.output");

            generator(input, output);
        });
    }

    private static void generator(String input, String output) throws IOException {
        File sourceFile = new File(input);
        File targetFile = new File(output);

        FileUtils.write(targetFile, "");

        List<String> inputContent = FileUtils.readLines(sourceFile);
        List<String> outputContent = new ArrayList<>();

        for (String inputLine : inputContent) {

            String targetLine = inputLine.replaceAll("`", "");
            if (targetLine.contains("idx") || targetLine.contains("PRIMARY KEY") || targetLine.contains("UNIQUE")) {
                continue;
            }

            if (targetLine.contains("CREATE TABLE")) {
                handleCreateTableLine(targetLine, outputContent);
            }
            else if (targetLine.contains("ENGINE")) {
                handleEngineLine(outputContent);
            }
            else {
                handleFieldLine(targetLine, outputContent);
            }
        }

        FileUtils.writeLines(targetFile, outputContent);
    }

    private static void handleCreateTableLine(String targetLine, List<String> outputContent) {
        outputContent.add(targetLine);
    }

    private static void handleEngineLine(List<String> outputContent) {
        outputContent.add(");");
    }

    private static void handleFieldLine(String targetLine, List<String> outputContent) {
        boolean isPk = targetLine.contains("AUTO_INCREMENT");
        String[] blankSplit = StringUtils.split(targetLine, " ");
        if (isPk) {
            targetLine = "\t" + blankSplit[0] + " " + blankSplit[1] + " PRIMARY KEY,";
        }
        else {
            targetLine = "\t" + blankSplit[0] + " " + blankSplit[1] + ",";
        }
        outputContent.add(targetLine);
    }
}