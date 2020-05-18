package br.com.caelum.leilao.dominio;

public class AnoBissexto {

    public boolean ehBissexto(int ano) {
        if (ano % 400 == 0) return true;
        if (ano % 100 == 0) return false;

        return (ano % 4) == 0;
    }
}
