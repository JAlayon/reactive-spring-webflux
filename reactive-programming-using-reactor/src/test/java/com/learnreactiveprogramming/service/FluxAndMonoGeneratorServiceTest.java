package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {

    private FluxAndMonoGeneratorService service;

    public FluxAndMonoGeneratorServiceTest(){
        this.service = new FluxAndMonoGeneratorService();
    }

    @Test
    void namesFlux(){

        //when
        var namesFlux = service.namesFlux();

        //then
        StepVerifier.create(namesFlux)
                //.expectNext("Alex", "Jair", "Mariana", "Rodrigo")
                //.expectNextCount(4)
                .expectNext("Alex")
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void namesFlux_map() {

        int stringLength = 4;

        var namesFlux = service.namesFlux_map(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("7-MARIANA", "7-RODRIGO")
                .verifyComplete();
    }

    /*
    @Test
    void namesFlux_immutability() {
        var namesFlux = service.namesFlux_immutability();

        StepVerifier.create(namesFlux)
                .expectNext("Ales", "Jair", "Mariana", "Rodrigo")
                .verifyComplete();
    }
    */

    @Test
    void namesFlux_flatMap() {

        int stringLength = 4;

        var namesFlux = service.namesFlux_flatMap(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("M","A","R","I","A","N","A","R","O","D","R","I","G","O")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatMap_async(){
        int stringLength = 4;

        var namesFlux = service.namesFlux_flatMap_async(stringLength);

        StepVerifier.create(namesFlux)
                .expectNextCount(14)
                .verifyComplete();
    }

    /**
     * Use concatMap if ordering matters
     */
    @Test
    void namesFlux_concatMap(){
        int stringLength = 4;

        var namesFlux = service.namesFlux_concatMap(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("M","A","R","I","A","N","A","R","O","D","R","I","G","O")
                //.expectNextCount(14)
                .verifyComplete();
    }

    @Test
    void namesFlux_transform(){
        int stringLength = 4;

        var namesFlux = service.namesFlux_transform(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("MARIANA")
                .expectNext("RODRIGO")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_defaultIfEmpty(){
        int stringLength = 10;

        var namesFlux = service.namesFlux_transform(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_switchIfEmpty(){
        int stringLength = 10;

        var namesFlux = service.namesFlux_transform_switchIfEmpty(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesMono_flatMap(){
        int stringLength = 3;

        var value = service.namesMono_flatMap(stringLength);

        StepVerifier.create(value)
                .expectNext(List.of("A","L","E","X"))
                .verifyComplete();
    }

    @Test
    void namesMono_flatMapMany(){
        int stringLength = 3;

        var value = service.namesMono_flatMapMany(stringLength);

        StepVerifier.create(value)
                .expectNext("A","L","E","X")
                .verifyComplete();
    }

    @Test
    void explore_concat(){
        var concatFlux = service.explore_concat();

        StepVerifier.create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_concatWith(){
        var concatFlux = service.explore_concatWith();

        StepVerifier.create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_concatWith_mono(){
        var concatFlux = service.explore_concatWith_mono();

        StepVerifier.create(concatFlux)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void explore_merge(){
        var value = service.explore_merge();

        StepVerifier.create(value)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWith(){
        var value = service.explore_mergeWith();

        StepVerifier.create(value)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }
}