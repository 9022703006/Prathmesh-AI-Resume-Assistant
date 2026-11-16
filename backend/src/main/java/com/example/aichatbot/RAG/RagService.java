package com.example.aichatbot.RAG;


import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final List<String> knowledgeChunks;

    public RagService() {
        try (InputStream inputStream =
                     getClass().getClassLoader().getResourceAsStream("knowledge.txt")) {

            if (inputStream == null) {
                throw new RuntimeException("knowledge.txt not found");
            }

            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            knowledgeChunks = Arrays.stream(content.split("\\n\\s*\\n"))
                    .map(String::trim)
                    .filter(chunk -> !chunk.isEmpty())
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load knowledge.txt", e);
        }
    }

    public String retrieve(String question) {

        String[] questionWords = question
                .toLowerCase()
                .split("\\W+");

        List<String> matches = knowledgeChunks.stream()
                .map(chunk -> new Match(chunk, calculateScore(chunk, questionWords)))
                .filter(match -> match.score > 0)
                .sorted(Comparator.comparingInt(Match::score).reversed())
                .limit(3)
                .map(Match::text)
                .collect(Collectors.toList());

        if (matches.isEmpty()) {
            return "";
        }

        return String.join("\n\n", matches);
    }

    private int calculateScore(String chunk, String[] questionWords) {

        String lowerChunk = chunk.toLowerCase();
        int score = 0;

        for (String word : questionWords) {

            if (word.isBlank()) {
                continue;
            }

            if (lowerChunk.contains(word)) {
                score++;
            }
        }

        return score;
    }

    private record Match(String text, int score) {
    }
}