# Compliance Shield

Aplicação de estudo voltada a processos de **KYC (Know Your Customer)** e **AML (Anti-Money Laundering)**. O objetivo do projeto é servir como um ponto central para cadastrar empresas, consultar bases externas simuladas e consolidar indicadores que auxiliem na avaliação de riscos de compliance.

> **Status atual:** o projeto é um esqueleto de aplicação Spring Boot. A estrutura principal e o ambiente de build estão configurados, mas o cadastro de empresas, os endpoints REST e a integração com APIs mocadas ainda não foram implementados.

## Contexto

Empresas sujeitas a normas regulatórias precisam conhecer os clientes com os quais se relacionam. O processo de KYC organiza a coleta e a validação de dados cadastrais para confirmar a identidade de pessoas físicas e jurídicas.

O AML reúne controles destinados a prevenir, detectar e reportar tentativas de utilização de organizações ou transações para ocultar a origem ilícita de recursos. Nesse contexto, uma aplicação de compliance pode auxiliar ao:

- manter uma visão centralizada das empresas cadastradas;
- consultar informações em listas de sanções;
- identificar diretores vinculados a Pessoas Politicamente Expostas (PEPs);
- consolidar sinais de risco em uma avaliação;
- registrar o histórico das verificações realizadas.

Este projeto utiliza APIs simuladas para representar fontes externas. Assim, é possível desenvolver e testar fluxos de compliance sem depender de provedores reais ou expor dados de produção.

## Objetivos do estudo

- Modelar o cadastro de empresas e seus representantes;
- Consumir APIs simuladas de sanções e PEP;
- Aplicar regras de classificação de risco;
- Registrar os resultados das verificações;
- Demonstrar boas práticas de testes unitários e de integração;
- Estudar contratos HTTP, tratamento de falhas e configuração de serviços.

## Conceitos fundamentais

### KYC

**Know Your Customer**, ou **Conheça o seu Cliente**, é o conjunto de processos usados para identificar e verificar clientes antes e durante o relacionamento comercial.

Em uma avaliação de pessoa jurídica, podem ser considerados:

- razão social e nome fantasia;
- documento de identificação empresarial;
- endereço e país;
- atividade econômica;
- sócios e administradores;
- país de origem dos recursos;
- histórico de relacionamento;
- nível de risco associado ao cliente.

O objetivo é entender quem é o cliente, validar os dados informados e monitorar a relação ao longo do tempo.

### AML

**Anti-Money Laundering**, ou **Prevenção à Lavagem de Dinheiro**, envolve regras e procedimentos para impedir que recursos de origem ilícita sejam introduzidos no sistema financeiro ou comercial.

Controles comuns incluem:

- validação de identidade;
- análise de listas de sanções;
- identificação de PEPs;
- monitoramento de transações;
- análise de padrões incompatíveis com o perfil do cliente;
- reporte de atividades suspeitas;
- manutenção de registros para auditoria.

> Este projeto é um recurso educacional e não substitui aconselhamento jurídico, regulatório ou uma solução comercial de compliance.

### PEP

Uma **Pessoa Politicamente Exposta** exerce ou exerceu cargos públicos relevantes e pode estar sujeita a maior risco de conflito de interesse ou corrupção, conforme a legislação aplicável.

No contexto da aplicação, a avaliação pode verificar se administradores ou sócios estão associados a PEPs em uma base simulada. Ser PEP não significa, por si só, que exista prática ilícita; apenas eleva a necessidade de análise e monitoramento.

### Sanções

Listas de sanções relacionam pessoas ou organizações sujeitas a restrições determinadas por governos ou entidades regulatórias. Uma correspondência pode exigir bloqueio, revisão humana ou diligência adicional, dependendo da regra aplicável.

## Fluxo planejado

```text
Cliente ou consumidor da API
        |
        v
API de cadastro/consulta
        |
        v
Serviço de Compliance
        |
        +--> API simulada de sanções
        |
        +--> API simulada de PEP
        |
        v
Consolidação e classificação de risco
        |
        v
Resultado da análise
```

Um fluxo típico deve seguir estas etapas:

1. Cadastrar uma empresa com seus dados básicos.
2. Validar campos obrigatórios e formatar documentos.
3. Consultar a base simulada de sanções pelo identificador da empresa.
4. Consultar administradores e sócios na base simulada de PEP.
5. Consolidar os resultados obtidos pelas fontes externas.
6. Atribuir uma classificação de risco ou uma recomendação de revisão.
7. Persistir o resultado e permitir consultas posteriores.
8. Registrar falhas temporárias sem apresentar uma análise como aprovada automaticamente.

## Riscos e regras possíveis

A implementação pode iniciar com uma regra simples e evoluir para um motor de avaliação mais completo.

| Condição simulada | Exemplo de indicação |
| --- | --- |
| Empresa encontrada em lista de sanções | Risco crítico ou revisão obrigatória |
| Diretor ou sócio identificado como PEP | Risco elevado |
| Dados cadastrais incompletos | Diligência pendente |
| Correspondência parcial em uma fonte externa | Revisão manual |
| Nenhuma ocorrência encontrada | Risco baixo |
| Fonte indisponível ou timeout | Análise inconclusiva |

Essas categorias são apenas exemplos didáticos. Os critérios reais devem ser definidos por especialistas de compliance e pelas regras aplicáveis ao contexto da organização.

## Arquitetura planejada

```text
src/main/java/dio/compliance
├── controller/       Recursos REST
├── domain/           Entidades e objetos de negócio
├── dto/              Contratos de entrada e saída
├── repository/       Persistência de dados
├── service/          Casos de uso e regras de compliance
├── integration/      Clientes das APIs simuladas
└── exception/        Tratamento de erros

src/test/java/dio/compliance
├── unit/             Testes de regras e serviços
└── integration/      Testes dos recursos REST e APIs mocadas
```

A arquitetura não precisa estar limitada a esses pacotes. A estrutura pode evoluir conforme a complexidade da aplicação.

## APIs mocadas

O projeto deve utilizar servidores ou respostas simuladas para representar fontes externas. Os mocks podem ser implementados como respostas controladas pelo próprio Spring Boot ou por uma ferramenta específica.

Exemplos de contratos:

```http
GET /mock/sanctions/{companyId}
```

```json
{
  "companyId": "empresa-001",
  "sanctioned": true,
  "lists": ["Lista Educacional A"],
  "lastUpdated": "2026-11-19"
}
```

```http
GET /mock/pep/{personDocument}
```

```json
{
  "document": "12345678900",
  "name": "Pessoa Exemplo",
  "pep": true,
  "positions": ["Cargo público fictício"],
  "lastUpdated": "2026-11-19"
}
```

Os dados acima são fictícios e existem somente para demonstrar o formato previsto.

## Tecnologias

- Java 25
- Spring Boot 4.1.2
- Gradle Wrapper
- JUnit 5
- Spring Boot Test

## Pré-requisitos

- JDK 25
- Git
- Conexão com a internet na primeira resolução das dependências Gradle

## Como executar

### Linux e macOS

```bash
./gradlew bootRun
```

### Windows

```bat
gradlew.bat bootRun
```

Após a implementação dos recursos web, a aplicação pode ser acessada no endereço configurado pelo Spring Boot, normalmente:

```text
http://localhost:8080
```

No estado atual, o comando inicia o contexto da aplicação, mas não oferece endpoints REST.

## Como testar

Execute todos os testes:

```bash
./gradlew test
```

Gere o relatório HTML dos testes:

```bash
./gradlew test
```

O resultado será disponibilizado em:

```text
build/reports/tests/test/index.html
```

## Boas práticas previstas

- Não persistir dados pessoais reais em exemplos ou testes.
- Utilizar dados claramente fictícios em mocks.
- Separar chamadas externas da lógica de negócio.
- Configurar URLs externas por propriedades da aplicação.
- Definir timeouts e limitar tentativas para evitar lentidão em cascata.
- Tratar indisponibilidade de fonte sem transformar erro técnico em "cliente sem risco".
- Diferenciar ausência de correspondência de erro na consulta.
- Registrar fonte, data da consulta e versão da regra aplicada.
- Testar correspondências exatas, parciais, múltiplas e inexistentes.
- Validar entradas na camada REST antes das consultas.
- Aplicar autenticação e autorização antes de adicionar persistência ou dados sensíveis.
- Não usar o termo PEP como confirmação automática de irregularidade.
- Considerar auditoria, retenção e proteção de dados pessoais desde o desenho inicial.

## Possíveis evoluções

- API REST para criação e consulta de empresas;
- cadastro de sócios e administradores;
- persistência com banco relacional;
- clientes configuráveis para APIs externas;
- mock server para cenários positivos e negativos;
- motor de pontuação de risco;
- histórico versionado das avaliações;
- autenticação e perfis de usuário;
- documentação OpenAPI/Swagger;
- fila para processar grandes volumes de consultas;
- observabilidade com logs estruturados e métricas;
- testes de contrato das integrações;
- contêinerização com Docker.

## Estrutura do projeto

```text
.
├── gradle/wrapper/
├── src/
│   ├── main/
│   │   ├── java/dio/compliance/
│   │   └── resources/application.properties
│   └── test/java/dio/compliance/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
└── README.md
```

## Licença

Este projeto está licenciado nos termos da [MIT License](LICENSE).
 
