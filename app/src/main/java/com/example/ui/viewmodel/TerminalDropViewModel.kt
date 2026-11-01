package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AirportData
import com.example.data.model.BookingStatus
import com.example.data.model.BusinessOperatorStats
import com.example.data.model.CarReturnBooking
import com.example.data.model.RefuelOption
import com.example.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class AppRole {
    TRAVELER,
    BUSINESS_OPERATOR
}

enum class TravelerTab {
    HOME,
    ACTIVE_RADAR,
    MY_DROPS,
    AIRPORT_GUIDE
}

enum class OperatorTab {
    QUEUE,
    ANALYTICS,
    RATES
}

data class BookingDraft(
    val passengerName: String = "John Davis",
    val passengerPhone: String = "(415) 555-0129",
    val airportCode: String = "LAX",
    val terminalName: String = "Terminal 4",
    val curbsidePillar: String = "Pillar 4A - Upper Departures",
    val flightNumber: String = "AA 2294",
    val airline: String = "American Airlines",
    val scheduledDropTime: String = "In 25 mins",
    val rentalCompany: String = "Hertz",
    val rentalAgreementNumber: String = "H-492019",
    val vehicleMakeModel: String = "2024 Ford Explorer",
    val vehicleColor: String = "Oxford White",
    val licensePlate: String = "8TRK992",
    val refuelOption: RefuelOption = RefuelOption.VALET_TOP_OFF,
    val isPriorityRush: Boolean = false,
    val intakeNotes: String = "Front bumper clean, interior immaculate."
) {
    val airportInfo get() = AirportData.findAirport(airportCode)
    val baseFee get() = airportInfo.baseValetFee
    val airportFee get() = airportInfo.airportSurcharge
    val refuelFee get() = refuelOption.fee
    val rushFee get() = if (isPriorityRush) 12.0 else 0.0
    val totalEstimatedFee get() = baseFee + airportFee + refuelFee + rushFee
}

data class PricingSettings(
    val baseValetFee: Double = 49.0,
    val airportSurcharge: Double = 8.50,
    val valetRefuelServiceFee: Double = 15.0,
    val rushExpressFee: Double = 12.0
)

class TerminalDropViewModel(
    private val repository: BookingRepository
) : ViewModel() {

    private val _role = MutableStateFlow(AppRole.TRAVELER)
    val role: StateFlow<AppRole> = _role.asStateFlow()

    private val _travelerTab = MutableStateFlow(TravelerTab.HOME)
    val travelerTab: StateFlow<TravelerTab> = _travelerTab.asStateFlow()

    private val _operatorTab = MutableStateFlow(OperatorTab.QUEUE)
    val operatorTab: StateFlow<OperatorTab> = _operatorTab.asStateFlow()

    private val _selectedBookingId = MutableStateFlow<Long?>(null)
    val selectedBookingId: StateFlow<Long?> = _selectedBookingId.asStateFlow()

    private val _bookingDraft = MutableStateFlow(BookingDraft())
    val bookingDraft: StateFlow<BookingDraft> = _bookingDraft.asStateFlow()

    private val _pricingSettings = MutableStateFlow(PricingSettings())
    val pricingSettings: StateFlow<PricingSettings> = _pricingSettings.asStateFlow()

    private val _isBookingWizardOpen = MutableStateFlow(false)
    val isBookingWizardOpen: StateFlow<Boolean> = _isBookingWizardOpen.asStateFlow()

    // Inspection Dialog / Handshake Bottom Sheet State for Operators
    private val _inspectingBooking = MutableStateFlow<CarReturnBooking?>(null)
    val inspectingBooking: StateFlow<CarReturnBooking?> = _inspectingBooking.asStateFlow()

    val allBookings: StateFlow<List<CarReturnBooking>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeBookings: StateFlow<List<CarReturnBooking>> = repository.activeBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pastBookings: StateFlow<List<CarReturnBooking>> = repository.pastBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active tracking target: either the explicitly selected booking or the most recent active one
    val currentActiveBooking: StateFlow<CarReturnBooking?> = combine(
        activeBookings,
        _selectedBookingId,
        allBookings
    ) { activeList, selectedId, allList ->
        if (selectedId != null) {
            allList.find { it.id == selectedId } ?: activeList.firstOrNull()
        } else {
            activeList.firstOrNull()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val operatorStats: StateFlow<BusinessOperatorStats> = allBookings.combine(_pricingSettings) { list, _ ->
        repository.computeOperatorStats(list)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        BusinessOperatorStats(3, 1, 197.50, 21, 4)
    )

    fun setRole(newRole: AppRole) {
        _role.value = newRole
    }

    fun setTravelerTab(tab: TravelerTab) {
        _travelerTab.value = tab
    }

    fun setOperatorTab(tab: OperatorTab) {
        _operatorTab.value = tab
    }

    fun selectBooking(bookingId: Long) {
        _selectedBookingId.value = bookingId
        _travelerTab.value = TravelerTab.ACTIVE_RADAR
    }

    fun openBookingWizard() {
        _bookingDraft.value = BookingDraft()
        _isBookingWizardOpen.value = true
    }

    fun closeBookingWizard() {
        _isBookingWizardOpen.value = false
    }

    fun updateBookingDraft(transform: (BookingDraft) -> BookingDraft) {
        _bookingDraft.value = transform(_bookingDraft.value)
    }

    fun submitBookingDraft() {
        viewModelScope.launch {
            val draft = _bookingDraft.value
            val pin = (1000 + Random.nextInt(9000)).toString()
            val code = "TD-${1000 + Random.nextInt(9000)}"
            val newBooking = CarReturnBooking(
                bookingCode = code,
                passengerName = draft.passengerName.ifBlank { "Traveler" },
                passengerPhone = draft.passengerPhone.ifBlank { "(555) 000-0000" },
                airportCode = draft.airportCode,
                terminalName = draft.terminalName,
                curbsidePillar = draft.curbsidePillar,
                flightNumber = draft.flightNumber.ifBlank { "FL 100" },
                airline = draft.airline.ifBlank { "Airline" },
                scheduledDropTime = draft.scheduledDropTime,
                rentalCompany = draft.rentalCompany,
                rentalAgreementNumber = draft.rentalAgreementNumber.ifBlank { "RA-${Random.nextInt(10000, 99999)}" },
                vehicleMakeModel = draft.vehicleMakeModel.ifBlank { "Sedan / SUV" },
                vehicleColor = draft.vehicleColor.ifBlank { "Silver" },
                licensePlate = draft.licensePlate.ifBlank { "7XYZ00" },
                refuelOption = draft.refuelOption,
                isPriorityRush = draft.isPriorityRush,
                handoffPin = pin,
                status = BookingStatus.CONFIRMED,
                assignedAgentName = "Marcus Cole (Badge #104)",
                assignedAgentPhone = "(310) 555-0194",
                baseFee = draft.baseFee,
                airportFee = draft.airportFee,
                refuelFee = draft.refuelFee,
                rushFee = draft.rushFee
            )
            val newId = repository.createBooking(newBooking)
            _selectedBookingId.value = newId
            _isBookingWizardOpen.value = false
            _travelerTab.value = TravelerTab.ACTIVE_RADAR
        }
    }

    fun advanceBookingStatus(booking: CarReturnBooking, nextStatus: BookingStatus) {
        viewModelScope.launch {
            repository.advanceStatus(booking, nextStatus)
        }
    }

    fun openInspectionModal(booking: CarReturnBooking) {
        _inspectingBooking.value = booking
    }

    fun closeInspectionModal() {
        _inspectingBooking.value = null
    }

    fun completeIntakeChecklist(
        booking: CarReturnBooking,
        mileage: Int,
        fuelPercent: Int,
        notes: String
    ) {
        viewModelScope.launch {
            repository.advanceStatus(
                currentBooking = booking,
                newStatus = BookingStatus.EN_ROUTE_TO_RETURN,
                intakeMileage = mileage,
                intakeFuel = fuelPercent,
                conditionNotes = notes
            )
            _inspectingBooking.value = null
        }
    }

    fun finalizeRentalReturn(
        booking: CarReturnBooking,
        bayNumber: String,
        lotAgentName: String
    ) {
        viewModelScope.launch {
            repository.advanceStatus(
                currentBooking = booking,
                newStatus = BookingStatus.RETURNED_SUCCESS,
                bayNumber = bayNumber.ifBlank { "Bay 3A Rapid Check" },
                lotAgentName = lotAgentName.ifBlank { "Lot Attendant" }
            )
        }
    }

    fun updatePricing(baseFee: Double, airportFee: Double, fuelFee: Double, rushFee: Double) {
        _pricingSettings.value = PricingSettings(
            baseValetFee = baseFee,
            airportSurcharge = airportFee,
            valetRefuelServiceFee = fuelFee,
            rushExpressFee = rushFee
        )
    }
}
