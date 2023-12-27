package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.dto.MovieDTO;

public class MovieDTOCreator {

    public static MovieDTO createMovieDTO(){
        return MovieDTO.builder()
                .name(MovieCreator.createMovieToBeSaved().getName())
                .build();
    }

    public static MovieDTO createValidUpdatedMovieDTO(){
        return MovieDTO.builder()
                .id(MovieCreator.createValidUpdatedMovie().getId())
                .name(MovieCreator.createValidUpdatedMovie().getName())
                .build();
    }

}
