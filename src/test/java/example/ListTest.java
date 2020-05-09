package example;

import example.pojo.Usuario;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;

import static example.Helper.getListaDeUsuarios;
import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;

class ListTest {

    List<Usuario> usuarios = getListaDeUsuarios();

    @Test
    public void devePrintarTodosUsuarios() {

        //Utilizando for
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println(i);
        }

        //Utilizando iterator (feio)
        Iterator<Usuario> iterator = usuarios.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        //Utilizando enhanced-for (bonito)
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }

        //Utilizando lambda/method reference (maravilhoso)
        usuarios.forEach(System.out::println);

        //É possível salvar o sysout em uma variável Consumer
        Consumer print = System.out::println;

        //Utilizar lambda/method references equivale a criar uma classe anônima
        Consumer printInstanciaAnonima = new Consumer() {
            @Override
            public void accept(Object o) {
                System.out.println(o);
            }
        };

        //A prova
        usuarios.forEach(print);
        usuarios.forEach(printInstanciaAnonima);

        //É feio, porém possível fazer isso
        //E só porque é possível, não quer dizer que devemos fazer isso
        usuarios.forEach(u -> new Consumer() {
            @Override
            public void accept(Object o) {
                System.out.println(o);
            }
        }.accept(u));

        //NUNCA FAÇA ISSO
        for (int i = 0; i < usuarios.size(); i++) {
            new Consumer() {
                @Override
                public void accept(Object o) {
                    System.out.println(o);
                }
            }.accept(usuarios.get(i));
        }

    }

    @Test
    public void devePrintarTodosUsuariosComIdadeIgualA25() {
        List<Usuario> usuarios25 = getListaDeUsuarios();
        usuarios25.removeIf(u -> u.getIdade() != 25);
        usuarios25.forEach(System.out::println);
    }

    @Test
    public void devePrintarTodosUsuariosEmOrdemAlfabetica() {
        List<Usuario> usuarios = getListaDeUsuarios();
        //Para relembrar
        //Isso
        usuarios.sort((u1, u2) -> u1.getNome().compareToIgnoreCase(u2.getNome()));
        //Equivale a isso
        usuarios.sort(new Comparator() {
            @Override
            public int compare(Object u1, Object u2) {
                return ((Usuario) u1).getNome().compareToIgnoreCase(((Usuario) u2).getNome());
            }
        });
        //É possível criar um método para forçarmos a utilização do method reference e deixar o código mais limpo
        usuarios.sort(this::ordenaPorNome);

        //Porém o Java 8 nos presenteou com a classe Comparator e seus métodos estáticos
        usuarios.sort(Comparator.comparing(Usuario::getNome));

        //E é possível utilizar static imports para reduzir a verbosidade e deixar o código mais limpo
        usuarios.sort(comparing(Usuario::getNome));
        usuarios.forEach(System.out::println);
        System.out.println();

        //Quando utilizada a classe Comparator, é possível inverter a ordem de ordenação apenas com um reversed()
        usuarios.sort(comparing(Usuario::getNome).reversed());
        usuarios.forEach(System.out::println);
        System.out.println();

        //Caso tenha algum item null na List, é possível utilizar o nullsLast ou o nullsFirst
        //Além disso é possível utilizar chaining nas ordenações
        //Aqui estamos ordenando por idade, e caso haja repetições, ordenamos por nome
        usuarios.add(null);
        usuarios.sort(nullsLast(comparing(Usuario::getIdade).thenComparing(Usuario::getNome)));
        usuarios.forEach(System.out::println);
    }

    public int ordenaPorNome(Usuario u1, Usuario u2) {
        return u1.getNome().compareToIgnoreCase(u2.getNome());
    }

    @Test
    public void modificaTodosElementos() {
        //Aplicar uma função em todos os elementos de uma lista
        List<Integer> listaDeNumeros = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        listaDeNumeros.replaceAll(x -> x * 10);
        listaDeNumeros.forEach(System.out::println);
        System.out.println();

        //Aplicar uma intersecção na lista, mantendo apenas itens em comum entre duas listas
        listaDeNumeros.retainAll(Arrays.asList(10, 20, 30));
        listaDeNumeros.forEach(System.out::println);
    }

}