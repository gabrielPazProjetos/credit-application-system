package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.dto.request.CreditDto
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.repository.CustomerRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CreditResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    companion object {
        const val URL: String = "/api/credits"
    }

    @BeforeEach
    fun setup() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @Test
    fun `should create credit and return 201 status`() {
        val customer = customerRepository.save(builderCustomerDto().toEntity())
        val creditDto = builderCreditDto(customerId = customer.id!!)
        val valueAsString = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value("1000.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value("5"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find all credits by customer id and return 200 status`() {
        val customer = customerRepository.save(builderCustomerDto().toEntity())
        val credit1 = creditRepository.save(builderCreditDto(customerId = customer.id!!).toEntity())
        val credit2 = creditRepository.save(builderCreditDto(customerId = customer.id!!).copy(creditValue = BigDecimal(2000)).toEntity())

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL?customerId=${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find credit by credit code and customer id and return 200 status`() {
        val customer = customerRepository.save(builderCustomerDto().toEntity())
        val credit = creditRepository.save(builderCreditDto(customerId = customer.id!!).toEntity())

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${credit.creditCode}?customerId=${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").value(credit.creditCode.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value("1000.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value("5"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find credit with invalid credit code and return 400 status`() {
        val customer = customerRepository.save(builderCustomerDto().toEntity())
        val invalidCode = UUID.randomUUID()

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/$invalidCode?customerId=${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class me.dio.credit.application.system.exception.BusinessException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    private fun builderCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(1000.0),
        dayFirstInstallment: LocalDate = LocalDate.now().plusDays(30),
        numberOfInstallments: Int = 5,
        customerId: Long = 1L
    ): CreditDto {
        return CreditDto(
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallments,
            customerId = customerId
        )
    }
}
