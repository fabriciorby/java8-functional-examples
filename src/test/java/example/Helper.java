package example;

import example.pojo.Usuario;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Helper {

    //Antes explicar method references
    private static Usuario criaUsuario(Integer i) {
        List<String> nomes = Arrays.asList("Hugo", "Fabricio", "Vinicius", "Victor", "Ariella");
        List<String> sobrenomes = Arrays.asList("Ogawa", "Yamamoto", "Fonseca", "Hideki", "Yamada");
        List<String> cpfs = Arrays.asList("12341243", "3245124", "54323245", "1235436", "324324");
        List<Integer> idade = Arrays.asList(25, 52, 25, 50, 30);
        return new Usuario(nomes.get(i), sobrenomes.get(i), cpfs.get(i), idade.get(i), i);
    }

    public static List<Usuario> getListaDeUsuarios() {
        return IntStream.range(0, 5)
                .mapToObj(Helper::criaUsuario)
                .collect(toList());
    }
}
