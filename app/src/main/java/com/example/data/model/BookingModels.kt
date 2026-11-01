package com.example.data.model

enum class BookingStatus(
    val title: String,
    val stepIndex: Int,
    val description: String
) {
    CONFIRMED(
        title = "Assigned",
        stepIndex = 0,
        description = "Your independent valet concierge has been assigned and is monitoring your departure time."
    ),
    VALET_DISPATCHED(
        title = "Concierge En Route",
        stepIndex = 1,
        description = "Your concierge is en route to your departure terminal curbside pillar."
    ),
    AT_CURBSIDE(
        title = "Standing by at Curb",
        stepIndex = 2,
        description = "Concierge is standing by at your designated terminal pillar for immediate 60-second handoff."
    ),
    INSPECTING_HANDOFF(
        title = "Curbside Handoff",
        stepIndex = 3,
        description = "60-second walkaround check, bailment PIN verification, mileage/fuel log, and key transfer."
    ),
    EN_ROUTE_TO_RETURN(
        title = "Vehicle Picked Up",
        stepIndex = 4,
        description = "Vehicle picked up and insured in transit to the designated rental return facility."
    ),
    RETURNED_SUCCESS(
        title = "Returned",
        stepIndex = 5,
        description = "Vehicle checked in at the rental return facility! Official return receipt generated."
    ),
    CANCELLED(
        title = "Cancelled",
        stepIndex = -1,
        description = "This booking was cancelled."
    );

    fun isTerminalPhase(): Boolean = this == AT_CURBSIDE || this == INSPECTING_HANDOFF
    fun isActive(): Boolean = this != RETURNED_SUCCESS && this != CANCELLED
}

enum class RefuelOption(val label: String, val fee: Double, val detail: String) {
    PRE_FILLED(
        label = "Full Tank (Self-Filled)",
        fee = 0.0,
        detail = "You have already filled the tank. No extra refuel charge."
    ),
    VALET_TOP_OFF(
        label = "Courier Fuel Fill Assist (Save $)",
        fee = 15.0,
        detail = "We refuel at a local station at pump price (~$4.45/gal) + $15 service fee so you avoid $11+/gal agency refuel rates."
    ),
    AGENCY_CHARGE(
        label = "Return As-Is (Agency Billed)",
        fee = 0.0,
        detail = "Return with current fuel level and let the rental company bill their standard rate."
    )
}

data class CarReturnBooking(
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
    val refuelOption: RefuelOption,
    val isPriorityRush: Boolean = false,
    val handoffPin: String,
    val status: BookingStatus = BookingStatus.CONFIRMED,
    val assignedAgentName: String = "Marcus Cole (Badge #104)",
    val assignedAgentPhone: String = "(310) 555-0194",
    val intakeMileage: Int = 14250,
    val intakeFuelPercent: Int = 100,
    val intakeConditionNotes: String = "Clean vehicle, no new body damage, 2 key fobs.",
    val returnBayNumber: String = "",
    val returnRentalAgentName: String = "",
    val rentalReturnReceiptUrl: String? = null,
    val baseFee: Double = 49.0,
    val airportFee: Double = 8.50,
    val refuelFee: Double = 0.0,
    val rushFee: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
) {
    val totalFee: Double get() = baseFee + airportFee + refuelFee + rushFee
}

data class BusinessOperatorStats(
    val activePickupsCount: Int,
    val completedTodayCount: Int,
    val totalGrossRevenue: Double,
    val averageTurnaroundMinutes: Int,
    val onDutyDriversCount: Int
)
