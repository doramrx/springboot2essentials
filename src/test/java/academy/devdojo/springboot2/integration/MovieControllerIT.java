package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Movie;
import academy.devdojo.springboot2.repository.MovieRepository;
import academy.devdojo.springboot2.util.MovieCreator;
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
import org.springframework.http.HttpMethod;

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
}
