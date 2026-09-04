package fhnw.hoti.worldebackend.service;


import fhnw.hoti.worldebackend.dto.GuessRequest;
import fhnw.hoti.worldebackend.dto.GuessResponse;
import fhnw.hoti.worldebackend.model.Game;
import fhnw.hoti.worldebackend.repository.GameRepository;
import fhnw.hoti.worldebackend.dto.StatisticsResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.InputBuffer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    private List<String> words = new ArrayList<>();
    private final Random random = new Random();

    @PostConstruct
    public void loadWords() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource("words.txt").getInputStream()
                ))) {

            String line;
            while ((line = br.readLine()) != null)
                words.add(line.trim().toUpperCase());

        } catch (IOException e) {
            throw new RuntimeException("Could not load words", e);
        }
    }



    public Game startNewGame() {
        if (words.isEmpty()) {
            throw new RuntimeException("Word list is empty!");
        }

        String word = words.get(random.nextInt(words.size()));

        Game game = Game.builder()
                .solutionWord(word)
                .attempts(0)
                .completed(false)
                .hintsUsed(0)
                .build();
        return gameRepository.save(game);
    }

    public GuessResponse makeGuess(GuessRequest request) {
        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (game.isCompleted()) {
            throw new RuntimeException("Game already finished");
        }

        String guess = request.getGuess().toUpperCase();
        String solution = game.getSolutionWord();

        if (!words.contains(guess)) {
            throw new RuntimeException("Word not found");
        }

        game.setAttempts(game.getAttempts() + 1);

        String feedback = generateFeedback(guess, solution);

        boolean success = guess.equals(solution);
        boolean completed = success || game.getAttempts() >= 6;

        game.setCompleted(completed);
        gameRepository.save(game);

        return new GuessResponse(feedback, completed, success, solution);
    }

    public StatisticsResponse getStatistics() {

        List<Game> games = gameRepository.findAll();

        List<Game> completedGames = games.stream()
                .filter(Game::isCompleted)
                .toList();

        long gamesPlayed = completedGames.size();

        long totalAttempts = completedGames.stream()
                .mapToLong(Game::getAttempts)
                .sum();

        double averageAttempts = 0;

        if (gamesPlayed > 0) {
            averageAttempts = (double) totalAttempts / gamesPlayed;
        }

        return new StatisticsResponse(
                gamesPlayed,
                totalAttempts,
                averageAttempts
        );
    }

    private String generateFeedback(String guess, String solution) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            char g = guess.charAt(i);
            char s = solution.charAt(i);

            if (g == s) {
                result.append("G"); // Green
            } else if (solution.contains(String.valueOf(g))) {
                result.append("Y"); // Yellow
            } else {
                result.append("R"); // Red
            }
        }
        return result.toString();
    }

    public String getHint(Long gameId) {

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (game.getHintsUsed() == null) {
            game.setHintsUsed(0);
        }

        if (game.isCompleted()) {
            throw new RuntimeException("Game already finished");
        }

        if (game.getHintsUsed() >= 2) {
            throw new RuntimeException("No hints left");
        }

        String solution = game.getSolutionWord();

        char hint = solution.charAt(random.nextInt(solution.length()));

        game.setHintsUsed(game.getHintsUsed() + 1);
        gameRepository.save(game);

        return String.valueOf(hint);
    }
}
