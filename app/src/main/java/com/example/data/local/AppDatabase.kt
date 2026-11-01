package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.BookingStatus
import com.example.data.model.RefuelOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [BookingEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "terminaldrop_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.bookingDao())
                }
            }
        }

        suspend fun populateInitialData(dao: BookingDao) {
            val now = System.currentTimeMillis()
            val sampleBookings = listOf(
                // Live booking at the curb right now
                BookingEntity(
                    id = 1,
                    bookingCode = "TD-9104",
                    passengerName = "David Henderson",
                    passengerPhone = "(415) 890-2134",
                    airportCode = "LAX",
                    terminalName = "Terminal 4",
                    curbsidePillar = "Pillar 4A - American Airlines Upper Curb",
                    flightNumber = "AA 1428",
                    airline = "American Airlines",
                    scheduledDropTime = "In 15 mins (Curbside Ready)",
                    rentalCompany = "Hertz",
                    rentalAgreementNumber = "H-83920194",
                    vehicleMakeModel = "2024 Toyota RAV4 Hybrid",
                    vehicleColor = "Magnetic Gray Metallic",
                    licensePlate = "8XYZ412",
                    refuelOptionName = RefuelOption.VALET_TOP_OFF.name,
                    isPriorityRush = true,
                    handoffPin = "4829",
                    statusName = BookingStatus.AT_CURBSIDE.name,
                    assignedAgentName = "Marcus Cole (Badge #104)",
                    assignedAgentPhone = "(310) 555-0194",
                    intakeMileage = 16840,
                    intakeFuelPercent = 70,
                    intakeConditionNotes = "Clean exterior, small front bumper bug residue. 2 keys present.",
                    returnBayNumber = "",
                    returnRentalAgentName = "",
                    rentalReturnReceiptUrl = null,
                    baseFee = 49.0,
                    airportFee = 9.50,
                    refuelFee = 15.0,
                    rushFee = 12.0,
                    createdAt = now - (25 * 60 * 1000),
                    completedAt = null
                ),
                // Booking en-route to return facility
                BookingEntity(
                    id = 2,
                    bookingCode = "TD-7842",
                    passengerName = "Elena Rostova",
                    passengerPhone = "(206) 432-8819",
                    airportCode = "SFO",
                    terminalName = "Terminal 3",
                    curbsidePillar = "Pillar 3-Door 4 (United)",
                    flightNumber = "UA 412",
                    airline = "United Airlines",
                    scheduledDropTime = "Today, 1:45 PM",
                    rentalCompany = "Enterprise",
                    rentalAgreementNumber = "ENT-55291",
                    vehicleMakeModel = "2024 Ford Mustang Mach-E",
                    vehicleColor = "Grabber Blue",
                    licensePlate = "9ABC771",
                    refuelOptionName = RefuelOption.PRE_FILLED.name,
                    isPriorityRush = false,
                    handoffPin = "7163",
                    statusName = BookingStatus.EN_ROUTE_TO_RETURN.name,
                    assignedAgentName = "Javier Ortiz (Badge #210)",
                    assignedAgentPhone = "(650) 555-0182",
                    intakeMileage = 8240,
                    intakeFuelPercent = 95,
                    intakeConditionNotes = "Pristine EV condition. Charge cable in trunk verified.",
                    returnBayNumber = "Bay 12 - Rapid Return",
                    returnRentalAgentName = "Kevin S.",
                    rentalReturnReceiptUrl = null,
                    baseFee = 49.0,
                    airportFee = 8.00,
                    refuelFee = 0.0,
                    rushFee = 0.0,
                    createdAt = now - (55 * 60 * 1000),
                    completedAt = null
                ),
                // Upcoming booking later today
                BookingEntity(
                    id = 3,
                    bookingCode = "TD-6218",
                    passengerName = "Michael Chang",
                    passengerPhone = "(312) 887-1940",
                    airportCode = "ORD",
                    terminalName = "Terminal 1",
                    curbsidePillar = "Upper Level Door 1E",
                    flightNumber = "UA 2108",
                    airline = "United Airlines",
                    scheduledDropTime = "Today, 5:30 PM",
                    rentalCompany = "Avis",
                    rentalAgreementNumber = "AV-994102",
                    vehicleMakeModel = "2023 Jeep Grand Cherokee",
                    vehicleColor = "Diamond Black",
                    licensePlate = "ILL-40291",
                    refuelOptionName = RefuelOption.PRE_FILLED.name,
                    isPriorityRush = false,
                    handoffPin = "9302",
                    statusName = BookingStatus.CONFIRMED.name,
                    assignedAgentName = "Sarah Jenkins (Badge #118)",
                    assignedAgentPhone = "(773) 555-0145",
                    intakeMileage = 21400,
                    intakeFuelPercent = 100,
                    intakeConditionNotes = "Pre-booking checklist confirmed.",
                    returnBayNumber = "",
                    returnRentalAgentName = "",
                    rentalReturnReceiptUrl = null,
                    baseFee = 52.0,
                    airportFee = 8.50,
                    refuelFee = 0.0,
                    rushFee = 0.0,
                    createdAt = now - (120 * 60 * 1000),
                    completedAt = null
                ),
                // Completed return from earlier with official checkout receipt
                BookingEntity(
                    id = 4,
                    bookingCode = "TD-5049",
                    passengerName = "Sarah Miller",
                    passengerPhone = "(404) 912-3049",
                    airportCode = "ATL",
                    terminalName = "Domestic South Terminal",
                    curbsidePillar = "South Door S-2 (Southwest)",
                    flightNumber = "WN 1802",
                    airline = "Southwest Airlines",
                    scheduledDropTime = "Yesterday, 10:30 AM",
                    rentalCompany = "National",
                    rentalAgreementNumber = "NAT-774019",
                    vehicleMakeModel = "2024 Chevrolet Tahoe LT",
                    vehicleColor = "Summit White",
                    licensePlate = "GA-82199",
                    refuelOptionName = RefuelOption.PRE_FILLED.name,
                    isPriorityRush = false,
                    handoffPin = "5512",
                    statusName = BookingStatus.RETURNED_SUCCESS.name,
                    assignedAgentName = "Marcus Cole (Badge #104)",
                    assignedAgentPhone = "(404) 555-0199",
                    intakeMileage = 12350,
                    intakeFuelPercent = 100,
                    intakeConditionNotes = "No damage detected. Checked in with National Lot Lead.",
                    returnBayNumber = "Emerald Aisle Lane 1, Spot 4",
                    returnRentalAgentName = "Derrick P. (National Lot Supervisor)",
                    rentalReturnReceiptUrl = "NAT-RECEIPT-2026-OK.PDF",
                    baseFee = 47.0,
                    airportFee = 7.50,
                    refuelFee = 0.0,
                    rushFee = 0.0,
                    createdAt = now - (28 * 60 * 60 * 1000),
                    completedAt = now - (27 * 60 * 60 * 1000)
                )
            )
            dao.insertAll(sampleBookings)
        }
    }
}
