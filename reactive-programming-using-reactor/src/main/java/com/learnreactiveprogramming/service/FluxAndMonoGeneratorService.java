package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux(){
        return Flux.fromIterable(List.of("Alex", "Jair", "Mariana", "Rodrigo"))
                .log(); //from db or remote service calls
    }

    public Flux<String> namesFlux_map(int stringLength){
        //filter the string whose length is greater than stringLength
        return Flux.fromIterable(List.of("Alex", "Jair", "Mariana", "Rodrigo"))
                .map(String::toUpperCase)
                .filter(s-> s.length() > stringLength)
                .map(s-> s.length()+"-"+s)
                .log();
    }

    public Flux<String> namesFlux_immutability(){
        var namesFlux = Flux.fromIterable(List.of("Alex", "Jair", "Mariana", "Rodrigo"));
        namesFlux.map(String::toUpperCase);
        return namesFlux;
    }

    public Flux<String> namesFlux_flatMap(int stringLength){
        //filter the string whose length is greater than stringLength
        return Flux.fromIterable(List.of("Alex", "Jair", "Mariana", "Rodrigo"))
                .map(String::toUpperCase)
                .filter(s-> s.length() > stringLength)
                .flatMap(s-> Flux.fromArray(s.split("")))
                .log();
    }

    public Flux<String> namesFlux_flatMap_async(int stringLength){
        var delay = new Random().nextInt(1000);
        return Flux.fromIterable(List.of("Alex", "Jair", "Mariana", "Rodrigo"))
                .map(String::toUpperCase)
                .filter(s-> s.length() > stringLength)
                .flatMap(s-> Flux.fromArray(s.split(""))
                        .delayElements(Duration.ofMillis(delay)))
                .log();
    }
    public Flux<String> namesFlux_concatMap(int stringLength){
        var delay = new Random().nextInt(1000);
        return Flux.fromIterable(List.of("Alex", "Jair", "Mariana", "Rodrigo"))
                .map(String::toUpperCase)
                .filter(s-> s.length() > stringLength)
                .concatMap(s-> Flux.fromArray(s.split(""))
                        .delayElements(Duration.ofMillis(delay)))
                .log();
    }

    public Flux<String> namesFlux_transform(int stringLength){
        Function<Flux<String>, Flux<String>> filterMap = name -> (
                    name.map(String::toUpperCase)
                        .filter(s-> s.length() > stringLength)
        );
        return Flux.fromIterable(List.of("Alex", "Jair", "Mariana", "Rodrigo"))
                .transform(filterMap)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_transform_switchIfEmpty(int stringLength){
        Function<Flux<String>, Flux<String>> filterMap = name -> (
                name.map(String::toUpperCase)
                        .filter(s-> s.length() > stringLength)
        );

        var defaultFlux = Flux.just("default");

        return Flux.fromIterable(List.of("Alex", "Jair", "Mariana", "Rodrigo"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> explore_concat(){
        var stream1 = Flux.just("A","B","C");
        var stream2 = Flux.just("D","E","F");

        return Flux.concat(stream1, stream2)
                .log();
    }

    public Flux<String> explore_concatWith(){
        var stream1 = Flux.just("A","B","C");
        var stream2 = Flux.just("D","E","F");

        return stream1.concatWith(stream2).log();
    }

    public Flux<String> explore_concatWith_mono(){
        var stream1Mono = Mono.just("A");
        var stream2Mono = Mono.just("B");

        return stream1Mono.concatWith(stream2Mono).log(); //Flux of A B
    }

    public Flux<String> explore_merge(){
        var stream1 = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));

        var stream2 = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(120));

        return Flux.merge(stream1, stream2).log();
    }

    public Flux<String> explore_mergeWith(){
        var stream1 = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));

        var stream2 = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(120));

        return stream1.mergeWith(stream2).log();
    }


    public Mono<String> namesMono(){
        return Mono.just("Jair")
                .log();
    }

    public Mono<String> namesMono_map_filter(int stringLength){
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s-> s.length() > stringLength);
    }

    public Mono<List<String>> namesMono_flatMap(int stringLength){
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s-> s.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();//Mono<List of A L E X>
    }

    public Flux<String> namesMono_flatMapMany(int stringLength){
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s-> s.length() > stringLength)
                .flatMapMany(s -> Flux.fromArray(s.split("")))
                .log();//Mono<List of A L E X>
    }


    private Mono<List<String>> splitStringMono(String s) {
        var charArray = s.split("");
        var charList = List.of(charArray);
        return Mono.just(charList);
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();
        service.namesFlux()
                .subscribe(System.out::println);

        service.namesMono()
                .subscribe(name -> System.out.println("Mono name is: " + name));
    }
}
