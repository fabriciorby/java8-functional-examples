package example;

import example.pojo.Usuario;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;

public class FunctionalTest {

    //Colocar num ppt e rodar só pra dizer que compila LOL
    @Test
    public void lambdaSyntax() {
        //Exemplo de lambda com apenas uma linha
        Function<Integer, Integer> f0 = i -> i++;

        //Exemplo de lambda com bloco de código
        Function<Integer, Integer> f1 =
                i -> {
                    i = i++;
                    return i / 2;
                };

        //Exemplo de código com descrição da variável do lambda
        Function<Integer, Integer> f2 =
                (Integer i) -> {
                    i = i++;
                    return i / 2;
                };
    }

    @Test
    public void supplierTest() {
        //Supplier: Não recebe parâmetros, apenas retorna.
        //Utiliza o get() para ser executado.
        //Com Generics e lambda
        Supplier<Usuario> novoUsuario1 = () -> new Usuario();
        Usuario usuario1 = novoUsuario1.get();

        //Sem Generics e method reference
        //Notar a necessidade do casting explícito
        Supplier novoUsuario2 = Usuario::new;
        Usuario usuario2 = (Usuario) novoUsuario2.get();

        //Exemplo bobo retornando um new ArrayList<Usuario>()
        Supplier<List<Usuario>> novaListaDeUsuario = ArrayList::new;
        List listaUsuario = novaListaDeUsuario.get();

        //Adicionando usuários do Supplier apenas para testar
        listaUsuario.add(usuario1);
        listaUsuario.add(usuario2);

        System.out.println(listaUsuario);
    }

    @Test
    public void consumerTest() {
        //Consumer: Recebe parâmetro, porém não retorna nada (void).
        //Utiliza o accept(T t) para ser executado.
        Consumer<String> print1 = (s) -> System.out.println(s); //lambda
        Consumer<String> print2 = System.out::println; //method reference
        print1.accept("primeiro print");
        print2.accept("segundo print");

        Supplier<Map<String, String>> stringMap = HashMap::new;
        Map<String, String> nomeSobrenome = stringMap.get();
        BiConsumer<String, String> putNome1 = (k, v) -> nomeSobrenome.putIfAbsent(k, v); //lambda
        BiConsumer<String, String> putNome2 = nomeSobrenome::putIfAbsent; //method reference

        putNome1.accept("Fabricio", "Yamamoto");
        putNome2.accept("Hugo", "Ogawa");
        print1.accept(nomeSobrenome.toString());
    }

    @Test
    public void predicateTest() {
        //Predicate: Recebe parâmetro e retorna um primitive boolean
        //Utiliza o test(T t) para ser executado.
        Predicate<String> p1 = (s) -> s.isEmpty(); //lambda
        Predicate<String> p2 = String::isEmpty; //method reference

        System.out.println(p1.test(""));
        System.out.println(p2.test("notEmpty"));

        BiPredicate<String, String> b1 = (s1, s2) -> s1.contains(s2);
        BiPredicate<String, String> b2 = String::contains;

        System.out.println(b1.test("Fabricio", "F"));
        System.out.println(b2.test("Fabricio", "c"));

        Predicate<String> comecaComA = s -> s.toUpperCase().startsWith("A");
        Predicate<String> terminaComO = s -> s.toUpperCase().endsWith("O");

        //Exemplos úteis para ver como o código pode ficar limpo
        System.out.println(comecaComA.and(terminaComO).test("Alberto"));
        System.out.println(comecaComA.or(terminaComO).test("Alberta"));
        System.out.println(comecaComA.and(terminaComO.negate()).test("Alberto"));
    }

    @Test
    public void functionTest() {
        //Function: Recebe parâmetro e retorna um parâmetro
        //Utiliza o apply(T t) para ser executado.
        Function<String, Integer> f1 = s -> s.length(); //lambda
        Function<String, Integer> f2 = String::length; //method reference

        System.out.println(f1.apply("Fabricio")); //8
        System.out.println(f2.apply("Fabricio")); //8

        BiFunction<String, String, String> b1 = (s1, s2) -> s1.concat(s2);
        BiFunction<String, String, String> b2 = String::concat;

        System.out.println(b1.apply("Fab", "ricio"));
        System.out.println(b2.apply("Fab", "ricio"));
        System.out.println(b1.andThen(f1).apply("Fab", "ricio"));

        //Diferença entre Predicate<String> e uma Function<String, Boolean>
        //Predicate retorna primitivo e Function retorna o wrapper
        Predicate<String> p1 = String::isEmpty;
        System.out.println(p1.test(""));
        Function<String, Boolean> f3 = String::isEmpty;
        System.out.println(f3.apply(""));

        //É possível criar novas interfaces funcionais!
        TriFunction<String, String, String, String> t1 = String::replaceAll;
        System.out.println(t1.apply("Fabricio", "i", "a"));
    }

    //Qualquer interface com apenas um método abstrato é uma interface funcional
    @FunctionalInterface
    interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    @Test
    public void operatorTest() {
        //Operator: Recebe parâmetro e retorna parâmetro do mesmo tipo
        //Vantagem: Reduz a verbosidade do Function
        UnaryOperator<String> capsLock = String::toUpperCase;
        Function<String, String> f1 = String::toUpperCase;

        BinaryOperator<String> concatena = String::concat;
        BiFunction<String, String, String> b2 = String::concat;

        //Possível aplicar chaining nas funções
        System.out.println(concatena.andThen(capsLock).apply("Fabricio ", "Yamamoto"));
    }

}


