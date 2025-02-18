# 📌 Desafio picpay simplificado
<hr>

- Descrição completa do projeto [aqui](https://github.com/PicPay/picpay-desafio-backend)
- Diagramas do projeto [aqui](/design/Diagramas.md)

# Descrição
<hr>

Basicamente o desafio proposto é desenvolver o fluxo de uma aplicação que faz transações entre carteiras, existem 2 tipos de carteiras, a comum e a dos lojistas, sendo que a dos lojistas só é possível receber transações.

- **Atenção:** O sistema ja inicia com 3 carteiras, a carteira 1 do tipo comum, a carteira 2 do tipo lojista e a carteira 3 do tipo comum.

- O sistema possui um endpoint:
``POST /v1/transfer``
- Que aceita apenas Json neste formato:

```
POST /v1/transfer
Content-Type: application/json

{
  "value": 100.0,
  "payer": 1,
  "payee": 3
}
```
- Sendo value o valor da transação, payer sendo o id da carteira do pagador da transação, e payee sendo o id da carteira do recebedor da transação.

# 📦 Pré-requisitos
<hr>

- Ter docker instalado na máquina

# 🚀 Passo a passo para rodar o projeto
<hr>

1️⃣ Clonar o repositório

`git clone https://github.com/ValentePG/desafiopicpaysimplificado.git`

`cd desafiopicpaysimplificado`

2️⃣ Instalar dependências

`./mvnw clean install # Linux`

`mvnw.cmd clean install # Windows`

3️⃣ Rodar a aplicação

- **Primeira maneira**
  - Você pode rodar a aplicação diretamente sem docker, porém você ainda vai precisar da imagem do PostgreSQL e RabbitMQ ou ambos estarem instalados na máquina rodando na mesma porta 
  - Alterar o arquivo `application-dev.yml` 
  - Alterar a url do postgreSQL para localhost `jdbc:postgresql://localhost:5432/picpaysimplificado?createDatabaseIfNotExist=true`
  - Alterar o campo `host` do rabbitmq para `localhost`
  - Após isto você pode executar o programa normalmente com:

  - `./mvnw spring-boot:run # Linux`

  - `mvnw.cmd spring-boot:run # Windows`


- **Segunda maneira**
    - Com o docker instalado apenas rode o comando abaixo:
    - `docker compose -f compose-dev.yml up`

4️⃣ Acessando a API

- API:`http://localhost:8080/transfer`
- Swagger:`http://localhost:8080/swagger-ui.html`

5️⃣ Rodar os testes (opcional)

**Lembrando que os testes de integração fazem uso da tecnologia testContainers, que utiliza docker, então certifique-se de estar com o docker engine em execução.**

- **Para rodar os testes unitários**

`./mvnw test`

- **Para rodar os testes de integração**

`./mvnw verify -P integration-test`

- **Para rodar os 2 juntos**

`./mvnw test; ./mvnw verify -P integration-test # Para windows`

`./mvnw test && ./mvnw verify -P integration-test # Para linux` 


# 🛠️ Tecnologias Utilizadas
<hr>

- Java 21

- Spring Boot 3.4.2

- Docker

- RabbitMQ

- PostgreSQL


# 📄 Licença

<hr>
Este projeto está sob a licença MIT - veja o arquivo LICENSE para mais detalhes.
