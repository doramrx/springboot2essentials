package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Movie;
import academy.devdojo.springboot2.util.MovieCreator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Movie Repository")
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("Save persists movie when successful")
    void save_PersistsMovie_WhenSuccessful(){
        Movie movieToBeSaved = MovieCreator.createMovieToBeSaved();
        Movie savedMovie = this.movieRepository.save(movieToBeSaved);

        Assertions.assertNotNull(savedMovie);
        Assertions.assertNotNull(savedMovie.getId());
        Assertions.assertEquals(movieToBeSaved.getName(), savedMovie.getName());
    }

    @Test
    @DisplayName("Save updates movie when successful")
    void save_UpdatesMovie_WhenSuccessful(){
        Movie movieToBeSaved = MovieCreator.createMovieToBeSaved();
        Movie savedMovie = this.movieRepository.save(movieToBeSaved);

        savedMovie.setName("Sound of Freedom");
        Movie updatedMovie = this.movieRepository.save(savedMovie);

        Assertions.assertNotNull(updatedMovie);
        Assertions.assertNotNull(updatedMovie.getId());
        Assertions.assertEquals(savedMovie.getName(), updatedMovie.getName());
    }

    @Test
    @DisplayName("Delete removes movie when successful")
    void delete_RemovesMovie_WhenSuccessful(){
        Movie movieToBeSaved = MovieCreator.createMovieToBeSaved();
        Movie savedMovie = this.movieRepository.save(movieToBeSaved);
        this.movieRepository.delete(savedMovie);

        Optional<Movie> optionalMovie = this.movieRepository.findById(savedMovie.getId());
        Assertions.assertTrue(optionalMovie.isEmpty());
    }

    @Test
    @DisplayName("Find By Name returns list of movies when successful")
    void findByName_ReturnsListOfMovie_WhenSuccessful(){
        Movie movieToBeSaved = MovieCreator.createMovieToBeSaved();
        Movie savedMovie = this.movieRepository.save(movieToBeSaved);

        String name = savedMovie.getName();
        List<Movie> movies = this.movieRepository.findByName(name);

        Assertions.assertFalse(movies.isEmpty());
        Assertions.assertTrue(movies.contains(savedMovie));
    }

    @Test
    @DisplayName("Find By Name returns empty list when no movie is found")
    void findByName_ReturnsEmptyList_WhenMovieIsNotFound(){
        List<Movie> movies = this.movieRepository.findByName("Abcdefg");
        Assertions.assertTrue(movies.isEmpty());
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty(){
        Movie movie = new Movie();
        ConstraintViolationException exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            this.movieRepository.save(movie);
        });

        Assertions.assertTrue(exception.getMessage().contains("The movie name cannot be empty"));
    }
}