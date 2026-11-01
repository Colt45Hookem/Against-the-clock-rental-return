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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AirportData
import com.example.data.model.RefuelOption
import com.example.ui.components.FeeBreakdownCard
import com.example.ui.components.TimeSavingsBanner
import com.example.ui.theme.AeroBlue
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.ValetGold
import com.example.ui.viewmodel.BookingDraft

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookDropOffScreen(
    draft: BookingDraft,
    onDraftChange: ((BookingDraft) -> BookingDraft) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableIntStateOf(1) }

    Surface(
        color = Color(0xFFF8FAFC),
        modifier = modifier
            .fillMaxSize()
            .testTag("book_dropoff_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
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
                        Text(
                            text = "Book Curbside Valet Return",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        IconButton(onClick = onCancel, modifier = Modifier.size(36.dp)) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Step Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WizardStepPill(number = 1, label = "Airport", isActive = currentStep == 1, isDone = currentStep > 1)
                        Box(modifier = Modifier.weight(1f).height(2.dp).background(if (currentStep > 1) ValetGold else Color(0xFF334155)))
                        WizardStepPill(number = 2, label = "Vehicle", isActive = currentStep == 2, isDone = currentStep > 2)
                        Box(modifier = Modifier.weight(1f).height(2.dp).background(if (currentStep > 2) ValetGold else Color(0xFF334155)))
                        WizardStepPill(number = 3, label = "Services", isActive = currentStep == 3, isDone = currentStep > 3)
                        Box(modifier = Modifier.weight(1f).height(2.dp).background(if (currentStep > 3) ValetGold else Color(0xFF334155)))
                        WizardStepPill(number = 4, label = "Confirm", isActive = currentStep == 4, isDone = false)
                    }
                }
            }

            // Step Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (currentStep) {
                    1 -> {
                        item {
                            Step1AirportFlight(
                                draft = draft,
                                onDraftChange = onDraftChange
                            )
                        }
                    }
                    2 -> {
                        item {
                            Step2VehicleRental(
                                draft = draft,
                                onDraftChange = onDraftChange
                            )
                        }
                    }
                    3 -> {
                        item {
                            Step3FuelAndServices(
                                draft = draft,
                                onDraftChange = onDraftChange
                            )
                        }
                    }
                    4 -> {
                        item {
                            Step4ReviewConfirm(
                                draft = draft
                            )
                        }
                    }
                }
            }

            // Bottom Navigation Actions
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = { currentStep -= 1 },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Text("Back", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Button(
                        onClick = {
                            if (currentStep < 4) {
                                currentStep += 1
                            } else {
                                onSubmit()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentStep == 4) ValetGold else Navy900,
                            contentColor = if (currentStep == 4) Navy900 else Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(2f)
                            .height(50.dp)
                            .testTag(if (currentStep == 4) "confirm_booking_button" else "next_step_button")
                    ) {
                        Text(
                            text = if (currentStep == 4) "Confirm & Assign Valet ($${String.format("%.2f", draft.totalEstimatedFee)})" else "Continue to Next Step",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WizardStepPill(
    number: Int,
    label: String,
    isActive: Boolean,
    isDone: Boolean
) {
    val bgColor = when {
        isDone -> StatusGreen
        isActive -> ValetGold
        else -> Color(0xFF334155)
    }
    val textColor = when {
        isDone -> Color.White
        isActive -> Navy900
        else -> Slate300
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            } else {
                Text(text = number.toString(), color = textColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, color = if (isActive) ValetGold else Slate300, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step1AirportFlight(
    draft: BookingDraft,
    onDraftChange: ((BookingDraft) -> BookingDraft) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "1. Departure Airport & Terminal",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Slate900
            )
            Text(
                text = "Select where you are flying out of and your curbside meeting point.",
                fontSize = 12.sp,
                color = Slate500
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(text = "Select Airport", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Slate700)
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AirportData.AIRPORTS.forEach { airport ->
                    val isSelected = draft.airportCode == airport.code
                    Surface(
                        color = if (isSelected) Navy900 else Slate100,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) ValetGold else Color.Transparent
                        ),
                        modifier = Modifier
                            .clickable {
                                onDraftChange {
                                    it.copy(
                                        airportCode = airport.code,
                                        terminalName = airport.terminals.firstOrNull() ?: "Terminal 1",
                                        curbsidePillar = airport.defaultPillars.firstOrNull() ?: "Upper Level"
                                    )
                                }
                            }
                            .testTag("airport_chip_${airport.code}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = airport.code,
                                color = if (isSelected) ValetGold else Slate900,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val currentAirport = draft.airportInfo
            TimeSavingsBanner(minutesSaved = currentAirport.averageShuttleMinutesSaved)

            Spacer(modifier = Modifier.height(14.dp))

            Text(text = "Terminal", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Slate700)
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                currentAirport.terminals.forEach { term ->
                    val isSelected = draft.terminalName == term
                    Surface(
                        color = if (isSelected) AeroBlue.copy(alpha = 0.15f) else Slate100,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) AeroBlue else Color.Transparent
                        ),
                        modifier = Modifier.clickable {
                            onDraftChange { it.copy(terminalName = term) }
                        }
                    ) {
                        Text(
                            text = term,
                            color = if (isSelected) AeroBlue else Slate900,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = draft.curbsidePillar,
                onValueChange = { pillar -> onDraftChange { it.copy(curbsidePillar = pillar) } },
                label = { Text("Curbside Pillar / Door Location") },
                placeholder = { Text("e.g. Pillar 4A Upper Departures") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("curbside_pillar_input"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = draft.airline,
                    onValueChange = { airline -> onDraftChange { it.copy(airline = airline) } },
                    label = { Text("Airline") },
                    placeholder = { Text("American Airlines") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = draft.flightNumber,
                    onValueChange = { flight -> onDraftChange { it.copy(flightNumber = flight) } },
                    label = { Text("Flight #") },
                    placeholder = { Text("AA 1428") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = draft.scheduledDropTime,
                onValueChange = { time -> onDraftChange { it.copy(scheduledDropTime = time) } },
                label = { Text("Estimated Drop Time") },
                placeholder = { Text("e.g. In 20 mins / 4:15 PM") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step2VehicleRental(
    draft: BookingDraft,
    onDraftChange: ((BookingDraft) -> BookingDraft) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "2. Rental Agency & Car Details",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Slate900
            )
            Text(
                text = "Our concierge will return the vehicle to this rental agency's official check-in lanes.",
                fontSize = 12.sp,
                color = Slate500
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(text = "Rental Company", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Slate700)
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AirportData.RENTAL_COMPANIES.forEach { agency ->
                    val isSelected = draft.rentalCompany.equals(agency.name, ignoreCase = true)
                    Surface(
                        color = if (isSelected) Navy900 else Slate100,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) ValetGold else Color.Transparent
                        ),
                        modifier = Modifier
                            .clickable {
                                onDraftChange { it.copy(rentalCompany = agency.name) }
                            }
                            .testTag("agency_chip_${agency.id}")
                    ) {
                        Text(
                            text = agency.name,
                            color = if (isSelected) ValetGold else Slate900,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = draft.vehicleMakeModel,
                onValueChange = { makeModel -> onDraftChange { it.copy(vehicleMakeModel = makeModel) } },
                label = { Text("Vehicle Make & Model") },
                placeholder = { Text("e.g. 2024 Toyota RAV4") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("make_model_input"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = draft.vehicleColor,
                    onValueChange = { color -> onDraftChange { it.copy(vehicleColor = color) } },
                    label = { Text("Color") },
                    placeholder = { Text("Silver") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = draft.licensePlate,
                    onValueChange = { plate -> onDraftChange { it.copy(licensePlate = plate) } },
                    label = { Text("License Plate") },
                    placeholder = { Text("7XYZ892") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = draft.rentalAgreementNumber,
                onValueChange = { agree -> onDraftChange { it.copy(rentalAgreementNumber = agree) } },
                label = { Text("Rental Agreement # (on key tag or paper)") },
                placeholder = { Text("e.g. H-84920194") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
private fun Step3FuelAndServices(
    draft: BookingDraft,
    onDraftChange: ((BookingDraft) -> BookingDraft) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "3. Refuel & Express Options",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Slate900
            )
            Text(
                text = "Don't let rental companies charge you $11-$14/gallon penalty if you didn't have time to pump!",
                fontSize = 12.sp,
                color = Slate500
            )

            Spacer(modifier = Modifier.height(14.dp))

            RefuelOption.values().forEach { option ->
                val isSelected = draft.refuelOption == option
                Surface(
                    color = if (isSelected) ValetGold.copy(alpha = 0.1f) else Slate100,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (isSelected) ValetGold else Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onDraftChange { it.copy(refuelOption = option) } }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onDraftChange { it.copy(refuelOption = option) } },
                            colors = RadioButtonDefaults.colors(selectedColor = ValetGold)
                        )
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = option.label,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Slate900
                                )
                                if (option.fee > 0.0) {
                                    Text(
                                        text = "+$${String.format("%.2f", option.fee)} fee",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = AeroBlue
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = option.detail,
                                fontSize = 11.sp,
                                color = Slate700,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFE2E8F0))
            Spacer(modifier = Modifier.height(14.dp))

            // Priority Rush Checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDraftChange { it.copy(isPriorityRush = !it.isPriorityRush) } },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = draft.isPriorityRush,
                    onCheckedChange = { checked -> onDraftChange { it.copy(isPriorityRush = checked) } },
                    colors = CheckboxDefaults.colors(checkedColor = ValetGold)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Priority Express Curbside Dispatch (+$12.00)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Slate900
                    )
                    Text(
                        text = "Guaranteed valet arrival at curb in under 8 minutes.",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
            }
        }
    }
}

@Composable
private fun Step4ReviewConfirm(
    draft: BookingDraft
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Booking Summary",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate900
                )
                Spacer(modifier = Modifier.height(12.dp))

                ReviewLine(label = "Airport", value = "${draft.airportCode} - ${draft.terminalName}")
                ReviewLine(label = "Meeting Spot", value = draft.curbsidePillar)
                ReviewLine(label = "Flight & Airline", value = "${draft.airline} (${draft.flightNumber})")
                ReviewLine(label = "Scheduled Drop", value = draft.scheduledDropTime)
                ReviewLine(label = "Rental Company", value = draft.rentalCompany)
                ReviewLine(label = "Vehicle", value = "${draft.vehicleColor} ${draft.vehicleMakeModel} (${draft.licensePlate})")
                ReviewLine(label = "Rental Agmt #", value = draft.rentalAgreementNumber)
                ReviewLine(label = "Fuel Option", value = draft.refuelOption.label)
                if (draft.isPriorityRush) {
                    ReviewLine(label = "Dispatch", value = "Priority Express (<8 mins)")
                }
            }
        }

        FeeBreakdownCard(
            baseFee = draft.baseFee,
            airportFee = draft.airportFee,
            refuelFee = draft.refuelFee,
            rushFee = draft.rushFee,
            totalFee = draft.totalEstimatedFee
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Navy900),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = ValetGold, modifier = Modifier.size(24.dp))
                Column {
                    Text(
                        text = "Bailment & Limited Return Agency Authorization",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                    Text(
                        text = "By confirming, you appoint TerminalDrop as your independent courier & bailee to transport your vehicle from the departure curb to ${draft.rentalCompany}'s return facility under our Commercial Garagekeepers Liability coverage. TerminalDrop is an independent service not affiliated with ${draft.rentalCompany} or the airport authority.",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Slate500)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate900)
    }
}
