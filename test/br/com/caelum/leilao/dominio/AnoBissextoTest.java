package br.com.caelum.leilao.dominio;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class AnoBissextoTest {

    Function<Integer, Boolean> expectedRule = ano -> {
        if (ano % 400 == 0) return true;
        if (ano % 100 == 0) return false;
        return ano % 4 == 0;
    };

    @Test
    public void deveVerificarQueOAnoEhBissextoParaAnosBissexto() {
        List<Integer> anos = gerarAnosMultiplosDe(4, 500);
        AnoBissexto anoBissexto = new AnoBissexto();

        anos.forEach(ano -> {
            boolean ehBissexto = anoBissexto.ehBissexto(ano.intValue());

            assertEquals(expectedRule.apply(ano), ehBissexto);
        });
    }

    @Test
    public void deveVerificarQueOAnoNaoEhBissexto() {
        int ano = 2019;
        AnoBissexto anoBissexto = new AnoBissexto();

        boolean ehBissexto = anoBissexto.ehBissexto(ano);

        assertEquals(false, ehBissexto);
    }

    private List<Integer> gerarAnosMultiplosDe(int multiplo, int quantidade) {
        List anos = new ArrayList<Integer>();

        for (int a = 0; a < quantidade; a++) {
            anos.add(Integer.valueOf(a * multiplo));
        }

        return anos;
    }
}
