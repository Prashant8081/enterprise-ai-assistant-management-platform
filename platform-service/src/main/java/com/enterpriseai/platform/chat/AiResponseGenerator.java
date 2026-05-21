package com.enterpriseai.platform.chat;

import java.util.List;

import com.enterpriseai.platform.assistant.AiAssistant;
import com.enterpriseai.platform.document.DocumentChunk;

public interface AiResponseGenerator {

    String generate(AiAssistant assistant, String userMessage, List<DocumentChunk> context);
}
