package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM car_return_bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM car_return_bookings WHERE statusName != 'RETURNED_SUCCESS' AND statusName != 'CANCELLED' ORDER BY createdAt DESC")
    fun getActiveBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM car_return_bookings WHERE statusName = 'RETURNED_SUCCESS' OR statusName = 'CANCELLED' ORDER BY completedAt DESC, createdAt DESC")
    fun getPastBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM car_return_bookings WHERE id = :id LIMIT 1")
    fun getBookingById(id: Long): Flow<BookingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bookings: List<BookingEntity>)

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Delete
    suspend fun deleteBooking(booking: BookingEntity)

    @Query("SELECT COUNT(*) FROM car_return_bookings")
    suspend fun getCount(): Int
}
