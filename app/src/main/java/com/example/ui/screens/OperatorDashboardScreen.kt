package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AirportData
import com.example.data.model.BookingStatus
import com.example.data.model.BusinessOperatorStats
import com.example.data.model.CarReturnBooking
import com.example.ui.components.BookingStatusBadge
import com.example.ui.components.RentalCompanyBadge
import com.example.ui.theme.AeroBlue
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.StatusBlue
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusOrange
import com.example.ui.theme.ValetGold

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OperatorDashboardScreen(
    bookings: List<CarReturnBooking>,
    stats: BusinessOperatorStats,
    onAdvanceStatus: (CarReturnBooking, BookingStatus) -> Unit,
    onCompleteIntake: (CarReturnBooking, Int, Int, String) -> Unit,
    onFinalizeReturn: (CarReturnBooking, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf<String>("ALL") }
    var activeIntakeBooking by remember { mutableStateOf<CarReturnBooking?>(null) }
    var activeReturnBooking by remember { mutableStateOf<CarReturnBooking?>(null) }

    val filteredBookings = remember(bookings, selectedFilter) {
        when (selectedFilter) {
            "NEEDS_DISPATCH" -> bookings.filter { it.status == BookingStatus.CONFIRMED }
            "AT_CURB" -> bookings.filter { it.status == BookingStatus.VALET_DISPATCHED || it.status == BookingStatus.AT_CURBSIDE || it.status == BookingStatus.INSPECTING_HANDOFF }
            "IN_TRANSIT" -> bookings.filter { it.status == BookingStatus.EN_ROUTE_TO_RETURN }
            "COMPLETED" -> bookings.filter { it.status == BookingStatus.RETURNED_SUCCESS }
            else -> bookings
        }
    }

    Surface(
        color = Color(0xFFF8FAFC),
        modifier = modifier
            .fillMaxSize()
            .testTag("operator_dashboard_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 96.dp)
        ) {
            // Business Operations Header
            item {
                Surface(
                    color = Navy900,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "TerminalDrop Business Portal",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = "Airport Valet Pickup Operations & Dispatch",
                                    color = ValetGold,
                                    fontSize = 12.sp
                                )
                            }
                            Surface(
                                color = StatusGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(StatusGreen))
                                    Text(text = "LIVE DISPATCH", color = StatusGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Business KPI Cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            KpiCard(
                                title = "Active Drops",
                                value = stats.activePickupsCount.toString(),
                                subtitle = "at terminals",
                                modifier = Modifier.weight(1f)
                            )
                            KpiCard(
                                title = "Done Today",
                                value = stats.completedTodayCount.toString(),
                                subtitle = "returns",
                                modifier = Modifier.weight(1f)
                            )
                            KpiCard(
                                title = "Gross Fees",
                                value = "$${stats.totalGrossRevenue.toInt()}",
                                subtitle = "revenue",
                                isGold = true,
                                modifier = Modifier.weight(1f)
                            )
                            KpiCard(
                                title = "Avg Return",
                                value = "${stats.averageTurnaroundMinutes}m",
                                subtitle = "turnaround",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Filter Chips
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(
                        text = "TERMINAL PICKUP QUEUE (${filteredBookings.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate500,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        QueueFilterChip(label = "All (${bookings.size})", isSelected = selectedFilter == "ALL") { selectedFilter = "ALL" }
                        QueueFilterChip(label = "Needs Dispatch", isSelected = selectedFilter == "NEEDS_DISPATCH") { selectedFilter = "NEEDS_DISPATCH" }
                        QueueFilterChip(label = "At Curbside", isSelected = selectedFilter == "AT_CURB") { selectedFilter = "AT_CURB" }
                        QueueFilterChip(label = "In Transit to Lot", isSelected = selectedFilter == "IN_TRANSIT") { selectedFilter = "IN_TRANSIT" }
                        QueueFilterChip(label = "Completed", isSelected = selectedFilter == "COMPLETED") { selectedFilter = "COMPLETED" }
                    }
                }
            }

            // Queue List
            items(filteredBookings, key = { it.id }) { booking ->
                OperatorJobCard(
                    booking = booking,
                    onAdvance = { next -> onAdvanceStatus(booking, next) },
                    onOpenIntake = { activeIntakeBooking = booking },
                    onOpenReturnCheckin = { activeReturnBooking = booking },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }

    // Curbside Intake Dialog
    activeIntakeBooking?.let { booking ->
        IntakeInspectionDialog(
            booking = booking,
            onDismiss = { activeIntakeBooking = null },
            onConfirm = { mileage, fuel, notes ->
                onCompleteIntake(booking, mileage, fuel, notes)
                activeIntakeBooking = null
            }
        )
    }

    // Rental Lot Return Dialog
    activeReturnBooking?.let { booking ->
        RentalLotCheckinDialog(
            booking = booking,
            onDismiss = { activeReturnBooking = null },
            onConfirm = { bay, lotAgent ->
                onFinalizeReturn(booking, bay, lotAgent)
                activeReturnBooking = null
            }
        )
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    isGold: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Navy800,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isGold) ValetGold.copy(alpha = 0.5f) else Color(0xFF1E3A5F)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 9.sp, color = Slate300, maxLines = 1)
            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isGold) ValetGold else Color.White
            )
            Text(text = subtitle, fontSize = 8.sp, color = Slate500, maxLines = 1)
        }
    }
}

@Composable
private fun QueueFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) Navy900 else Color.White,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Navy900 else Color(0xFFE2E8F0)),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            color = if (isSelected) ValetGold else Slate700,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun OperatorJobCard(
    booking: CarReturnBooking,
    onAdvance: (BookingStatus) -> Unit,
    onOpenIntake: () -> Unit,
    onOpenReturnCheckin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rentalInfo = AirportData.findRentalCompany(booking.rentalCompany)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (booking.status.isTerminalPhase()) ValetGold else Color(0xFFE2E8F0)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("operator_job_${booking.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(color = Navy900, shape = RoundedCornerShape(6.dp)) {
                        Text(
                            text = booking.airportCode,
                            color = ValetGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "${booking.passengerName} (${booking.bookingCode})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Slate900
                        )
                        Text(
                            text = "${booking.passengerPhone} • Flight: ${booking.flightNumber}",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }
                }

                BookingStatusBadge(status = booking.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Curbside Pillar highlight
            Surface(
                color = Slate100,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AeroBlue, modifier = Modifier.size(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${booking.terminalName} - ${booking.curbsidePillar}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Slate900
                        )
                        Text(
                            text = "Scheduled: ${booking.scheduledDropTime}",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }
                    // Curbside PIN
                    Surface(
                        color = Navy900,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "PIN: ${booking.handoffPin}",
                            color = ValetGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Vehicle and return facility instructions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${booking.vehicleColor} ${booking.vehicleMakeModel}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Slate900
                    )
                    Text(
                        text = "Plate: ${booking.licensePlate} • Agmt: ${booking.rentalAgreementNumber}",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
                RentalCompanyBadge(name = booking.rentalCompany)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Return to: ${rentalInfo.defaultReturnFacility}",
                fontSize = 11.sp,
                color = Slate700,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            // Operator Dispatch Action Button
            when (booking.status) {
                BookingStatus.CONFIRMED -> {
                    Button(
                        onClick = { onAdvance(BookingStatus.VALET_DISPATCHED) },
                        colors = ButtonDefaults.buttonColors(containerColor = Navy900, contentColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("dispatch_driver_button")
                    ) {
                        Text("Dispatch Driver to Terminal Curb", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                BookingStatus.VALET_DISPATCHED -> {
                    Button(
                        onClick = { onAdvance(BookingStatus.AT_CURBSIDE) },
                        colors = ButtonDefaults.buttonColors(containerColor = ValetGold, contentColor = Navy900),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("mark_arrived_button")
                    ) {
                        Text("Mark Arrived at Pillar (Standing by)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                BookingStatus.AT_CURBSIDE, BookingStatus.INSPECTING_HANDOFF -> {
                    Button(
                        onClick = onOpenIntake,
                        colors = ButtonDefaults.buttonColors(containerColor = StatusGreen, contentColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("start_intake_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.Key, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text("Perform Curbside Intake & Collect Keys", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
                BookingStatus.EN_ROUTE_TO_RETURN -> {
                    Button(
                        onClick = onOpenReturnCheckin,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E22CE), contentColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("log_return_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text("Log Rental Agency Return & Send Receipt", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
                BookingStatus.RETURNED_SUCCESS -> {
                    Surface(
                        color = Color(0xFFD1FAE5),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(16.dp))
                            Text(
                                text = "Return Completed • Bay ${booking.returnBayNumber.ifBlank { "OK" }} • Fee $${String.format("%.2f", booking.totalFee)} Earned",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46)
                            )
                        }
                    }
                }
                BookingStatus.CANCELLED -> {
                    Text(text = "Booking Cancelled", fontSize = 12.sp, color = Slate500)
                }
            }
        }
    }
}

@Composable
private fun IntakeInspectionDialog(
    booking: CarReturnBooking,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, String) -> Unit
) {
    var mileageText by remember { mutableStateOf(booking.intakeMileage.toString()) }
    var fuelPercent by remember { mutableFloatStateOf(booking.intakeFuelPercent.toFloat()) }
    var notesText by remember { mutableStateOf("Clean condition, no body scratches, 2 fobs collected.") }
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Curbside Intake Walkaround",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Slate900
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Verify customer PIN with ${booking.passengerName} and record vehicle state before taking possession:",
                    fontSize = 12.sp,
                    color = Slate700
                )

                // Customer PIN check
                OutlinedTextField(
                    value = enteredPin,
                    onValueChange = {
                        enteredPin = it
                        pinError = false
                    },
                    label = { Text("Customer Handoff PIN (Required: ${booking.handoffPin})") },
                    placeholder = { Text(booking.handoffPin) },
                    isError = pinError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("intake_pin_input")
                )
                if (pinError) {
                    Text("PIN does not match customer's code!", color = Color.Red, fontSize = 11.sp)
                }

                // Odometer
                OutlinedTextField(
                    value = mileageText,
                    onValueChange = { mileageText = it },
                    label = { Text("Odometer Mileage") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Fuel Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Fuel Level", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "${fuelPercent.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AeroBlue)
                    }
                    Slider(
                        value = fuelPercent,
                        onValueChange = { fuelPercent = it },
                        valueRange = 0f..100f,
                        steps = 9,
                        colors = SliderDefaults.colors(thumbColor = ValetGold, activeTrackColor = ValetGold)
                    )
                }

                // Scratch/Damage Notes
                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text("Walkaround / Scratch Notes") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (enteredPin.trim() != booking.handoffPin && enteredPin.isNotBlank()) {
                        pinError = true
                    } else {
                        val mileage = mileageText.toIntOrNull() ?: booking.intakeMileage
                        onConfirm(mileage, fuelPercent.toInt(), notesText)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Navy900, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("confirm_intake_button")
            ) {
                Text("Confirm Keys & Drive to Lot", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun RentalLotCheckinDialog(
    booking: CarReturnBooking,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var bayNumber by remember { mutableStateOf("Bay 2B - Express Return") }
    var lotAgentName by remember { mutableStateOf("David R. (${booking.rentalCompany} Check-in)") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Complete Rental Agency Return",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Slate900
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Car arrived at ${booking.rentalCompany} Return Facility. Log return spot and attendant name to issue the traveler's digital return receipt:",
                    fontSize = 12.sp,
                    color = Slate700
                )

                OutlinedTextField(
                    value = bayNumber,
                    onValueChange = { bayNumber = it },
                    label = { Text("Rental Return Bay / Lane #") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("bay_number_input")
                )

                OutlinedTextField(
                    value = lotAgentName,
                    onValueChange = { lotAgentName = it },
                    label = { Text("Rental Company Attendant Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Billing Summary",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Slate700
                        )
                        Text(
                            text = "Fee Collected: $${String.format("%.2f", booking.totalFee)}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Navy900
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(bayNumber, lotAgentName) },
                colors = ButtonDefaults.buttonColors(containerColor = StatusGreen, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("finalize_return_button")
            ) {
                Text("Issue Receipt & Close Job", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
