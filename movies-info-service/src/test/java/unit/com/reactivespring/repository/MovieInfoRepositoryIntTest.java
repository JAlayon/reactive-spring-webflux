package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryIntTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setup(){

        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieinfos).blockLast();
    }

    @AfterEach
    void tearDown(){
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findAll(){

        var moviesInfoFlux = movieInfoRepository.findAll();

        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById(){

        var movieInfoMono = movieInfoRepository.findById("abc");

        StepVerifier.create(movieInfoMono)
                .assertNext(movie -> {
                    assertEquals("Dark Knight Rises", movie.getName());
                })
                .verifyComplete();
    }

    @Test
    void saveMovieInfo(){

        var movieInfo = movieInfoRepository.save(
                new MovieInfo(null, "Batman Begins 1",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")));

        StepVerifier.create(movieInfo)
                .assertNext(movie -> {
                    assertNotNull(movie.getMovieInfoId());
                    assertEquals("Batman Begins 1", movie.getName());
                })
                .verifyComplete();
    }

    @Test
    void udpateMovieInfo(){
        //given
        var movieToUpdate = movieInfoRepository.findById("abc").block();
        movieToUpdate.setName("Batman Returns");
        movieToUpdate.setRelease_date(LocalDate.parse("2015-10-20"));

        //when
        var movieUpdated = movieInfoRepository.save(movieToUpdate);

        StepVerifier.create(movieUpdated)
                .assertNext(movie -> {
                    assertEquals("Batman Returns", movie.getName());
                    assertEquals(LocalDate.parse("2015-10-20"), movie.getRelease_date());
                })
                .verifyComplete();
    }



}