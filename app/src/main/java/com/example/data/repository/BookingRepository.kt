package com.example.data.repository

import com.example.data.local.BookingDao
import com.example.data.local.BookingEntity
import com.example.data.model.BookingStatus
import com.example.data.model.BusinessOperatorStats
import com.example.data.model.CarReturnBooking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookingRepository(private val bookingDao: BookingDao) {

    val allBookings: Flow<List<CarReturnBooking>> = bookingDao.getAllBookings().map { list ->
        list.map { it.toDomain() }
    }

    val activeBookings: Flow<List<CarReturnBooking>> = bookingDao.getActiveBookings().map { list ->
        list.map { it.toDomain() }
    }

    val pastBookings: Flow<List<CarReturnBooking>> = bookingDao.getPastBookings().map { list ->
        list.map { it.toDomain() }
    }

    fun getBookingById(id: Long): Flow<CarReturnBooking?> {
        return bookingDao.getBookingById(id).map { it?.toDomain() }
    }

    suspend fun createBooking(booking: CarReturnBooking): Long {
        val entity = BookingEntity.fromDomain(booking)
        return bookingDao.insertBooking(entity)
    }

    suspend fun updateBooking(booking: CarReturnBooking) {
        val entity = BookingEntity.fromDomain(booking)
        bookingDao.updateBooking(entity)
    }

    suspend fun deleteBooking(booking: CarReturnBooking) {
        val entity = BookingEntity.fromDomain(booking)
        bookingDao.deleteBooking(entity)
    }

    suspend fun advanceStatus(
        currentBooking: CarReturnBooking,
        newStatus: BookingStatus,
        intakeMileage: Int? = null,
        intakeFuel: Int? = null,
        conditionNotes: String? = null,
        bayNumber: String? = null,
        lotAgentName: String? = null
    ) {
        val updated = currentBooking.copy(
            status = newStatus,
            intakeMileage = intakeMileage ?: currentBooking.intakeMileage,
            intakeFuelPercent = intakeFuel ?: currentBooking.intakeFuelPercent,
            intakeConditionNotes = conditionNotes ?: currentBooking.intakeConditionNotes,
            returnBayNumber = bayNumber ?: currentBooking.returnBayNumber,
            returnRentalAgentName = lotAgentName ?: currentBooking.returnRentalAgentName,
            completedAt = if (newStatus == BookingStatus.RETURNED_SUCCESS) System.currentTimeMillis() else currentBooking.completedAt,
            rentalReturnReceiptUrl = if (newStatus == BookingStatus.RETURNED_SUCCESS && currentBooking.rentalReturnReceiptUrl == null) {
                "${currentBooking.rentalCompany.uppercase()}-RECEIPT-${System.currentTimeMillis() % 100000}.PDF"
            } else currentBooking.rentalReturnReceiptUrl
        )
        updateBooking(updated)
    }

    fun computeOperatorStats(bookings: List<CarReturnBooking>): BusinessOperatorStats {
        val active = bookings.filter { it.status.isActive() }
        val completed = bookings.filter { it.status == BookingStatus.RETURNED_SUCCESS }
        val gross = bookings.sumOf { it.totalFee }
        return BusinessOperatorStats(
            activePickupsCount = active.size,
            completedTodayCount = completed.size,
            totalGrossRevenue = gross,
            averageTurnaroundMinutes = 21,
            onDutyDriversCount = 4
        )
    }
}
