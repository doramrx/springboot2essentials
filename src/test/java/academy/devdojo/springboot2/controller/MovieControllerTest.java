package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Movie;
import academy.devdojo.springboot2.dto.MovieDTO;
import academy.devdojo.springboot2.service.MovieService;
import academy.devdojo.springboot2.util.MovieCreator;
import academy.devdojo.springboot2.util.MovieDTOCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class MovieControllerTest {

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieServiceMock;

    @BeforeEach
    void setUp(){
        PageImpl<Movie> moviePage = new PageImpl<>(List.of(MovieCreator.createValidMovie()));
        BDDMockito.when(movieServiceMock.findAll(ArgumentMatchers.any()))
                .thenReturn(moviePage);

        BDDMockito.when(movieServiceMock.findAllNonPageable())
                .thenReturn(List.of(MovieCreator.createValidMovie()));

        BDDMockito.when(movieServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(MovieCreator.createValidMovie());

        BDDMockito.when(movieServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(MovieCreator.createValidMovie()));

        BDDMockito.when(movieServiceMock.save(ArgumentMatchers.any(MovieDTO.class)))
                .thenReturn(MovieCreator.createValidMovie());

        BDDMockito.doNothing().when(movieServiceMock).replace(ArgumentMatchers.any(MovieDTO.class));
    }

    @Test
    @DisplayName("list returns list of movies inside page object when successful")
    void list_ReturnsListOfMoviesInsidePageObject_WhenSuccessful(){
        String expectedName = MovieCreator.createValidMovie().getName();

        Page<Movie> moviePage = movieController.list(null).getBody();

        Assertions.assertNotNull(moviePage);
        Assertions.assertFalse(moviePage.toList().isEmpty());
        Assertions.assertEquals(moviePage.toList().size(), 1);
        Assertions.assertEquals(moviePage.toList().get(0).getName(), expectedName);
    }

    @Test
    @DisplayName("listAll returns list of movies when successful")
    void listAll_ReturnsListOfMovies_WhenSuccessful(){
        String expectedName = MovieCreator.createValidMovie().getName();

        List<Movie> movies = movieController.listAll().getBody();

        Assertions.assertNotNull(movies);
        Assertions.assertFalse(movies.isEmpty());
        Assertions.assertEquals(movies.size(), 1);
        Assertions.assertEquals(movies.get(0).getName(), expectedName);
    }

    @Test
    @DisplayName("findById returns movie when successful")
    void findById_ReturnsMovie_WhenSuccessful(){
        Long expectedId = MovieCreator.createValidMovie().getId();

        Movie movie = movieController.findById(1).getBody();

        Assertions.assertNotNull(movie);
        Assertions.assertEquals(movie.getId(), expectedId);
    }

    @Test
    @DisplayName("findByName returns a list of movies when successful")
    void findByName_ReturnsListOfMovies_WhenSuccessful(){
        String expectedName = MovieCreator.createValidMovie().getName();

        List<Movie> movies = movieController.findByName("movie").getBody();

        Assertions.assertNotNull(movies);
        Assertions.assertFalse(movies.isEmpty());
        Assertions.assertEquals(movies.size(), 1);
        Assertions.assertEquals(movies.get(0).getName(), expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of movies when movie is not found")
    void findByName_ReturnsAnEmptyListOfMovies_WhenMovieIsNotFound(){
        BDDMockito.when(movieServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Movie> movies = movieController.findByName("movie").getBody();

        Assertions.assertNotNull(movies);
        Assertions.assertTrue(movies.isEmpty());
    }

    @Test
    @DisplayName("save returns movie when successful")
    void save_ReturnsMovie_WhenSuccessful(){
        Movie movie = movieController.save(MovieDTOCreator.createMovieDTO()).getBody();

        Assertions.assertNotNull(movie);
        Assertions.assertEquals(movie, MovieCreator.createValidMovie());
    }

    @Test
    @DisplayName("replace updates movie when successful")
    void replace_UpdatesMovie_WhenSuccessful(){
        Assertions.assertDoesNotThrow(() -> movieController.replace(MovieDTOCreator.createValidUpdatedMovieDTO()).getBody());
        ResponseEntity<Void> entity = movieController.replace(MovieDTOCreator.createValidUpdatedMovieDTO());
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, entity.getStatusCode());
    }
}