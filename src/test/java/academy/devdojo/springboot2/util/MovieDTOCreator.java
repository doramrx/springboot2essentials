package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.dto.MovieDTO;

public class MovieDTOCreator {

    public static MovieDTO createMovieDTO(){
        return MovieDTO.builder()
                .name(MovieCreator.createMovieToBeSaved().getName())
                .build();
    }

}
