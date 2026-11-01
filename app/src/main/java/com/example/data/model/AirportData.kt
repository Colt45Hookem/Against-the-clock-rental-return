package com.example.data.model

data class AirportInfo(
    val code: String,
    val name: String,
    val city: String,
    val terminals: List<String>,
    val defaultPillars: List<String>,
    val averageShuttleMinutesSaved: Int,
    val baseValetFee: Double = 49.0,
    val airportSurcharge: Double = 8.50,
    val description: String
)

data class RentalCompanyInfo(
    val id: String,
    val name: String,
    val brandHexColor: Long,
    val defaultReturnFacility: String,
    val returnLaneInstructions: String,
    val agencyFuelPenaltyPerGallon: Double = 10.99
)

data class LegalCompliancePillar(
    val title: String,
    val summary: String,
    val details: String
)

object AirportData {
    val AIRPORTS = listOf(
        AirportInfo(
            code = "LAX",
            name = "Los Angeles International",
            city = "Los Angeles, CA",
            terminals = listOf("Terminal 1", "Terminal 2", "Terminal 3", "Intl Terminal (TBIT)", "Terminal 4", "Terminal 5", "Terminal 6", "Terminal 7"),
            defaultPillars = listOf("Pillar 1B - Departures", "Pillar 2D - Departures", "Pillar 3C - Departures", "Pillar TBIT-4 - International", "Pillar 4A - Departures", "Pillar 5C - Departures", "Pillar 6B - Departures", "Pillar 7A - Departures"),
            averageShuttleMinutesSaved = 38,
            baseValetFee = 49.0,
            airportSurcharge = 9.50,
            description = "Off-airport consolidated rental return shuttles often require a 20-30 min wait plus a 15 min loop in central terminal traffic. Meet your independent courier curbside and walk straight to security."
        ),
        AirportInfo(
            code = "SFO",
            name = "San Francisco International",
            city = "San Francisco, CA",
            terminals = listOf("Terminal 1", "Terminal 2", "Terminal 3", "International Terminal A", "International Terminal G"),
            defaultPillars = listOf("Terminal 1 - Door 3", "Terminal 2 - Door 2", "Terminal 3 - Door 4", "Intl A - Door 1", "Intl G - Door 2"),
            averageShuttleMinutesSaved = 32,
            baseValetFee = 49.0,
            airportSurcharge = 8.00,
            description = "Remote rental center transit requires multi-level train transfers with luggage. Hand off your keys at the departure curb in 60 seconds."
        ),
        AirportInfo(
            code = "ORD",
            name = "O'Hare International",
            city = "Chicago, IL",
            terminals = listOf("Terminal 1", "Terminal 2", "Terminal 3", "Terminal 5"),
            defaultPillars = listOf("Upper Level Door 1E", "Upper Level Door 2B", "Upper Level Door 3F", "Terminal 5 Door 5C"),
            averageShuttleMinutesSaved = 42,
            baseValetFee = 52.0,
            airportSurcharge = 8.50,
            description = "Remote multi-modal rental facility transit takes 25+ minutes during peak departures. Drop off directly at the upper departure curb."
        ),
        AirportInfo(
            code = "ATL",
            name = "Hartsfield-Jackson Atlanta",
            city = "Atlanta, GA",
            terminals = listOf("Domestic North Terminal", "Domestic South Terminal", "International Terminal"),
            defaultPillars = listOf("North Door N-3", "South Door S-2", "Intl Departures Door 2"),
            averageShuttleMinutesSaved = 35,
            baseValetFee = 47.0,
            airportSurcharge = 7.50,
            description = "Bypass the remote rental car center transit train and luggage escalators by meeting your designated courier at the terminal curb."
        ),
        AirportInfo(
            code = "DFW",
            name = "Dallas/Fort Worth International",
            city = "Dallas, TX",
            terminals = listOf("Terminal A", "Terminal B", "Terminal C", "Terminal D", "Terminal E"),
            defaultPillars = listOf("Terminal A - Gate A20 Curb", "Terminal B - Gate B14 Curb", "Terminal C - Gate C19 Curb", "Terminal D - Gate D22 Curb", "Terminal E - Gate E15 Curb"),
            averageShuttleMinutesSaved = 30,
            baseValetFee = 45.0,
            airportSurcharge = 7.00,
            description = "Remote rental facility buses run every 10-15 minutes. Avoid loading and unloading bags twice."
        ),
        AirportInfo(
            code = "MIA",
            name = "Miami International",
            city = "Miami, FL",
            terminals = listOf("Concourse D", "Concourse E/F", "Concourse G/H", "Concourse J"),
            defaultPillars = listOf("Door 1 - Concourse D", "Door 8 - Concourse E", "Door 15 - Concourse G", "Door 22 - Concourse J"),
            averageShuttleMinutesSaved = 40,
            baseValetFee = 49.0,
            airportSurcharge = 9.00,
            description = "Skip the crowded inter-terminal mover to the remote car center. Drop off at Departures Level 2."
        ),
        AirportInfo(
            code = "DEN",
            name = "Denver International",
            city = "Denver, CO",
            terminals = listOf("Main Terminal - West", "Main Terminal - East"),
            defaultPillars = listOf("Level 6 West - Door 606", "Level 6 East - Door 607", "Level 6 West - Door 614"),
            averageShuttleMinutesSaved = 45,
            baseValetFee = 54.0,
            airportSurcharge = 8.50,
            description = "Remote rental lots are located miles away with lengthy shuttle rides in inclement weather."
        )
    )

    // Uses neutral TerminalDrop slate/navy styling (no third-party proprietary trade dress or trademarked loyalty tier names).
    // Agency names are referenced strictly under Nominative Fair Use to identify the customer's destination return lot.
    val RENTAL_COMPANIES = listOf(
        RentalCompanyInfo(
            id = "hertz",
            name = "Hertz",
            brandHexColor = 0xFF1E3A5F,
            defaultReturnFacility = "Consolidated Rental Return — Hertz Drop Lanes",
            returnLaneInstructions = "Enter designated return gate, pull into standard check-in lane, log final odometer & fuel, and obtain attendant check-in receipt."
        ),
        RentalCompanyInfo(
            id = "enterprise",
            name = "Enterprise",
            brandHexColor = 0xFF1E3A5F,
            defaultReturnFacility = "Consolidated Rental Return — Enterprise Drop Lanes",
            returnLaneInstructions = "Proceed through return barrier to designated check-in bay, complete handheld attendant inspection, and secure return receipt."
        ),
        RentalCompanyInfo(
            id = "avis",
            name = "Avis",
            brandHexColor = 0xFF1E3A5F,
            defaultReturnFacility = "Consolidated Rental Return — Avis Drop Lanes",
            returnLaneInstructions = "Drive to standard return lanes, verify odometer/fuel with lot attendant, and collect printed or digital return confirmation."
        ),
        RentalCompanyInfo(
            id = "national",
            name = "National",
            brandHexColor = 0xFF1E3A5F,
            defaultReturnFacility = "Consolidated Rental Return — National Drop Lanes",
            returnLaneInstructions = "Enter return lane, complete vehicle check-in scan with lot staff, and record return bay number."
        ),
        RentalCompanyInfo(
            id = "sixt",
            name = "SIXT",
            brandHexColor = 0xFF1E3A5F,
            defaultReturnFacility = "Consolidated Rental Return — SIXT Drop Deck",
            returnLaneInstructions = "Proceed to return deck, complete walkaround check-in with attendant, and deposit key fob in designated return drop."
        ),
        RentalCompanyInfo(
            id = "budget",
            name = "Budget",
            brandHexColor = 0xFF1E3A5F,
            defaultReturnFacility = "Consolidated Rental Return — Budget Drop Lanes",
            returnLaneInstructions = "Pull into return lane, complete handheld receipt scan, and log final mileage."
        ),
        RentalCompanyInfo(
            id = "alamo",
            name = "Alamo",
            brandHexColor = 0xFF1E3A5F,
            defaultReturnFacility = "Consolidated Rental Return — Alamo Drop Lanes",
            returnLaneInstructions = "Enter return bay, scan rental contract barcode with attendant, and confirm zero-balance check-in."
        ),
        RentalCompanyInfo(
            id = "dollar",
            name = "Dollar",
            brandHexColor = 0xFF1E3A5F,
            defaultReturnFacility = "Consolidated Rental Return — Dollar Drop Lanes",
            returnLaneInstructions = "Pull into return line, verify fuel/odometer with attendant, and secure final check-in slip."
        ),
        RentalCompanyInfo(
            id = "other",
            name = "Other / Independent Lot",
            brandHexColor = 0xFF1E3A5F,
            defaultReturnFacility = "Designated Off-Airport Rental Return Facility",
            returnLaneInstructions = "Follow customer's rental agreement return address and obtain signed lot check-in receipt."
        )
    )

    val LEGAL_COMPLIANCE_PILLARS = listOf(
        LegalCompliancePillar(
            title = "1. Independent Third-Party Courier (No Affiliation & Nominative Fair Use)",
            summary = "100% independent service — zero trademark or trade dress infringement.",
            details = "TerminalDrop operates strictly as an independent, customer-hired vehicle courier and concierge service. TerminalDrop is NOT affiliated with, endorsed by, sponsored by, or partnered with any rental car agency, airline, or airport authority. Any rental company or airport names appear solely for nominative fair-use identification so customers can specify where their vehicle must be returned."
        ),
        LegalCompliancePillar(
            title = "2. Customer Bailment & Limited Return Agency Agreement",
            summary = "Explicit legal authorization from the renter to transport and return the vehicle.",
            details = "Before key handoff, the customer executes a Limited Vehicle Return Authorization & Bailment Agreement appointing the TerminalDrop driver as their designated courier for the sole purpose of driving the vehicle from the departure terminal curb to the designated return facility (and optional fuel station). Custody transfer is cryptographically timestamped via a 4-digit PIN, odometer reading, fuel level, and 360° condition log."
        ),
        LegalCompliancePillar(
            title = "3. Commercial Garagekeepers & Non-Owned Auto Liability Coverage",
            summary = "Dedicated commercial insurance covering in-transit bailment custody.",
            details = "During the transit window between curbside PIN verification and rental bay check-in, the vehicle and courier are covered under TerminalDrop's Commercial Garagekeepers Legal Liability and Hired/Non-Owned Auto Liability policies, protecting the traveler and the vehicle owner."
        ),
        LegalCompliancePillar(
            title = "4. Airport Curbside Active-Loading & GT Compliance",
            summary = "Zero unattended parking at departure curbs; pre-staged dispatch.",
            details = "To comply with airport ground transportation and TSA security regulations, couriers arrive on foot or via drop-shuttle at the passenger pickup pillar right as the traveler pulls up. Vehicles are never staged, parked, or left unattended at terminal curbs—handoff takes under 60 seconds in active unloading zones."
        )
    )

    fun findAirport(code: String): AirportInfo {
        return AIRPORTS.find { it.code.equals(code, ignoreCase = true) } ?: AIRPORTS.first()
    }

    fun findRentalCompany(name: String): RentalCompanyInfo {
        return RENTAL_COMPANIES.find { it.name.equals(name, ignoreCase = true) } ?: RENTAL_COMPANIES.first()
    }
}
