package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static br.com.caelum.leilao.matchers.LeilaoMatcher.temUmLance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

public class AvaliadorTest {

    private Avaliador leiloeiro;
    private Usuario joao;
    private Usuario jose;
    private Usuario maria;

    @Before
    public void setUp() {
        leiloeiro = new Avaliador();

        this.joao = new Usuario("Joao");
        this.jose = new Usuario("José");
        this.maria = new Usuario("Maria");
    }

    @After
    public void finaliza() {
        System.out.println("fim");
    }

    @Test(expected = RuntimeException.class)
    public void naoDeveAvaliarLeiloesSemNenhumLanceDado() {
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo").constroi();

        leiloeiro.avalia(leilao);
    }

    @Test
    public void deveEntenderLancesEmOrdemCrescente() {
        // parte 1: cenario
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 250)
                .lance(jose, 300)
                .lance(maria, 400)
                .constroi();

        // parte 2: acao
        leiloeiro.avalia(leilao);

        // parte 3: validacao
        double maiorEsperado = 400;
        double menorEsperado = 250;

        Lance lance = new Lance(joao, 250);
        assertThat(leilao, temUmLance(lance));
        assertThat(leiloeiro.getMaiorLance(), equalTo(maiorEsperado));
        assertThat(leiloeiro.getMenorLance(), equalTo(menorEsperado));
    }

    @Test
    public void deveEntenderLeilaoComApenasUmLance() {
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 1000.0)
                .constroi();

        leiloeiro.avalia(leilao);

        assertThat(leiloeiro.getMaiorLance(), equalTo(1000.0));
        assertThat(leiloeiro.getMenorLance(), equalTo(1000.0));
    }

    @Test
    public void deveCalcularAMedia() {
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 300)
                .lance(jose, 400)
                .lance(maria, 500)
                .constroi();

        // executando a ação
        leiloeiro.avalia(leilao);

        // comparando a saido com o esperado
        assertThat(leiloeiro.getMedia(), equalTo(400.0));
    }

    @Test
    public void deveEncontrarOsTresMaioresLances() {
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 100.0)
                .lance(maria, 200.0)
                .lance(joao, 300.0)
                .lance(maria, 400.0)
                .constroi();

        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();

        assertThat(maiores.size(), equalTo(3));

        assertThat(maiores, hasItems(
                new Lance(maria, 400),
                new Lance(joao, 300),
                new Lance(maria, 200)
        ));
    }

    @Test
    public void deveAvaliarLeilaoComApenasUmLance() { // já tem um teste desse
        Leilao leilao = new CriadorDeLeilao().para("Notebook novo")
                .lance(maria, 200.0)
                .constroi();

        leiloeiro.avalia(leilao);

        assertThat(leiloeiro.getMaiorLance(), equalTo(200.0));
        assertThat(leiloeiro.getMenorLance(), equalTo(200.0));
        assertThat(leiloeiro.getMedia(), equalTo(200.0));
    }

    @Test
    public void deveEntenderLeilaoComLancesRandomicos() {
        Leilao leilao = new CriadorDeLeilao().para("Galaxy X")
                .lance(maria, 200.0)
                .lance(jose, 450.0)
                .lance(joao, 120.0)
                .lance(jose, 700.0)
                .lance(maria, 630.0)
                .lance(jose, 230.0)
                .constroi();

        leiloeiro.avalia(leilao);

        assertThat(leiloeiro.getMenorLance(), equalTo(120.0));
        assertThat(leiloeiro.getMaiorLance(), equalTo(700.0));
    }

    @Test
    public void deveEntenderLeilaoComLancesEmOrdemDecrescente() {
        Leilao leilao = new CriadorDeLeilao().para("Moto X")
                .lance(maria, 400)
                .lance(jose, 300)
                .lance(maria, 200)
                .lance(jose, 100)
                .constroi();

        leiloeiro.avalia(leilao);

        assertThat(leiloeiro.getMenorLance(), equalTo(100.0));
        assertThat(leiloeiro.getMaiorLance(), equalTo(400.0));
    }

    @Test
    public void deveEntenderOsTresMaioresLancesParaCincoLances() {
        Leilao leilao = new CriadorDeLeilao().para("carro 0km")
                .lance(joao, 4000.0)
                .lance(jose, 800.0)
                .lance(joao, 8000.0)
                .lance(jose, 78400.0)
                .lance(joao, 400300.0)
                .constroi();

        leiloeiro.avalia(leilao);

        assertThat(leiloeiro.getTresMaiores().size(), equalTo(3));
        assertThat(leiloeiro.getTresMaiores(), hasItems(
                new Lance(joao, 400300.0),
                new Lance(jose, 78400.0),
                new Lance(joao, 8000.0)
        ));
    }

    @Test
    public void deveEntenderOsTresMaioresLancesParaDoisLancesDados() {
        Leilao leilao = new CriadorDeLeilao().para("carro 0km")
                .lance(maria, 4000.0)
                .lance(joao, 800.0)
                .constroi();

        leiloeiro.avalia(leilao);

        assertThat(leiloeiro.getTresMaiores().size(), equalTo(2));
        assertThat(leiloeiro.getTresMaiores(), hasItems(
                new Lance(maria, 4000.0),
                new Lance(joao, 800.0)
        ));
    }
}
