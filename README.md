# 📌 Desafio picpay simplificado

- Descrição completa do projeto [aqui](https://github.com/PicPay/picpay-desafio-backend)
- Diagramas do projeto [aqui](/design/Diagramas.md)
  
Basicamente o desafio proposto é desenvolver o fluxo de uma aplicação que faz transações entre carteiras, existem 2 tipos de carteiras, a comum e a dos lojistas, sendo que a dos lojistas só é possível receber transações.

- **Atenção:** O sistema ja inicia com 3 carteiras, a carteira 1 do tipo comum, a carteira 2 do tipo lojista e a carteira 3 do tipo comum.

- O sistema possui um endpoint:

      POST /v1/transfer
  
- Que aceita apenas Json neste formato:

      POST /v1/transfer
      Content-Type: application/json
      
      {
        "value": 100.0,
        "payer": 1,
        "payee": 3
      }
  
- Sendo value o valor da transação, payer sendo o id da carteira do pagador da transação, e payee sendo o id da carteira do recebedor da transação.

# 📦 Pré-requisitos

Antes de começar, certifique-se de ter os seguintes requisitos instalados na sua máquina:


- [Docker](https://www.docker.com/get-started) (versão recomendada: `27.5.1`)
  
- (Opcional) [Java 21](https://www.oracle.com/java/technologies/downloads/#java21) e [Maven 3.9.9](https://maven.apache.org/download.cgi) para rodar sem Docker
  
- (Opcional) PostgreSQL 16
  
- (Opcional) RabbitMQ 4
  
# 🚀 Passo a passo para rodar o projeto

1️⃣ Clonar o repositório

    git clone https://github.com/ValentePG/desafiopicpaysimplificado.git
    cd desafiopicpaysimplificado
    
2️⃣ Instalar dependências

- Linux/MacOs

      ./mvnw clean install
    
- Windows
    
      mvnw.cmd clean install
  
💡 Caso tenha Maven instalado globalmente, você pode rodar:

     mvn clean install
     
3️⃣ Rodar a aplicação

Você pode rodar o projeto de duas formas:

- Método 1: Rodando sem Docker (Configuração Manual)
  - Se optar por rodar sem Docker, você precisa ter um banco de dados `PostgreSQL` e uma instância do `RabbitMQ` rodando localmente.
  - Além disso, será necessário editar a configuração no arquivo `application-dev.yml` para apontar para o banco de dados local:
  
        spring:
          datasource:
            url: jdbc:postgresql://localhost:5432/picpaysimplificado?createDatabaseIfNotExist=true
    
- Alterar o host do RabbitMQ para localhost:

  
      rabbitmq:
        host: localhost
  
- Executar o projeto:
  - Linux/MacOs
  
        ./mvnw spring-boot:run
    
  - Windows
  
        mvnw.cmd spring-boot:run

- Método 2: Rodando com Docker (Recomendado)
Caso tenha o Docker instalado, basta rodar um único comando, e tudo será iniciado automaticamente:

      docker compose -f compose-dev.yml up

4️⃣ Acessando a API

Após iniciar o projeto, os endpoints da API estarão disponíveis nos seguintes links:

- API:

      http://localhost:8080/transfer

- Swagger:

      http://localhost:8080/swagger-ui.html

5️⃣ Rodar os testes (opcional)

⚠️ IMPORTANTE: Os testes de integração utilizam Testcontainers, então é necessário que o Docker esteja rodando antes de executá-los.

- **Para rodar os testes unitários**

      ./mvnw test

- **Para rodar os testes de integração**

      ./mvnw verify -P integration-test

- **Para rodar ambos**
  - Linux/MacOS

        ./mvnw test && ./mvnw verify -P integration-test

  - Windows

        ./mvnw test; ./mvnw verify -P integration-test

# 🛠️ Tecnologias Utilizadas

- Java 21

- Spring Boot 3.4.2

- Docker 27.5.1

- RabbitMQ (imagem: rabbitmq:4-management)

- PostgreSQL (imagem: postgres:16)


# 📄 Licença
Este projeto está sob a licença MIT - veja o arquivo LICENSE para mais detalhes.
