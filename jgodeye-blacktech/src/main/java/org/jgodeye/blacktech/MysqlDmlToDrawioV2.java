package org.jgodeye.blacktech;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;


/**
 * mysql dml 导入 draw.io
 */
public class MysqlDmlToDrawioV2 {

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
            if (StringUtils.containsIgnoreCase(targetLine, "CREATE TABLE")) {
                outputContent.add(targetLine);
            }
            else if (StringUtils.containsIgnoreCase(targetLine, "ENGINE")) {
                handleEngineLine(targetLine, outputContent);
            }
            else {
                handleFieldLine(targetLine, outputContent);
            }
        }

        FileUtils.writeLines(targetFile, outputContent);
    }

    private static void handleEngineLine(String targetLine, List<String> outputContent) {
        if (!StringUtils.containsIgnoreCase(targetLine, "COMMENT")) {
            outputContent.add(");");
        }
        String table = Arrays.stream(outputContent.get(0).split(" ")).filter(t -> t.contains("_")).findAny().get();
        String title = targetLine.split("'")[1];
        outputContent.remove(0);
        outputContent.add(0, "CREATE TABLE " + table + "(" + title + ")" + " (");
        outputContent.add(");");
    }

    private static void handleFieldLine(String targetLine, List<String> outputContent) {
        String[] lineSplit = targetLine.split("COMMENT '");
        if (lineSplit.length <= 1) {
            return;
        }
        String fieldName = lineSplit[0].trim().split(" ")[0];
        String comment = lineSplit[1].split("'")[0];
        if (StringUtils.isNotEmpty(comment)) {
            targetLine = "+ " + fieldName + "(" + comment + "),";
        } else {
            targetLine = "+ " + fieldName + ",";
        }
        outputContent.add(targetLine);
    }
}