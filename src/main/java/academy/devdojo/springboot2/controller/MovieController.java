package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Movie;
import academy.devdojo.springboot2.dto.MovieDTO;
import academy.devdojo.springboot2.service.MovieService;
import academy.devdojo.springboot2.util.DateUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("movies")
@Log4j2
public class MovieController {

    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private MovieService service;

    @GetMapping
    public ResponseEntity<Page<Movie>> list(Pageable pageable){
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Movie>> listAll(){
        return ResponseEntity.ok().body(service.findAllNonPageable());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Movie> findById(@PathVariable long id){
        return ResponseEntity.ok().body(service.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Movie>> findByName(@RequestParam(required = false) String name){
        return ResponseEntity.ok().body(service.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Movie> save(@RequestBody @Valid MovieDTO movieDto) {
        return new ResponseEntity<>(service.save(movieDto), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody MovieDTO movieDto){
        service.replace(movieDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
