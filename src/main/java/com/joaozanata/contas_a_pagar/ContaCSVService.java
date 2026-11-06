package com.joaozanata.contas_a_pagar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ContaCSVService implements ContasService{
    private static final String SEPARADOR = ";";
    private static final Path ARQUIVO_SAIDA = Paths.get("./dados.csv");
    private List<Conta> contas;
    final SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
    public ContaCSVService(){
        carregaDados();
    }

    @Override
    public void salvar(Conta conta) {
        conta.setId(ultimoId() + 1);
        contas.add(conta);
        salvaDados();
    }

    @Override
    public void atualizar(Conta conta) {
        Conta contaAntiga = buscaPorId(conta.getId());
        contaAntiga.setDataVencimento(conta.getDataVencimento());
        contaAntiga.setDescricao(conta.getDescricao());
        contaAntiga.setForma(conta.getForma());
        contaAntiga.setParcela(conta.getParcela());
        contaAntiga.setStatus(conta.getStatus());
        contaAntiga.setValor(conta.getValor());
        salvaDados();
    }

    @Override
    public List<Conta> buscarContas() {
        return contas;
    }

    @Override
    public void apagar(int id) {
        Conta conta = buscaPorId(id);
        contas.remove(conta);
        salvaDados();
    }

    public Conta buscaPorId(int id) {
        return contas.stream().filter(c -> c.getId() == id).findFirst().orElseThrow(() -> new Error("Conta não encontrada"));
    }

    private void salvaDados(){
        StringBuilder sb = new StringBuilder();
        for(Conta c : contas){
            String linha = criaLinha(c);
            sb.append(linha);
            sb.append(System.lineSeparator());
        }
        try {
            Files.delete(ARQUIVO_SAIDA);
            Files.write(ARQUIVO_SAIDA, sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private int ultimoId(){
        return contas.stream().mapToInt(Conta::getId).max().orElse(0);
    }

    private void carregaDados(){
        try {
            if(!Files.exists(ARQUIVO_SAIDA)){
                Files.createFile(ARQUIVO_SAIDA);
            }
            contas = Files.lines(ARQUIVO_SAIDA).map(this::LeLinha).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private Conta LeLinha(String linha){
        String[] colunas = linha.split(SEPARADOR);
        int id = Integer.parseInt(colunas[0]);
        Date dataVencimento = null;
        try {
            dataVencimento = formatoData.parse(colunas[1]);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(0);
        }
        Conta conta = new Conta();
        conta.setId(id);
        conta.setDataVencimento(dataVencimento);
        conta.setDescricao(colunas[2]);
        conta.setForma(colunas[3]);
        conta.setParcela(colunas[4]);
        conta.setStatus(colunas[5]);
        conta.setValor(colunas[6]);
        return conta;
    }

    private String criaLinha(Conta c) {
        String dataStr = formatoData.format(c.getDataVencimento());
        String idStr = String.valueOf(c.getId());
        return String.join(SEPARADOR, idStr, dataStr, c.getDescricao(), c.getForma(), c.getParcela(), c.getStatus(), c.getValor());
    }
}
