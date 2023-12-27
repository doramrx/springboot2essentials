package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Movie;

public class MovieCreator {

    public static Movie createMovieToBeSaved(){
        return Movie.builder().name("The Butterfly Effect").build();
    }

    public static Movie createValidMovie(){
        return Movie.builder().id(1L).name("The Butterfly Effect").build();
    }

    public static Movie createValidUpdatedMovie(){
        return Movie.builder().id(1L).name("The Butterfly Effect 2").build();
    }

}
