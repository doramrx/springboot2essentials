package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Movie;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {

        // REST TEMPLATE - GET
        /*
        ResponseEntity<Movie> entity = new RestTemplate()
                .getForEntity("http://localhost:8080/movies/{id}", Movie.class, 8);
        log.info(entity);

        Movie object = new RestTemplate()
                .getForObject("http://localhost:8080/movies/{id}", Movie.class, 12);
        log.info(object);

        Movie[] movies = new RestTemplate()
                .getForObject("http://localhost:8080/movies/all", Movie[].class);
        log.info(Arrays.toString(movies));

        ResponseEntity<List<Movie>> exchange = new RestTemplate().exchange(
                "http://localhost:8080/movies/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        log.info(exchange.getBody());
        */


        // REST TEMPLATE - POST

        /*
        Movie dune = Movie.builder().name("Dune").build();
        Movie duneSaved = new RestTemplate().postForObject("http://localhost:8080/movies", dune, Movie.class);
        log.info("Saved movie {}", duneSaved);
        */
        Movie lucy = Movie.builder().name("Lucy").build();
        ResponseEntity<Movie> lucySaved = new RestTemplate().exchange(
                "http://localhost:8080/movies",
                HttpMethod.POST,
                new HttpEntity<>(lucy, createJsomHeader()),
                Movie.class);
        log.info("Saved movie {}", lucySaved);


        // REST TEMPLATE - PUT


        Movie movieToBeUpdated = lucySaved.getBody();
        movieToBeUpdated.setName("Lucy 2");

        ResponseEntity<Void> lucyUpdated = new RestTemplate().exchange(
                "http://localhost:8080/movies",
                HttpMethod.PUT,
                new HttpEntity<>(movieToBeUpdated, createJsomHeader()),
                Void.class);
        log.info(lucyUpdated);


        // REST TEMPLATE - DELETE

        ResponseEntity<Void> lucyDeleted = new RestTemplate().exchange(
                "http://localhost:8080/movies/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                48);
        log.info(lucyUpdated);

    }

    private static HttpHeaders createJsomHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
