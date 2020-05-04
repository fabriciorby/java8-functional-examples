package example;

import example.pojo.Usuario;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.*;

import static example.Helper.getListaDeUsuarios;
import static java.util.stream.Collectors.*;

public class StreamTest {
    @Test
    public void streamTest() {
        //O que é uma Stream?
        Stream<Integer> simpleStream = Stream.of(1);
        System.out.println(simpleStream.count());

        //A Stream só pode ser utilizada uma vez.
        try {
            System.out.println(simpleStream.count());
        } catch (IllegalStateException e) {
            System.out.println(e);
        }

        //Curiosidade: Caso queira usar mais de uma vez, é possível 'guardá-la' num Supplier
        //Porém, de qualquer forma o Supplier sempre irá instanciar uma Stream nova
        Supplier<Stream<Integer>> stream = () -> Stream.of(1);

        System.out.println(stream.get().count());
        System.out.println(stream.get().count());

        //O output do print de uma Stream é horrível
        System.out.println(Stream.of(1));
    }

    @Test
    public void operadoresTerminais() {
        System.out.println(Stream.of(1, 2, 3).anyMatch(x -> x == 1));
        System.out.println(Stream.of(1, 2, 3).allMatch(x -> x == 1));
        System.out.println(Stream.of(1, 2, 3).noneMatch(x -> x == 0));

        System.out.println(Stream.of(1).findAny());
        System.out.println(Stream.of(1).findFirst());

        System.out.println(Stream.of(1, 2).max(Comparator.comparing(x -> x)));
        System.out.println(Stream.of(1, 2).min(Comparator.comparing(x -> x)));

        Stream.of("F", "a", "b", "r", "i", "c", "i", "o")
                .reduce((a, b) -> a + b)
                .ifPresent(System.out::println);

        System.out.println(Stream.of("F", "a", "b", "r", "i", "c", "i", "o")
                .reduce("", (a, b) -> a + b));

        System.out.println(Stream.of("boi", "vaca", "cachorro").collect(Collectors.joining(", ")));
        System.out.println(Stream.of("boi", "vaca", "cachorro").collect(Collectors.toList()));

        //É possível gerar Streams infinitas!
        //A partir de um Supplier com o .generate() e de uma seed e UnaryOperator .iterate.
        //Ao limitarmos elas, é possível imprimí-las com o operador terminal forEach.
        //Caso contrário... Iremos imprimir até acabar a memória da JVM e crashar. :(
        Stream.generate(Math::random).limit(10).forEach(System.out::println);
        Stream.iterate(1, x -> x + 1).limit(10).forEach(System.out::println);
    }

    @Test
    public void intermediateStream() {
        //Os principais operadores intermediários das Streams' são os seguintes:
        Stream.of("rato", "cachorro", "cachorro", "vaca", "vaca", "gato", "gato", "jacare", "cobra", "cobra", "arara")
                .skip(1)
                .limit(8)
                .distinct()
                .filter(s -> s.length() <= 5)
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);

        //O output final é: COBRA, GATO, VACA
        //Apenas olhando é possível deduzir!
        //Porém, como uma Stream funciona DE FATO?
        //Neste exemplo é utilizado o .peek(Consumer c), ele é um operador intermediário.
        //O peek é ótimo para imprimir nossa Stream, porém consegue adivinhar o que ele vai printar?
        //O que acontece se eu colocar um map antes de limitar uma Stream infinita?
        //O que acontece se eu colocar um sorted() antes de limitar uma Stream infinita?
        Stream.generate(() -> "a")
                .peek(System.out::println)
                .map(String::toUpperCase)
                .peek(System.out::println)
                .limit(5)
                .forEach(System.out::println);

        //Explicação relativamente longa passo a passo sobre TUDO que acontece aqui.
        //Praticamente um teste de mesa.
        Stream.of("rato", "cachorro", "cachorro", "vaca", "vaca", "gato", "gato", "jacare", "cobra", "cobra", "arara")
                .peek(System.out::println)
                .skip(1)
                .peek(System.out::println)
                .limit(8)
                .peek(System.out::println)
                .distinct()
                .peek(System.out::println)
                .filter(s -> s.length() <= 5)
                .peek(System.out::println)
                .map(String::toUpperCase)
                .peek(System.out::println)
                .sorted()
                .forEach(System.out::println);
    }

    public int fib(int n) {
        if (n <= 1) return n;
        return fib(n - 1) + fib(n - 2);
    }

    @Test
    public void fibonacciTest() {
        //Se é possível fazer uma Stream infinita a partir de uma Seed e um UnaryOperator...
        //Será que é possível implementar a sequência de Fibonacci?

        //Essa é a implementação clássica com recursividade
        //Nela é impressa os 10 primeiros números da sequência de Fibonacci
        for (int i = 0; i < 10; i++) {
            System.out.println(fib(i));
        }

        //Também podemos descartar o clássico for e fazermos da seguinte forma
        IntStream.range(0, 10)
                .forEach(i -> System.out.println(fib(i)));

        //Porém, se quisermos gerar uma Stream que consiga imprimir a sequência de Fibonacci...
        //Devemos fazer da seguinte forma:
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
                .map(t -> t[0])
                .forEach(System.out::println);

        //E se eu quiser colocar a sequência numa lista?
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
                .map(t -> t[0])
                .collect(Collectors.toList());

        //E se eu quiser somar todos os números?
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
                .map(t -> t[0])
                .reduce((a, b) -> a + b)
                .ifPresent(System.out::println);

        //Também podemos utilizar o IntStream para somar e retornar um int primitivo
        int soma = Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
                .map(t -> t[0])
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println(soma);

        //Tudo bem... Mas... e se eu quiser pegar o milionésimo número da sequência de Fibonacci?
        Stream.iterate(new BigInteger[]{
                BigInteger.ZERO, BigInteger.ONE}, t -> new BigInteger[]{t[1], t[0].add(t[1])})
                .limit(1_000_000)
                .map(n -> n[1]) // find, we need n[1]
                .reduce((a, b) -> b)
                .ifPresent(System.out::println);

    }

    @Test
    public void flatMap() {
        //O operador flatMap é um tanto quanto curioso, porém muito útil.
        //O que acontece se quisermos fazer operações com Stream de Listas, ou Stream de Stream?
        //E se foi me dado várias listas, onde eu preciso pegar todos seus valores únicos e imprimí-los?
        List<String> mamiferos = Arrays.asList("rato", "cachorro", "cachorro", "vaca", "vaca", "gato", "gato");
        List<String> repteis = Arrays.asList("jacare", "cobra", "cobra");
        List<String> aves = Arrays.asList("arara");
        List<String> insetos = Arrays.asList();

        //Utilizando flatMap!
        Stream.of(mamiferos, repteis, aves, insetos)
                .flatMap(x -> x.stream())
                .distinct()
                .forEach(System.out::println);

        //Exemplo sem a utilização do flatMap...
        //Note que utilizmos 2 Streams, uma vez que o reduce é um operador terminal!
        Stream.of(mamiferos, repteis, aves, insetos)
                .map(x -> x.stream())
                .reduce(Stream.of(), (a, b) -> Stream.concat(a, b))
                .distinct()
                .forEach(System.out::println);


        //Outro exemplo, porém com concatenação de Streams e utilização do .stream().
        List<List<String>> algunsAnimaisPorLetra = Arrays.asList(Arrays.asList("rato", "rinoceronte"), Arrays.asList("vaca", "veado"));

        //A partir do Java 8 as implementações de List possuem o .stream(), que convertem listas em... Stream!
        Stream<List<String>> streamAnimaisPorLetra = algunsAnimaisPorLetra.stream();
        Stream<List<String>> streamAnimais = Stream.of(mamiferos, repteis, aves, insetos);

        //É possível concatenar streams!
        Stream<List<String>> concatAnimais = Stream.concat(streamAnimais, streamAnimaisPorLetra);

        concatAnimais.flatMap(x -> x.stream()).forEach(System.out::println);
    }

    @Test
    public void basicoDeNumberStream() {
        //É interessante saber que esses tipos de Stream existem!
        IntStream.range(1, 5).forEach(System.out::println);
        IntStream.rangeClosed(1, 5).forEach(System.out::println);
        System.out.println(IntStream.of(1, 2, 3, 4, 5).sum());
        IntStream.of(1, 5, 0, 3, 2).average().ifPresent(System.out::println);
        DoubleStream.of(1, 2, 3, 4, 5).max().ifPresent(System.out::println);
        LongStream.of(1, 2, 3, 4, 5).min().ifPresent(System.out::println);
    }

    @Test
    public void collectTest() {
        //O operador mais importante e complicado, na minha opinião.

        Supplier<Stream<String>> stringStream = () -> Stream.of("Pikachu", "Squirtle", "Charmander");

        //Criar strings
        System.out.println(stringStream.get().collect(Collectors.joining(", ")));

        //Encontrar a média de alguma coisa
        System.out.println(stringStream.get().collect(Collectors.averagingInt(String::length)));


        //Collect simples com objetos
        System.out.println(getListaDeUsuarios().stream()
                .map(Usuario::getNome)
                .collect(Collectors.joining(", ", "Nomes: ", ".")));

        System.out.println("Média de idade: " +
                getListaDeUsuarios().stream()
                        .peek((usuario) -> System.out.println(usuario.getIdade()))
                        .collect(Collectors.averagingDouble(Usuario::getIdade)));

        //Collect com Maps (aqui começa a ficar útil! e complicado...)
        Map<String, Integer> m1 = getListaDeUsuarios().stream()
                .collect(Collectors.toMap(Usuario::getNome, Usuario::getIdade));
        System.out.println(m1);

        //Porém, e se eu quisesse que a idade fosse a chave?
        try {
            Map<Integer, String> m2 = getListaDeUsuarios().stream()
                    .collect(toMap(Usuario::getIdade, Usuario::getNome));
        } catch (IllegalStateException e) {
            System.out.println(e);
        }

        //Essa Exception é lançada pois temos duas pessoas com a mesma idade, e não podemos ter chaves duplicadas.
        //Portanto, o que fazer? Adicionar um lambda a ser executado caso isso ocorra!
        Map<Integer, String> m2 = getListaDeUsuarios().stream()
                .collect(toMap(Usuario::getIdade, Usuario::getNome, (u1, u2) -> "(" + u1 + ", " + u2 + ")"));
        System.out.println(m2);

        //Porém, no nosso caso, há formas melhores de fazer isso.
        //E se ao invés de concatenarmos uma String, salvássemos em uma List?
        Map<Integer, List<String>> m3 = getListaDeUsuarios().stream()
                .collect(groupingBy(Usuario::getIdade, mapping(Usuario::getNome, toList())));
        System.out.println(m3);

        //E se eu quisesse o objeto Usuario ao invés do nome?
        Map<Integer, List<Usuario>> m4 = getListaDeUsuarios().stream()
                .collect(groupingBy(Usuario::getIdade));
        System.out.println(m4);

        //Pela primeira letra do nome?
        Map<Character, List<String>> m5 = getListaDeUsuarios().stream()
                .collect(groupingBy(u -> u.getNome().charAt(0), mapping(Usuario::getNome, toList())));
        System.out.println(m5);

        //E se eu quiser criar um map dividido em pessoas com mais de 25 anos?
        Map<Boolean, List<String>> m6 = getListaDeUsuarios().stream()
                .collect(partitioningBy(u -> u.getIdade() > 25, mapping(Usuario::getNome, toList())));
        System.out.println(m6);

        //E se eu quiser contar a quantidade de pessoas que atendem essa condição?
        Map<Boolean, Long> m7 = getListaDeUsuarios().stream()
                .collect(partitioningBy(u -> u.getIdade() > 25, counting()));
        System.out.println(m7);

    }

}
