# Contas a Pagar

Aplicativo desktop para gestão de contas a pagar, desenvolvido com **Java** e **JavaFX**.

## Funcionalidades

- **Cadastro de contas:** Registre contas com data de vencimento, descrição, forma de pagamento, parcela, status e valor.
- **Edição de contas:** Altere os dados de uma conta existente.
- **Exclusão de contas:** Remova contas que não são mais necessárias.
- **Visualização de totais:** Acompanhe os valores totais de contas pagas, pendentes, vencidas e o total geral.
- **Persistência em CSV:** Os dados são salvos automaticamente no arquivo `dados.csv`, permitindo que as informações sejam mantidas entre execuções.

## Tecnologias

- **Java 9+** (módulos JPMS)
- **JavaFX 21.0.6** (interface gráfica)
- **Maven** (gerenciamento de dependências e build)
- **CSV** (armazenamento local dos dados)

## Estrutura do projeto

```
Contas_a_Pagar/
├── src/main/java/com/joaozanata/contas_a_pagar/
│   ├── ContasApplication.java      # Ponto de entrada da aplicação JavaFX
│   ├── Launcher.java               # Classe main para lançamento
│   ├── ContasController.java       # Controlador da interface (lógica da view)
│   ├── ContasService.java          # Interface do serviço de contas
│   ├── ContaCSVService.java        # Implementação do serviço com persistência em CSV
│   ├── Conta.java                  # Modelo de dados (entidade Conta)
│   └── module-info.java            # Declaração de módulos Java
├── src/main/resources/com/joaozanata/contas_a_pagar/
│   └── lancamentos.fxml            # Layout da interface gráfica (FXML)
├── dados.csv                       # Arquivo de dados (gerado automaticamente)
├── pom.xml                         # Configuração do Maven
└── README.md                       # Este arquivo
```

## Pré-requisitos

- **JDK 9 ou superior**
- **Maven 3.x**

## Como executar

### Com Maven

```bash
mvn clean javafx:run
```

### Com o wrapper Maven

```bash
# Windows
.\mvnw.cmd clean javafx:run

# Linux / macOS
./mvnw clean javafx:run
```

### Compilando o projeto

```bash
mvn clean package
```

## Formato do arquivo CSV

O arquivo `dados.csv` é criado automaticamente na primeira execução. Cada linha segue o formato:

```
id;data_vencimento;descricao;forma;parcela;status;valor
```

Exemplo:

```
1;01/12/2025;Aluguel;PIX;1/1;Pendente;2500.00
2;15/11/2025;Energia;Boleto;1/1;Paga;180.50
```

### Campos

| Campo           | Descrição                          |
|-----------------|------------------------------------|
| `id`            | Identificador único da conta       |
| `data_vencimento` | Data de vencimento (dd/MM/yyyy)  |
| `descricao`     | Descrição da conta                 |
| `forma`         | Forma de pagamento                 |
| `parcela`       | Número da parcela (ex: 1/3)       |
| `status`        | Status da conta (Pendente, Paga, Vencida) |
| `valor`         | Valor da conta                     |

## Status das contas

O aplicativo reconhece os seguintes status:

- **Pendente:** Conta ainda não paga e não vencida.
- **Paga:** Conta já quitada.
- **Vencida:** Conta com vencimento ultrapassado e não paga.

## Autor

Desenvolvido por **João Zanata** (`com.joaozanata`).
