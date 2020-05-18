package br.com.caelum.leilao.dominio;

import org.junit.Before;
import org.junit.Test;

public class LanceTest {

    private Usuario usuario;

    @Before
    public void setUp() {
        usuario = new Usuario("usuario");
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveRecusarLancesComValorNegativo() {
        new Lance(usuario, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveRecusarLancesComValorDeZero() {
        new Lance(usuario, 0);
    }
}
