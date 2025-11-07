-- Sistema de Crédito - API REST com Spring Boot
Este projeto é uma API REST desenvolvida com Spring Boot para simular operações de crédito e cadastro de clientes. Ele foi baseado em um fork inicial e amplamente ajustado e testado para garantir funcionalidade
completa.

--- Tecnologias utilizadas
Kotlin
Spring Boot
Spring Data JPA
H2 Database (ambiente de teste)
Swagger (documentação da API)
JUnit 5 + MockMvc (testes)
Maven ou Gradle

--- Endpoints disponíveis
- Customer
Método	Endpoint	Descrição
POST	/api/customers	Cria um novo cliente
GET	/api/customers/{id}	Busca cliente por ID
DELETE	/api/customers/{id}	Deleta cliente por ID
PATCH	/api/customers	Atualiza dados do cliente

- Credit
Método	Endpoint	Descrição
POST	/api/credits	Cria uma nova solicitação de crédito
GET	/api/credits?customerId={id}	Lista créditos de um cliente
GET	/api/credits/{creditCode}?customerId={id}	Busca crédito específico por código e cliente

--- Testes implementados
Os testes foram criados para validar os principais fluxos da API:

- CustomerResourceTest.kt
Testa criação, busca, atualização e exclusão de clientes
Usa o método auxiliar builderCustomerDto() para gerar dados de teste

- CreditResourceTest.kt
Testa criação de crédito, listagem por cliente e busca por código
Usa os métodos auxiliares:
builderCustomerDto() → para criar clientes
builderCreditDto() → para criar créditos

Tudo de acordo com o que foi pedido para o desafio DIO
