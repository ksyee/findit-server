package com.findit.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class CharsetDetector {

    private static final List<Charset> COMMON_CHARSETS = Arrays.asList(
            StandardCharsets.UTF_8,
            StandardCharsets.ISO_8859_1,
            StandardCharsets.US_ASCII,
            StandardCharsets.UTF_16,
            StandardCharsets.UTF_16BE,
            StandardCharsets.UTF_16LE,
            Charset.forName("EUC-KR"),
            Charset.forName("CP949")
    );
    
    public void detectCharset(String content) {
        byte[] bytes = content.getBytes();
        log.info("Content 길이: {} 바이트", bytes.length);
        
        for (Charset charset : COMMON_CHARSETS) {
            String decoded = new String(bytes, charset);
            log.info("{} 디코딩 결과 (처음 100자): {}", charset.name(), 
                decoded.substring(0, Math.min(100, decoded.length())));
        }
    }
}