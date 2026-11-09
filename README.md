# exportNfsMei

API que extrai as notas fiscais emitidas no portal do Emissor Nacional de NFS-e e devolve tudo em JSON, paginando o histórico até o fim.

> Projeto de estudo (novembro/2024) nascido de uma dor real: o portal da NFS-e não oferece exportação das notas emitidas, então a alternativa foi raspar o HTML com Jsoup reaproveitando os cookies de uma sessão já autenticada.

## Stack

- Java 17
- Spring Boot 3.3.5 (Web)
- Jsoup 1.15.3 (parsing de HTML)
- Lombok

## Como funciona

O serviço recebe os cookies de uma sessão autenticada no portal, monta as requisições para `https://www.nfse.gov.br/EmissorNacional/Notas/Emitidas?pg=N` e percorre a paginação enquanto houver página seguinte, lendo cada linha da tabela de notas.

A situação de cada nota é inferida pelo ícone exibido na coluna: se o `src` da imagem contém `cancelada`, a nota é marcada como `canceled`; caso contrário, `approved`.

## Endpoint

| Método | Rota         | Descrição                                     |
|--------|--------------|-----------------------------------------------|
| `POST` | `/exportNfs` | Devolve as notas emitidas da sessão informada |

Request:

```json
{
  "arraffinity": "...",
  "sessionId": "...",
  "emissor": "...",
  "requestVerificationToken": "..."
}
```

Response (`200 OK`):

```json
[
  {
    "date": "01/11/2024",
    "issuedTo": "Empresa Exemplo LTDA",
    "city": "São Paulo",
    "value": "1.500,00",
    "situation": "approved",
    "municipality": "...",
    "tax": "..."
  }
]
```

Se nenhuma nota for encontrada, a resposta é `400 Bad Request` com a mensagem `Notas Fiscais não encontradas por gentileza atualize o cookies` — na prática, o sintoma de cookies expirados.

## Como obter os cookies

1. Faça login em <https://www.nfse.gov.br/EmissorNacional>.
2. Abra o DevTools, aba **Application** (ou **Armazenamento**), seção **Cookies**.
3. Copie os valores de `ARRAffinity`, o cookie de sessão, o cookie do emissor e o `__RequestVerificationToken`.

## Como rodar

```bash
./mvnw spring-boot:run
```

## Limitações

Depende do HTML do portal: qualquer mudança nos seletores (`table.table-striped`, `ul.pagination`) quebra a extração. Os cookies de sessão expiram rápido, então é necessário renová-los a cada uso. Não há autenticação automatizada, por desenho, para não lidar com certificado digital nem armazenar credenciais.
