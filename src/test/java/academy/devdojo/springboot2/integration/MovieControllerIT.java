package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Movie;
import academy.devdojo.springboot2.dto.MovieDTO;
import academy.devdojo.springboot2.repository.MovieRepository;
import academy.devdojo.springboot2.util.MovieCreator;
import academy.devdojo.springboot2.util.MovieDTOCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    @Test
    @DisplayName("save returns movie when successful")
    void save_ReturnsMovie_WhenSuccessful(){
        MovieDTO movieDTO = MovieDTOCreator.createMovieDTO();

        ResponseEntity<Movie> movie = testRestTemplate.postForEntity(
                "/movies",
                movieDTO,
                Movie.class);

        Assertions.assertNotNull(movie);
        Assertions.assertNotNull(movie.getBody());
        Assertions.assertNotNull(movie.getBody().getId());
        Assertions.assertEquals(movie.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    @DisplayName("delete removes movie when successful")
    void delete_RemovesMovie_WhenSuccessful(){
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());

        ResponseEntity<Void> movie = testRestTemplate.exchange(
                "/movies/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedMovie.getId());

        Assertions.assertNotNull(movie);
        Assertions.assertEquals(movie.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace updates movie when successful")
    void replace_UpdatesMovie_WhenSuccessful(){
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());

        savedMovie.setName("new name");

        ResponseEntity<Void> movie = testRestTemplate.exchange(
                "/movies",
                HttpMethod.PUT,
                new HttpEntity<>(savedMovie),
                Void.class);

        Assertions.assertNotNull(movie);
        Assertions.assertEquals(movie.getStatusCode(), HttpStatus.NO_CONTENT);
    }
}
