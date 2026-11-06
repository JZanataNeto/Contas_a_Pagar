package com.joaozanata.contas_a_pagar;

import java.util.List;

public interface ContasService {
    void salvar(Conta conta);

    void atualizar(Conta conta);

    List<Conta> buscarContas();

    void apagar(int id);

    static ContasService getInstance() {
        return new ContaCSVService();
    }
}
