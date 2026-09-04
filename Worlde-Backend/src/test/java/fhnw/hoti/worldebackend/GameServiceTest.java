package fhnw.hoti.worldebackend;

import fhnw.hoti.worldebackend.model.Game;
import fhnw.hoti.worldebackend.repository.GameRepository;
import fhnw.hoti.worldebackend.service.GameService;
import fhnw.hoti.worldebackend.dto.GuessRequest;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private final GameRepository gameRepository = mock(GameRepository.class);
    private final GameService gameService = new GameService(gameRepository);

    @BeforeEach
    void setup() {
        gameService.loadWords();
    }

    @Test
    void shouldCreateGame() {
        Game game = Game.builder()
                .solutionWord("APPLE")
                .attempts(0)
                .completed(false)
                .build();

        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Game result = gameRepository.save(game);

        assertNotNull(result);
        assertEquals(0, result.getAttempts());
        assertFalse(result.isCompleted());
    }

    @Test
    void sixthWrongGuessShouldFinishGame() {

        Game game = Game.builder()
                .solutionWord("APPLE")
                .attempts(5)
                .completed(false)
                .build();

        when(gameRepository.findById(1L))
                .thenReturn(java.util.Optional.of(game));

        GuessRequest request = new GuessRequest();
        request.setGameId(1L);
        request.setGuess("HOUSE");

        var response = gameService.makeGuess(request);

        assertFalse(response.isSuccess());
        assertTrue(response.isCompleted());
        assertEquals(6, game.getAttempts());
    }

    @Test
    void invalidWordShouldBeRejected() {

        Game game = Game.builder()
                .solutionWord("APPLE")
                .attempts(0)
                .completed(false)
                .build();

        when(gameRepository.findById(1L))
                .thenReturn(java.util.Optional.of(game));

        GuessRequest request = new GuessRequest();
        request.setGameId(1L);
        request.setGuess("XXXXX");

        assertThrows(
                RuntimeException.class,
                () -> gameService.makeGuess(request)
        );
    }

}