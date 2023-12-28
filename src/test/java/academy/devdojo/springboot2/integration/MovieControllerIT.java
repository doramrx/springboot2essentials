package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Movie;
import academy.devdojo.springboot2.repository.MovieRepository;
import academy.devdojo.springboot2.util.MovieCreator;
import academy.devdojo.springboot2.util.MovieDTOCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class MovieControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("list returns list of movies inside page object when successful")
    void list_ReturnsListOfMoviesInsidePageObject_WhenSuccessful(){
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());
        String expectedName = savedMovie.getName();

        PageableResponse<Movie> moviePage = testRestTemplate.exchange(
                "/movies",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Movie>>() {
        }).getBody();

        Assertions.assertNotNull(moviePage);
        Assertions.assertFalse(moviePage.toList().isEmpty());
        Assertions.assertEquals(moviePage.toList().size(), 1);
        Assertions.assertEquals(moviePage.toList().get(0).getName(), expectedName);
    }

    @Test
    @DisplayName("listAll returns list of movies when successful")
    void listAll_ReturnsListOfMovies_WhenSuccessful(){
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());
        String expectedName = savedMovie.getName();

        List<Movie> movies = testRestTemplate.exchange(
                "/movies/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }).getBody();

        Assertions.assertNotNull(movies);
        Assertions.assertFalse(movies.isEmpty());
        Assertions.assertEquals(movies.size(), 1);
        Assertions.assertEquals(movies.get(0).getName(), expectedName);
    }

    @Test
    @DisplayName("findById returns movie when successful")
    void findById_ReturnsMovie_WhenSuccessful(){
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());
        Long expectedId = savedMovie.getId();

        Movie movie = testRestTemplate.getForObject("/movies/{id}", Movie.class, expectedId);

        Assertions.assertNotNull(movie);
        Assertions.assertEquals(movie.getId(), expectedId);
    }

    @Test
    @DisplayName("findByName returns a list of movies when successful")
    void findByName_ReturnsListOfMovies_WhenSuccessful(){
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());
        String expectedName = savedMovie.getName();
        String url = String.format("/movies/find?name=%s", expectedName);

        List<Movie> movies = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }).getBody();

        Assertions.assertNotNull(movies);
        Assertions.assertFalse(movies.isEmpty());
        Assertions.assertEquals(movies.size(), 1);
        Assertions.assertEquals(movies.get(0).getName(), expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of movies when movie is not found")
    void findByName_ReturnsAnEmptyListOfMovies_WhenMovieIsNotFound(){
        List<Movie> movies = testRestTemplate.exchange(
                "/movies/find?name=test",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }).getBody();

        Assertions.assertNotNull(movies);
        Assertions.assertTrue(movies.isEmpty());
    }
}
