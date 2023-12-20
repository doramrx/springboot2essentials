package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Movie;
import academy.devdojo.springboot2.dto.MovieDTO;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    MovieRepository repository;

    public Page<Movie> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public List<Movie> findAllNonPageable() {
        return repository.findAll();
    }

    public List<Movie> findByName(String name){
        return repository.findByName(name);
    }

    public Movie findByIdOrThrowBadRequestException(long id){
        return repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Movie not found"));
    }

    @Transactional
    public Movie save(MovieDTO movieDto) {
        return repository.save(Movie.builder().name(movieDto.getName()).build());
    }

    public void delete(long id) {
        repository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(MovieDTO movieDto) {
        Movie savedMovie= findByIdOrThrowBadRequestException(movieDto.getId());
        Movie movie = Movie.builder()
                .id(savedMovie.getId())
                .name(movieDto.getName())
                .build();

        repository.save(movie);
    }
}
