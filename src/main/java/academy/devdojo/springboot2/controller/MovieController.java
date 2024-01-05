package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Movie;
import academy.devdojo.springboot2.dto.MovieDTO;
import academy.devdojo.springboot2.service.MovieService;
import academy.devdojo.springboot2.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "List all movies paginated", description = "The default size is 20, use the parameter size to change the default value",
    tags = {"movie"})
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    public ResponseEntity<Page<Movie>> list(Pageable pageable){
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @GetMapping(path = "/all")
    @Operation(summary = "List all movies", description = "List all movies from Database", tags = {"movie"})
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    public ResponseEntity<List<Movie>> listAll(){
        return ResponseEntity.ok().body(service.findAllNonPageable());
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Find a movie by Id", description = "Find a movie by a given Id", tags = {"movie"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Movie Does Not Exist In The Database"),
    })
    public ResponseEntity<Movie> findById(@PathVariable long id){
        return ResponseEntity.ok().body(service.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    @Operation(summary = "Find a movie by name", description = "Find a movie by a given name", tags = {"movie"})
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    public ResponseEntity<List<Movie>> findByName(@RequestParam(required = false) String name){
        return ResponseEntity.ok().body(service.findByName(name));
    }

    @PostMapping
    @Operation(summary = "Create a new movie", description = "Create a new movie by a given name", tags = {"movie"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Movie Name Is Empty"),
    })
    public ResponseEntity<Movie> save(@RequestBody @Valid MovieDTO movieDto) {
        return new ResponseEntity<>(service.save(movieDto), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete a movie", description = "Delete a movie by a given id", tags = {"movie"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Movie Does Not Exist In The Database"),
    })
    public ResponseEntity<Void> delete(@PathVariable long id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    @Operation(summary = "Update a movie", description = "Update a movie by a given id", tags = {"movie"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Movie Does Not Exist In The Database"),
    })
    public ResponseEntity<Void> replace(@RequestBody MovieDTO movieDto){
        service.replace(movieDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
