# Compliance Shield

Aplicação educacional em Java e Spring Boot para estudo de **KYC** (Know Your Customer), **AML** (Anti-Money Laundering), integração HTTP e regras de análise de risco.

O sistema recebe o cadastro de empresas por uma API REST gerada pelo Spring Data REST, consulta fontes externas simuladas no **Mockoon** e consolida os resultados em uma avaliação armazenada em memória.

> **Este projeto não é um mecanismo de compliance para uso regulatório ou operacional.** Os dados, regras e respostas Mockoon são fictícios e existem apenas para demonstração técnica e educacional.

## O que a aplicação faz

- Expõe o recurso REST `companies`.
- Recebe uma empresa com nome e número de registro.
- Após a criação, executa automaticamente uma análise de risco.
- Consulta uma API simulada de sanções na porta `3001`.
- Consulta uma API simulada de AML/PEP na porta `3002`.
- Converte os JSONs externos em objetos de domínio.
- Aplica uma política de risco.
- Atualiza a empresa com score, nível e status.
- Mantém as entidades em um repositório Spring Data Map, ou seja, em memória.

## Tecnologias utilizadas

| Tecnologia | Uso no projeto |
| --- | --- |
| Java 25 | Linguagem principal |
| Spring Boot 4.1.2 | Inicialização da aplicação e infraestrutura web |
| Spring Data REST | Geração do recurso `GET/POST /companies` |
| Spring Data KeyValue/Map | Persistência em memória |
| OpenFeign | Clientes das APIs Mockoon |
| Resilience4j/Circuit Breaker | Resiliência configurada para integrações |
| Lombok | Redução de código repetitivo nas entidades |
| Gradle Wrapper | Compilação e execução |
| JUnit 5 | Estrutura de testes |
| Mockoon | Mock das APIs externas de sanções e AML/PEP |

## Arquitetura

```text
Cliente HTTP
    |
    v
POST /companies
    |
    v
Spring Data REST + CompanyEntityRepository
    |
    v
CompanyEventHandler (@HandleAfterCreate)
    |
    v
AnalyzeCompanyRiskUseCase
    |
    +--> SanctionClient ----------> Mockoon :3001
    |
    +--> AntiMoneyLauderingClient -> Mockoon :3002
    |
    v
ComplianceScreening
    |
    v
CompliancePolicy
    |
    v
RiskAssessment
    |
    v
InMemoryCompanyRepository -> armazenamento em memória
```

### Responsabilidade dos pacotes

| Pacote | Responsabilidade |
| --- | --- |
| `dio.compliance` | Inicialização Spring Boot e ativação de Feign/Map repositories |
| `dio.compliance.application` | Casos de uso que coordenam o fluxo de análise |
| `dio.compliance.domain` | Entidades, contratos e regras de negócio |
| `dio.compliance.infraestructure.persistence.entity` | Entidade persistível e conversões de domínio |
| `dio.compliance.infraestructure.persistence.event` | Disparo da análise após a criação da empresa |
| `dio.compliance.infraestructure.persistence.repository` | Repositório Spring Data e adaptador em memória |
| `dio.compliance.infraestructure.rest.client` | Contratos HTTP Feign |
| `dio.compliance.infraestructure.rest.dto` | Payloads JSON das fontes externas |

Cada pacote possui `package-info.java` com uma explicação geral. Todas as classes públicas, registros, interfaces, métodos e principais tipos de retorno também possuem Javadoc.

## Modelos de domínio

### Company

Representa uma empresa com:

- `id`: UUID de domínio;
- `name`: razão social ou nome da empresa;
- `registrationNumber`: identificador usado nas consultas externas;
- `riskAssessment`: avaliação opcional aplicada após a triagem.

### ComplianceScreening

Reúne a resposta normalizada das fontes:

- `sanctions`: correspondências em sanções;
- `amlProfile`: score AML, flags, presença de PEP e exposições políticas.

### RiskAssessment

Contém o resultado final:

- `score`: pontuação AML usada no cálculo;
- `level`: `LOW`, `MEDIUM`, `HIGH` ou `CRITICAL`;
- `status`: `APROVED`, `REJECTED` ou `MANUAL_REVIEW`.

O nome `APROVED` é mantido no código atual.

## Regras de risco atuais

A implementação atual está em `CompliancePolicy`:

1. Uma correspondência de sanção com `confidence > 0.8` gera `REJECTED`.
2. Se não houver rejeição e houver PEP, gera `MANUAL_REVIEW`.
3. Se não houver rejeição nem PEP e o score AML for maior que `70`, gera `MANUAL_REVIEW`.
4. Caso contrário, gera `APROVED`.
5. Status `REJECTED` gera nível `CRITICAL`.
6. Scores maiores que `70` geram nível `HIGH`.
7. Scores maiores que `30` geram nível `MEDIUM`.
8. Os demais scores geram nível `LOW`.

Estes valores são regras didáticas. Não representam recomendação jurídica ou metodologia regulatória real.

## Utilização do Mockoon

O projeto utiliza o **Mockoon** para simular as duas APIs externas sem dependências reais. As URLs são configuradas em `src/main/resources/application.properties`:

```properties
spring.cloud.openfeign.client.config.sanction-client.url=http://localhost:3001
spring.cloud.openfeign.client.config.aml-client.url=http://localhost:3002
```

### Ambiente recomendado

Crie um ambiente Mockoon com dois servidores separados ou uma única definição com duas interfaces. Configure:

| Serviço | Porta | Método e rota |
| --- | --- | --- |
| Sanções | `3001` | `GET /sanctions/companies/{registrationNumber}` |
| AML/PEP | `3002` | `GET /aml/v1/screening/{registrationNumber}` |

Inicie os ambientes Mockoon antes de criar uma empresa na aplicação.

### Rota de sanções

Na porta `3001`, crie uma rota `GET` com o caminho:

```text
/sanctions/companies/:registrationNumber
```

Exemplo de resposta `200`:

```json
{
  "matches": [
    {
      "entity": "{{urlParam 'registrationNumber'}}",
      "list": "Lista Educacional de Sanções",
      "reason": "Correspondência fictícia utilizada em teste",
      "confidenceScore": 0.98
    }
  ]
}
```

### Rota AML

Na porta `3002`, crie uma rota `GET` com o caminho:

```text
/aml/v1/screening/:registrationNumber
```

Exemplo de resposta `200`:

```json
{
  "riskScore": 85,
  "flags": [
    "Origem geográfica incomum"
  ],
  "pep": {
    "isPep": true,
    "occurrences": [
      {
        "personName": "Pessoa Fictícia",
        "position": "Cargo público fictício"
      }
    ]
  }
}
```

Os cabeçalhos configurados no cliente:

- Sanções: `x-api-key: kyc-secret-123`;
- AML: cabeçalho `Authorization` definido pela propriedade de aplicação.

No ambiente real, substitua os valores fictícios por gerenciamento seguro de credenciais. Não coloque credenciais reais no repositório.

Para cenários sem correspondências, configure:

```json
{
  "matches": []
}
```

Para o caso sem PEP:

```json
{
  "riskScore": 20,
  "flags": [],
  "pep": {
    "isPep": false,
    "occurrences": []
  }
}
```

Mais detalhes estão em [`docs/MOCKOON.md`](docs/MOCKOON.md).

## Pré-requisitos

- JDK 25;
- Git;
- Mockoon Desktop ou Mockoon CLI;
- acesso local às portas `3001` e `3002`.

## Como executar

### 1. Inicie os ambientes Mockoon

Configure e execute as duas APIs simuladas nas portas descritas acima.

### 2. Inicie a aplicação

Linux/macOS:

```bash
./gradlew bootRun
```

Windows:

```bat
gradlew.bat bootRun
```

A aplicação será iniciada em `http://localhost:8080`.

### 3. Confira o recurso de empresas

```bash
curl http://localhost:8080/companies
```

### 4. Cadastre uma empresa

```bash
curl -X POST http://localhost:8080/companies \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Empresa Fictícia",
    "registrationNumber": "empresa-001"
  }'
```

A resposta inicial pode não conter ainda a avaliação: o evento `@HandleAfterCreate` é executado após a criação e chama o caso de uso de forma síncrona. Consulte o mesmo recurso novamente:

```bash
curl http://localhost:8080/companies
```

No Spring Data REST, o identificador da entidade é o UUID. A consulta pelo JSONLink ou pelo identificador depende da representação HAL configurada pelo Spring.

## Como testar

Execute todos os testes:

```bash
./gradlew test
```

Relatório:

```text
build/reports/tests/test/index.html
```

## Estrutura dos arquivos

```text
.
├── docs/
│   └── MOCKOON.md
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

## Limitações atuais

- Os dados são armazenados apenas em memória e desaparecem ao reiniciar.
- Não há controlador REST próprio; o recurso é fornecido pelo Spring Data REST.
- Não há validação explícita de campos na entidade.
- O fallback de sanções retorna uma lista vazia em falha.
- O cliente AML não possui fallback configurado.
- Não há persistência em banco relacional.
- Não há autenticação ou autorização.
- A API Mockoon deve ser iniciada manualmente.
- Os dados não devem ser usados como evidência ou decisão regulatória.

## Próximos passos sugeridos

- Validar entrada com Bean Validation.
- Criar testes unitários da política e dos conversores.
- Criar testes de contrato para as respostas Mockoon.
- Diferenciar fallback técnico de ausência de correspondência.
- Adicionar fallback ao cliente AML.
- Persistir em PostgreSQL ou outro banco relacional.
- Configurar timeouts e retry com política clara.
- Incluir OpenAPI/Swagger.
- Criar Docker Compose com aplicação e Mockoon.
- Adicionar logs estruturados e observabilidade.

## Conceitos fundamentais

### KYC

KYC significa **Know Your Customer**. Reúne processos para confirmar identidade, dados cadastrais, representantes e risco antes e durante o relacionamento.

### AML

AML significa **Anti-Money Laundering**. Reúne controles para prevenir uso de empresas ou transações na ocultação ou movimentação de recursos ilícitos.

### PEP

PEP significa **Pessoa Politicamente Exposta**. A identificação eleva a necessidade de diligência, mas não prova por si só irregularidade.

### Sanções

São restrições associadas a pessoas ou organizações por governos ou entidades regulatórias. Uma correspondência pode exigir revisão humana, bloqueio ou diligência adicional conforme a regra aplicável.

## Licença

Este projeto está licenciado nos termos da [MIT License](LICENSE).
