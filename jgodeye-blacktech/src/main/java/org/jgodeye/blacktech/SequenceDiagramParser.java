package org.jgodeye.blacktech;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharUtils;
import org.jgodeye.common.ExceptionQuietly;

/**
 * idea SequenceDiagram插件解析器
 * 生成markdown文件,可树状静态分析调用链
 */
public class SequenceDiagramParser {

    private static final char LEFT_BRACKET = '(';
    private static final char RIGHT_BRACKET = ')';

    // 这里可定义不关心的分支
    private static final String[] FILTER_STRS = new String[] {
        "exception", "enums", "logger", "util", "Enum",
        "\"get\"", "\"set\"", "toString",
    };

    public static void main(String[] args) {

        ExceptionQuietly.call(() -> {

            InputStream inputStream = SequenceDiagramParser.class.getResourceAsStream("/conf.properties");
            Properties prop = new Properties();
            prop.load(inputStream);

            String input = prop.getProperty("sequence.diagram.parser.input");
            String output = prop.getProperty("sequence.diagram.parser.output");

            generator(input, output);
        });
    }

    public static void generator(String input, String output) throws Exception {

        File soureFile = new File(input);
        File targeFile = new File(output);

        // 清空
        FileUtils.write(targeFile, "");

        String content = FileUtils.readFileToString(soureFile);

        // 括号与位置
        List<Map<Character, Integer>> bracketList = getBracketIndex(content);

        // 获取解析后的行
        List<String> lines = getParsedLines(content, bracketList);

        // 过滤与匿名缩进
        List<String> targetLines = filterAndHandleLamda(lines);

        dumpLines(targeFile, targetLines);
    }

    private static List<Map<Character, Integer>> getBracketIndex(String content) {

        List<Map<Character, Integer>> bracketList = new ArrayList<>();

        for (int i = 0; i < content.length(); i++) {

            if (content.charAt(i) == LEFT_BRACKET
                || content.charAt(i) == RIGHT_BRACKET) {

                Map<Character, Integer> bracketMap = new HashMap<>();
                bracketMap.put(content.charAt(i), i);
                bracketList.add(bracketMap);
            }
        }

        return bracketList;
    }

    private static List<String> filterAndHandleLamda(List<String> lines) {

        List<String> targetlines = new ArrayList<>();

        int preTabCount = Integer.MAX_VALUE;

        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i);
            int tabCount = line.indexOf("-");

            // lamda调用缩进处理
            if (line.contains("λ→")) {

                line = line.substring(4);

                for (int j = i + 1; j < lines.size(); j++) {
                    String subLine = lines.get(j);
                    int subTabCount = subLine.indexOf("-");
                    if (subTabCount > tabCount) {
                        subLine = subLine.substring(4);
                    } else {
                        break;
                    }
                    lines.set(j, subLine);
                }
            }

            // 处理过滤
            boolean filter = Boolean.FALSE;
            for (String fc : FILTER_STRS) {
    
                if (tabCount > preTabCount) {
                    filter = Boolean.TRUE;
                    break;
                }
    
                if (line.contains(fc)) {
                    filter = Boolean.TRUE;
                    preTabCount = tabCount;
                    break;
                }
            }
    
            if (!filter) {
                targetlines.add(line);
                preTabCount = Integer.MAX_VALUE;
            }
        }
        
        return targetlines;
    }
    
    private static List<String> getParsedLines(String content, List<Map<Character, Integer>> bracketList) {
        
        // 缩进
        String tab = "";
        int preIndex = 0;
        
        List<String> lines = new ArrayList<>();
    
        for (Map<Character, Integer> bracketMap : bracketList) {
    
            Map.Entry<Character, Integer> entry = bracketMap.entrySet().iterator().next();

            char bracket = entry.getKey();
            int index = entry.getValue();
            
            // 标准的非空json串至少有4个字符
            if (index - preIndex > 4) {
                
                String jsonStr = content.substring(preIndex + 1, index);
                JSONObject call = JSON.parseObject(jsonStr);

                String className = call.getJSONObject("_classDescription").getString("_className");

                String line = tab + "- " + className + "." + call.getString("_methodName") + "()";
                
                // 根目录不用缩进
                lines.add(line.substring(4));
            }
            
            // 缩进处理
            if (LEFT_BRACKET == bracket) {
                tab += "    ";
            } else {
                tab = tab.substring(4);
            }
            
            preIndex = index;
        }
        
        return lines;
    }
    
    private static void dumpLines(File targetFile, List<String> targetLines) throws IOException {

        if (targetLines == null || targetLines.size() == 0) {
            return;
        }

        for (String line : targetLines) {

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                // 截断包名，只放类+方法名
                if (CharUtils.isAsciiAlphaUpper(c)) {

                    line = line.substring(0, line.indexOf("-") + 2) + line.substring(i);
                    break;
                }
            }
            FileUtils.write(targetFile, line + "\n", Boolean.TRUE);
        }
    }
}
