package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.BookingStatus
import com.example.data.model.CarReturnBooking
import com.example.data.model.RefuelOption

@Entity(tableName = "car_return_bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookingCode: String,
    val passengerName: String,
    val passengerPhone: String,
    val airportCode: String,
    val terminalName: String,
    val curbsidePillar: String,
    val flightNumber: String,
    val airline: String,
    val scheduledDropTime: String,
    val rentalCompany: String,
    val rentalAgreementNumber: String,
    val vehicleMakeModel: String,
    val vehicleColor: String,
    val licensePlate: String,
    val refuelOptionName: String,
    val isPriorityRush: Boolean,
    val handoffPin: String,
    val statusName: String,
    val assignedAgentName: String,
    val assignedAgentPhone: String,
    val intakeMileage: Int,
    val intakeFuelPercent: Int,
    val intakeConditionNotes: String,
    val returnBayNumber: String,
    val returnRentalAgentName: String,
    val rentalReturnReceiptUrl: String?,
    val baseFee: Double,
    val airportFee: Double,
    val refuelFee: Double,
    val rushFee: Double,
    val createdAt: Long,
    val completedAt: Long?
) {
    fun toDomain(): CarReturnBooking {
        val status = try {
            BookingStatus.valueOf(statusName)
        } catch (_: Exception) {
            BookingStatus.CONFIRMED
        }
        val refuel = try {
            RefuelOption.valueOf(refuelOptionName)
        } catch (_: Exception) {
            RefuelOption.PRE_FILLED
        }

        return CarReturnBooking(
            id = id,
            bookingCode = bookingCode,
            passengerName = passengerName,
            passengerPhone = passengerPhone,
            airportCode = airportCode,
            terminalName = terminalName,
            curbsidePillar = curbsidePillar,
            flightNumber = flightNumber,
            airline = airline,
            scheduledDropTime = scheduledDropTime,
            rentalCompany = rentalCompany,
            rentalAgreementNumber = rentalAgreementNumber,
            vehicleMakeModel = vehicleMakeModel,
            vehicleColor = vehicleColor,
            licensePlate = licensePlate,
            refuelOption = refuel,
            isPriorityRush = isPriorityRush,
            handoffPin = handoffPin,
            status = status,
            assignedAgentName = assignedAgentName,
            assignedAgentPhone = assignedAgentPhone,
            intakeMileage = intakeMileage,
            intakeFuelPercent = intakeFuelPercent,
            intakeConditionNotes = intakeConditionNotes,
            returnBayNumber = returnBayNumber,
            returnRentalAgentName = returnRentalAgentName,
            rentalReturnReceiptUrl = rentalReturnReceiptUrl,
            baseFee = baseFee,
            airportFee = airportFee,
            refuelFee = refuelFee,
            rushFee = rushFee,
            createdAt = createdAt,
            completedAt = completedAt
        )
    }

    companion object {
        fun fromDomain(booking: CarReturnBooking): BookingEntity {
            return BookingEntity(
                id = booking.id,
                bookingCode = booking.bookingCode,
                passengerName = booking.passengerName,
                passengerPhone = booking.passengerPhone,
                airportCode = booking.airportCode,
                terminalName = booking.terminalName,
                curbsidePillar = booking.curbsidePillar,
                flightNumber = booking.flightNumber,
                airline = booking.airline,
                scheduledDropTime = booking.scheduledDropTime,
                rentalCompany = booking.rentalCompany,
                rentalAgreementNumber = booking.rentalAgreementNumber,
                vehicleMakeModel = booking.vehicleMakeModel,
                vehicleColor = booking.vehicleColor,
                licensePlate = booking.licensePlate,
                refuelOptionName = booking.refuelOption.name,
                isPriorityRush = booking.isPriorityRush,
                handoffPin = booking.handoffPin,
                statusName = booking.status.name,
                assignedAgentName = booking.assignedAgentName,
                assignedAgentPhone = booking.assignedAgentPhone,
                intakeMileage = booking.intakeMileage,
                intakeFuelPercent = booking.intakeFuelPercent,
                intakeConditionNotes = booking.intakeConditionNotes,
                returnBayNumber = booking.returnBayNumber,
                returnRentalAgentName = booking.returnRentalAgentName,
                rentalReturnReceiptUrl = booking.rentalReturnReceiptUrl,
                baseFee = booking.baseFee,
                airportFee = booking.airportFee,
                refuelFee = booking.refuelFee,
                rushFee = booking.rushFee,
                createdAt = booking.createdAt,
                completedAt = booking.completedAt
            )
        }
    }
}
