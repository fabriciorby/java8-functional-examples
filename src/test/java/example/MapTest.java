package example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MapTest {
    Map<String, String> map = new HashMap<>();

    @BeforeEach
    public void populateMap() {
        map.clear();
        map.put("Fabricio", "Yamamoto");
        map.put("Victor", "Yamamoto");
        map.putIfAbsent("Victor", "da Silva");
        map.putIfAbsent("Fabricio", "da Silva");
        map.putIfAbsent("Hugo", "Ogawa");
        System.out.println(map);
    }

    @Test
    public void mergeMap() {
        //Como adicionar um value num Map dado determinada condição
        //Pré merge
        final String sobrenome = "da Silva";
        if (sobrenome.endsWith("da Silva")) {
            map.put("Fabricio", sobrenome);
        }
        System.out.println(map);

        //Com merge - s1 é o value existente, s2 é o value que eu desejo adicionar
        map.merge("Fabricio", "Algo da Silva", (s1, s2) -> s2.endsWith("da Silva") ? s2 : s1);
        //Caso o item não exista no Map, então ele simplesmente é adicionado
        map.merge("Ariella", "Yamada", (s1, s2) -> s2.endsWith("da Silva") ? s2 : s1); //ignora mapping
        System.out.println(map);
        //Caso o retorno da condição seja null, o valor é deletado do Map
        map.merge("Ariella", "Yamada", (s1, s2) -> s2.endsWith("da Silva") ? s2 : null); //null retira item
        System.out.println(map);
    }

    @Test
    public void computeMap() {
        //Aplicar uma função em um determinado value a partir de uma key
        //Aplicar caso exista value para o key
        map.computeIfPresent("Fabricio", (k, v) -> v.concat(" da Silva"));
        //Aplicar caso não exista value para o key
        map.computeIfAbsent("Fabricio", (k) -> new StringBuilder(k).reverse().toString()); //não aplica
        map.computeIfAbsent("Ariella", (k) -> new StringBuilder(k).reverse().toString()); //aplica
        System.out.println(map);
    }
}
