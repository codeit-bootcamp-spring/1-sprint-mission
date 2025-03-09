package com.sprint.mission.config;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class P6spyPrettySqlFormatter implements MessageFormattingStrategy {
    // ANSI 색상 코드
    private static final String RESET = "\u001B[0m";  // 기본 색상
    private static final String YELLOW = "\u001B[33m"; // SQL 키워드
    private static final String WHITE = "\u001B[37m";  // 일반 텍스트
    private static final String CYAN = "\u001B[36m";   // 구분선

    // SQL 키워드 목록 (대문자로 변환하고 색상을 적용할 단어들)
    private static final String SQL_KEYWORDS = "(?i)\\b(SELECT|FROM|WHERE|JOIN|INNER|LEFT|RIGHT|OUTER|ON|GROUP BY|ORDER BY|HAVING|AS|AND|OR|INSERT INTO|VALUES|UPDATE|SET|DELETE|LIMIT|OFFSET|DISTINCT)\\b";

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        return String.format(
                "%s | %s[SQL 실행]%s\n%s\n\n%s|======================================================================|%s\n%s[실행 시간]: %d ms%s\n",
                currentTime, YELLOW, RESET, sql, CYAN, RESET, YELLOW, elapsed, RESET
        );
    }

    private String formatSql(String category, String sql) {
        if (sql == null || sql.isBlank()) {
            return sql;
        }


        if (Category.STATEMENT.getName().equals(category)) {
            sql = FormatStyle.BASIC.getFormatter().format(sql);
        }


        Map<String, String> aliasMap = new HashMap<>();
        AtomicInteger aliasCounter = new AtomicInteger(1);


        Pattern pattern = Pattern.compile("\\b(\\w+?)(_\\d+)\\b");
        Matcher matcher = pattern.matcher(sql);

        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String originalAlias = matcher.group(); // ex: u1_0
            String baseName = matcher.group(1); // ex: u

            aliasMap.putIfAbsent(originalAlias, aliasMap.containsKey(baseName) ? baseName + aliasCounter.getAndIncrement() : baseName);
            matcher.appendReplacement(sb, aliasMap.get(originalAlias));
        }
        matcher.appendTail(sb);

        sql = sb.toString();


        Pattern keywordPattern = Pattern.compile(SQL_KEYWORDS);
        Matcher keywordMatcher = keywordPattern.matcher(sql);
        StringBuffer formattedSql = new StringBuffer();

        while (keywordMatcher.find()) {
            String keyword = keywordMatcher.group().toUpperCase();
            keywordMatcher.appendReplacement(formattedSql, YELLOW + keyword + RESET);
        }
        keywordMatcher.appendTail(formattedSql);

        return WHITE + formattedSql.toString() + RESET;
    }
}