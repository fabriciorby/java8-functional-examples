package example;

import example.pojo.Usuario;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class OptionalTest {


    @Test
    public void exemploOptional() {

        List<Usuario> listaUsuario = new ArrayList();
        Usuario usuario = new Usuario();
        usuario.setNome("Ariella");
        usuario.setIdade(25);
        listaUsuario.add(usuario);

        //Como saber se há pelo menos um usuário válido numa lista.
        //Pré enhanced-for:
        Boolean valido = false;
        if (listaUsuario != null && !listaUsuario.isEmpty()) {
            for (int i = 0; i < listaUsuario.size(); i++) {
                Usuario user = listaUsuario.get(i);
                if (user.getIdade() > 20 && user.getNome().startsWith("A")) {
                    valido = true;
                }
            }
        }
        if (valido)
            System.out.println(valido);

        //Enhanced-for:
        valido = false;
        if (listaUsuario != null) {
            for (Usuario user : listaUsuario) {
                if (user.getIdade() > 20 && user.getNome().startsWith("A")) {
                    valido = true;
                }
            }
        }
        if (valido)
            System.out.println(valido);

        //Optional:
        final Predicate<Usuario> idadeMaiorQue20 = user -> user.getIdade() > 20;
        final Predicate<Usuario> nomeIniciaComA = user -> user.getNome().startsWith("A");
        Optional.ofNullable(listaUsuario)
                .map(x -> x.stream())
                .map(x -> x.anyMatch(idadeMaiorQue20.and(nomeIniciaComA)))
                .ifPresent(System.out::println);


        //Exemplos de como se criar um Optional
        //ofNullable e orElse
        listaUsuario = null;
        Optional.ofNullable(listaUsuario)
                .orElse(new ArrayList<>());

        //thenThrow
        try {
            Optional.ofNullable(listaUsuario)
                    .orElseThrow(() -> new IllegalStateException("null"));
        } catch (IllegalStateException e) {
            System.out.println(e);
        }

        //of estourando NullPointer
        try {
            Optional.of(listaUsuario)
                    .orElseThrow(() -> new IllegalStateException("null"));
        } catch (NullPointerException e) {
            System.out.println(e);
        }

    }
}
