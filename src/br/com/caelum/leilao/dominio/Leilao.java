package br.com.caelum.leilao.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leilao {

	private String descricao;
	private List<Lance> lances;
	
	public Leilao(String descricao) {
		this.descricao = descricao;
		this.lances = new ArrayList<Lance>();
	}
	
	public void propoe(Lance lance) {
		if (lances.isEmpty() || podeDarLance(lance.getUsuario())) {

			lances.add(lance);
		}
	}

	public void dobraLance(Usuario usuario) {
		Double ultimoLance = ultimoLanceDoUsuario(usuario);

		if (ultimoLance != null) {
			propoe(new Lance(usuario, ultimoLance * 2));
		}
	}

	private Double ultimoLanceDoUsuario(Usuario usuario) {
		Double valorLance = null;

		for (Lance l : lances) {
			if (l.getUsuario().equals(usuario)) {
				valorLance = l.getValor();
			}
		}
		return valorLance;
	}

	public String getDescricao() {
		return descricao;
	}

	public List<Lance> getLances() {
		return Collections.unmodifiableList(lances);
	}

	private boolean podeDarLance(Usuario usuario) {
		return !ultimoLanceDado().getUsuario().equals(usuario) && qtdDeLancesDo(usuario) < 5;
	}

	private int qtdDeLancesDo(Usuario usuario) {
		int total = 0;

		for (Lance l : lances) {
			if (l.getUsuario().equals(usuario)) total++;
		}
		return total;
	}

	private Lance ultimoLanceDado() {
		return lances.get(lances.size()-1);
	}
}
