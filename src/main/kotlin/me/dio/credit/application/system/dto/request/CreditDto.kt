package me.dio.credit.application.system.dto.request

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:NotNull(message = "Invalid input")
    val creditValue: BigDecimal,

    @field:Future(message = "Installment date must be in the future")
    val dayFirstInstallment: LocalDate,

    @field:Min(value = 1, message = "Minimum 1 installment")
    @field:Max(value = 48, message = "Maximum 48 installments")
    val numberOfInstallments: Int,

    @field:NotNull(message = "Invalid input")
    val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )
}
