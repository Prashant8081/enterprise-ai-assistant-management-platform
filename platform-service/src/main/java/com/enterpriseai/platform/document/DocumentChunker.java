package com.enterpriseai.platform.document;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
class DocumentChunker {

    List<String> chunk(String content) {
        String cleaned = content.replaceAll("\\s+", " ").trim();
        List<String> chunks = new ArrayList<>();
        int targetSize = 900;
        int overlap = 120;
        int start = 0;

        while (start < cleaned.length()) {
            int end = Math.min(start + targetSize, cleaned.length());
            if (end < cleaned.length()) {
                int sentenceBoundary = cleaned.lastIndexOf('.', end);
                if (sentenceBoundary > start + 250) {
                    end = sentenceBoundary + 1;
                }
            }
            chunks.add(cleaned.substring(start, end).trim());
            if (end == cleaned.length()) {
                break;
            }
            start = Math.max(0, end - overlap);
        }
        return chunks;
    }

    int estimateTokens(String text) {
        return Math.max(1, text.length() / 4);
    }
}
