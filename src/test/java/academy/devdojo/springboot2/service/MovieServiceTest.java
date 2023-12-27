package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Movie;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.repository.MovieRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepositoryMock;

    @BeforeEach
    void setUp(){
        PageImpl<Movie> moviePage = new PageImpl<>(List.of(MovieCreator.createValidMovie()));
        BDDMockito.when(movieRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(moviePage);

        BDDMockito.when(movieRepositoryMock.findAll())
                .thenReturn(List.of(MovieCreator.createValidMovie()));

        BDDMockito.when(movieRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(MovieCreator.createValidMovie()));

        BDDMockito.when(movieRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(MovieCreator.createValidMovie()));

        BDDMockito.when(movieRepositoryMock.save(ArgumentMatchers.any(Movie.class)))
                .thenReturn(MovieCreator.createValidMovie());

        BDDMockito.doNothing().when(movieRepositoryMock).delete(ArgumentMatchers.any(Movie.class));
    }

    @Test
    @DisplayName("findAll returns list of movies inside page object when successful")
    void findAll_ReturnsListOfMoviesInsidePageObject_WhenSuccessful(){
        String expectedName = MovieCreator.createValidMovie().getName();

        Page<Movie> moviePage = movieService.findAll(PageRequest.of(1,1));

        Assertions.assertNotNull(moviePage);
        Assertions.assertFalse(moviePage.toList().isEmpty());
        Assertions.assertEquals(moviePage.toList().size(), 1);
        Assertions.assertEquals(moviePage.toList().get(0).getName(), expectedName);
    }

    @Test
    @DisplayName("findAllNonPageable returns list of movies when successful")
    void findAllNonPageable_ReturnsListOfMovies_WhenSuccessful(){
        String expectedName = MovieCreator.createValidMovie().getName();

        List<Movie> movies = movieService.findAllNonPageable();

        Assertions.assertNotNull(movies);
        Assertions.assertFalse(movies.isEmpty());
        Assertions.assertEquals(movies.size(), 1);
        Assertions.assertEquals(movies.get(0).getName(), expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns movie when successful")
    void findByIdOrThrowBadRequestException_ReturnsMovie_WhenSuccessful(){
        Long expectedId = MovieCreator.createValidMovie().getId();

        Movie movie = movieService.findByIdOrThrowBadRequestException(1);

        Assertions.assertNotNull(movie);
        Assertions.assertEquals(movie.getId(), expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when movie is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenMovieIsNotFound(){
        BDDMockito.when(movieRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(BadRequestException.class, () -> this.movieService.findByIdOrThrowBadRequestException(1));
    }

    @Test
    @DisplayName("findByName returns a list of movies when successful")
    void findByName_ReturnsListOfMovies_WhenSuccessful(){
        String expectedName = MovieCreator.createValidMovie().getName();

        List<Movie> movies = movieService.findByName("movie");

        Assertions.assertNotNull(movies);
        Assertions.assertFalse(movies.isEmpty());
        Assertions.assertEquals(movies.size(), 1);
        Assertions.assertEquals(movies.get(0).getName(), expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of movies when movie is not found")
    void findByName_ReturnsAnEmptyListOfMovies_WhenMovieIsNotFound(){
        BDDMockito.when(movieRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Movie> movies = movieService.findByName("movie");

        Assertions.assertNotNull(movies);
        Assertions.assertTrue(movies.isEmpty());
    }

    @Test
    @DisplayName("save returns movie when successful")
    void save_ReturnsMovie_WhenSuccessful(){
        Movie movie = movieService.save(MovieDTOCreator.createMovieDTO());

        Assertions.assertNotNull(movie);
        Assertions.assertEquals(movie, MovieCreator.createValidMovie());
    }

    @Test
    @DisplayName("delete removes movie when successful")
    void delete_RemovesMovie_WhenSuccessful(){
        Assertions.assertDoesNotThrow(() -> movieService.delete(1));
    }

    @Test
    @DisplayName("replace updates movie when successful")
    void replace_UpdatesMovie_WhenSuccessful(){
        Assertions.assertDoesNotThrow(() -> movieService.replace(MovieDTOCreator.createValidUpdatedMovieDTO()));
    }
}