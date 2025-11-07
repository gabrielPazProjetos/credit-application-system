package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.dto.request.CustomerDto
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.repository.CustomerRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal

@WebMvcTest(CustomerResource::class)
class CustomerResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var customerRepository: CustomerRepository

    @Test
    fun `should create customer and return 201`() {
        val customerDto = CustomerDto(
            firstName = "Camila",
            lastName = "Cavalcante",
            cpf = "28475934625",
            email = "camila@gmail.com",
            password = "12345",
            income = BigDecimal.valueOf(1000.0),
            zipCode = "12345",
            street = "Rua da Cami"
        )

        val json = objectMapper.writeValueAsString(customerDto)

        mockMvc.perform(
            post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isCreated)
    }

    @Test
    fun `should return 400 when creating customer with invalid email`() {
        val customerDto = CustomerDto(
            firstName = "Camila",
            lastName = "Cavalcante",
            cpf = "28475934625",
            email = "email-invalido",
            password = "12345",
            income = BigDecimal.valueOf(1000.0),
            zipCode = "12345",
            street = "Rua da Cami"
        )

        val json = objectMapper.writeValueAsString(customerDto)

        mockMvc.perform(
            post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest)
    }
}
