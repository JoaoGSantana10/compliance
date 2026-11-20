# Guia detalhado do Mockoon

Este projeto usa o Mockoon para substituir APIs externas reais de sanções e AML/PEP durante o desenvolvimento e os testes.

## Por que usar mocks

- Permite executar o projeto sem credenciais de fornecedores reais.
- Garante respostas previsíveis para cenários de teste.
- Evita envio de dados reais ou pessoais em ambiente local.
- Permite validar fluxo HTTP, fallback, transformação JSON e política de risco.

## Serviços necessários

| Serviço | Porta | Caminho | DTO consumido |
| --- | --- | --- | --- |
| Sanções | `3001` | `/sanctions/companies/:registrationNumber` | `SanctionResult` |
| AML/PEP | `3002` | `/aml/v1/screening/:registrationNumber` | `AmlResult` |

As portas correspondem exatamente às propriedades em `src/main/resources/application.properties`.

## Configuração no Mockoon Desktop

1. Abra o Mockoon.
2. Crie um novo ambiente.
3. Defina a porta `3001`.
4. Adicione uma rota `GET`.
5. Configure o endpoint `/sanctions/companies/:registrationNumber`.
6. Defina o status `200`.
7. Configure o corpo JSON descrito neste documento.
8. Repita o processo em um segundo ambiente na porta `3002`.
9. Inicie ambos os ambientes antes do `bootRun`.

Para usar um único arquivo ou ambiente, mantenha portas separadas para evitar conflito.

## Cenário de sanção crítica

Endpoint:

```text
GET http://localhost:3001/sanctions/companies/empresa-001
```

Resposta:

```json
{
  "matches": [
    {
      "entity": "empresa-001",
      "list": "OFAC Fictícia",
      "reason": "Correspondência usada para teste",
      "confidenceScore": 0.98
    }
  ]
}
```

Resultado esperado pela política:

- status: `REJECTED`;
- nível: `CRITICAL`;
- score: o valor devolvido pela API AML.

## Cenário sem sanções

```json
{
  "matches": []
}
```

Esse resultado é diferente de uma falha da API. Uma falha de sanções pode ativar `SanctionClient.Fallback`, que também entrega uma lista vazia. A implementação atual não distingue origem técnica e ausência de correspondência; essa limitação precisa ser tratada antes de uso produtivo.

## Cenário PEP

Endpoint:

```text
GET http://localhost:3002/aml/v1/screening/empresa-001
```

Resposta:

```json
{
  "riskScore": 75,
  "flags": [
    "PEP encontrada"
  ],
  "pep": {
    "isPep": true,
    "occurrences": [
      {
        "personName": "Pessoa Exemplo",
        "position": "Cargo Público Fictício"
      }
    ]
  }
}
```

Resultado esperado:

- status: `MANUAL_REVIEW`;
- nível: `HIGH`, porque o score é maior que `70`.

## Cenário baixo risco

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

Resultado esperado:

- status: `APROVED`;
- nível: `LOW`.

## Como o valor do caminho chega ao mock

No Mockoon, use a sintaxe específica do produto para dados dinâmicos. Por exemplo:

```json
{
  "entity": "{{urlParam 'registrationNumber'}}"
}
```

A aplicação envia o parâmetro preenchido pelo Feign a partir de `Company.registrationNumber`.

## Cabeçalhos

A configuração atual envia:

- `x-api-key: kyc-secret-123` na chamada de sanções;
- `Authorization: ******` na chamada AML, conforme o valor definido no `application.properties`.

Substitua esse valor por uma configuração segura em ambiente diferente. Não use credenciais reais em testes versionados.

## Testes rápidos

Com os dois servidores iniciados e a aplicação em execução:

```bash
curl -i http://localhost:3001/sanctions/companies/empresa-001
curl -i http://localhost:3002/aml/v1/screening/empresa-001

curl -X POST http://localhost:8080/companies \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Empresa Fictícia",
    "registrationNumber": "empresa-001"
  }'

curl http://localhost:8080/companies
```

## Limites do uso atual

- O Mockoon não substitui validação de um fornecedor real.
- A configuração não fornece Docker Compose nem inicialização automática.
- O fallback de sanções pode mascarar indisponibilidade.
- O cliente AML não possui fallback.
- Os payloads são estruturados para esta implementação e não possuem contrato formal versionado.
