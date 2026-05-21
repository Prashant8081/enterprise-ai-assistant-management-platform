package com.enterpriseai.platform.document;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import org.springframework.stereotype.Service;

@Service
class HashEmbeddingService implements EmbeddingService {

    private static final int DIMENSIONS = 64;

    @Override
    public String embed(String text) {
        double[] vector = new double[DIMENSIONS];
        String normalized = text.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9 ]", " ");
        for (String token : normalized.split("\\s+")) {
            if (token.length() < 2) {
                continue;
            }
            byte[] digest = sha256(token);
            int index = Byte.toUnsignedInt(digest[0]) % DIMENSIONS;
            double direction = (digest[1] & 1) == 0 ? 1.0 : -1.0;
            vector[index] += direction;
        }

        double magnitude = 0.0;
        for (double value : vector) {
            magnitude += value * value;
        }
        magnitude = Math.sqrt(magnitude);
        if (magnitude == 0.0) {
            vector[0] = 1.0;
            magnitude = 1.0;
        }

        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(String.format(Locale.ROOT, "%.6f", vector[i] / magnitude));
        }
        return builder.append(']').toString();
    }

    private byte[] sha256(String token) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 not available", ex);
        }
    }
}
