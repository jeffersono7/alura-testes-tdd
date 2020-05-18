package br.com.caelum.leilao.dominio;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import org.hamcrest.MatcherAssert;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static br.com.caelum.leilao.matchers.LeilaoMatcher.temUmLance;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class LeilaoTest {

    private Usuario steveJobs;
    private Usuario steveWozniak;
    private Usuario billGates;
    private Usuario jefferson;

    @BeforeClass
    public static void testandoBeforeClass() {
        System.out.println("before class");
    }

    @AfterClass
    public static void testandoAfterClass() {
        System.out.println("after class");
    }

    @Before
    public void setUp() {
        steveJobs = new Usuario("Steve Jobs");
        steveWozniak = new Usuario("Steve Wozniak");
        billGates = new Usuario("Bill Gates");
        jefferson = new Usuario("Jefferson");
    }

    @Test
    public void deveReceberUmLance() {
        Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15").constroi();

        assertEquals(0, leilao.getLances().size());

        Lance lance = new Lance(new Usuario("Steve Jobs"), 2000);
        leilao.propoe(lance);

        assertThat(leilao.getLances().size(), equalTo(1));
        assertThat(leilao, temUmLance(lance));
    }

    @Test
    public void deveReceberVariosLances() {
        Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
                .lance(steveJobs, 2000)
                .lance(steveWozniak, 3000)
                .constroi();

        assertEquals(2, leilao.getLances().size());
        assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
        assertEquals(3000.0, leilao.getLances().get(1).getValor(), 0.00001);
    }

    @Test
    public void naoDeveAceitarDoisLancesSeguidosDoMesmoUsuario() {
        Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
                .lance(steveJobs, 2000.0)
                .lance(steveJobs, 3000.0)
                .constroi();

        assertEquals(1, leilao.getLances().size());
        assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
    }

    @Test
    public void naoDeveAceitarMaisDoQue5LancesDeUmMesmoUsuario() {
        Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
                .lance(steveJobs, 2000.0)
                .lance(billGates, 3000.0)
                .lance(steveJobs, 4000.0)
                .lance(billGates, 5000.0)
                .lance(steveJobs, 6000.0)
                .lance(billGates, 7000.0)
                .lance(steveJobs, 8000.0)
                .lance(billGates, 9000.0)
                .lance(steveJobs, 10000.0)
                .lance(billGates, 11000.0)
                .constroi();

        // deve ser ignorado
        leilao.propoe(new Lance(steveJobs, 12000.0));

        assertEquals(10, leilao.getLances().size());
        assertEquals(11000.0, leilao.getLances().get(leilao.getLances().size() - 1).getValor(), 0.00001);
    }

    @Test
    public void deveDobrarUltimoLanceDoUsuario() {
        Leilao leilao = new CriadorDeLeilao().para("Avell I9 Extreme")
                .lance(jefferson, 900.0)
                .lance(steveJobs, 1000.0)
                .constroi();

        leilao.dobraLance(jefferson);

        assertEquals(3, leilao.getLances().size());
        assertEquals(1800.0, leilao.getLances().get(2).getValor(), 0.00001);
    }

    @Test
    public void naoDeveDobrarUltimoLanceParaUsuarioQueNaoTenhaFeitoAlgumLanceAnteriormente() {
        Leilao leilao = new CriadorDeLeilao().para("Raspberry PI")
                .lance(steveJobs, 700.0)
                .constroi();

        leilao.dobraLance(jefferson);

        assertEquals(1, leilao.getLances().size());
    }
}
