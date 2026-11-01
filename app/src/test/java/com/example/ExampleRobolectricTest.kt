package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AirportData
import com.example.data.model.BookingStatus
import com.example.data.model.RefuelOption
import com.example.ui.components.ValetReturnStage
import com.example.ui.viewmodel.BookingDraft
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("TerminalDrop", appName)
  }

  @Test
  fun `verify airport data and fee calculation`() {
    val lax = AirportData.findAirport("LAX")
    assertEquals("LAX", lax.code)
    assertTrue(lax.averageShuttleMinutesSaved > 30)

    val draft = BookingDraft(
      airportCode = "LAX",
      refuelOption = RefuelOption.VALET_TOP_OFF,
      isPriorityRush = true
    )
    // 49 (base) + 9.50 (LAX surcharge) + 15 (valet top-off) + 12 (rush) = 85.50
    assertEquals(85.50, draft.totalEstimatedFee, 0.01)
  }

  @Test
  fun `verify valet return 4 stage tracker labels and mapping`() {
    val stages = ValetReturnStage.entries
    assertEquals(4, stages.size)
    assertEquals("Assigned", stages[0].label)
    assertEquals("Concierge En Route", stages[1].label)
    assertEquals("Vehicle Picked Up", stages[2].label)
    assertEquals("Returned", stages[3].label)

    assertEquals(ValetReturnStage.ASSIGNED, ValetReturnStage.fromBookingStatus(BookingStatus.CONFIRMED))
    assertEquals(ValetReturnStage.CONCIERGE_EN_ROUTE, ValetReturnStage.fromBookingStatus(BookingStatus.VALET_DISPATCHED))
    assertEquals(ValetReturnStage.CONCIERGE_EN_ROUTE, ValetReturnStage.fromBookingStatus(BookingStatus.AT_CURBSIDE))
    assertEquals(ValetReturnStage.VEHICLE_PICKED_UP, ValetReturnStage.fromBookingStatus(BookingStatus.INSPECTING_HANDOFF))
    assertEquals(ValetReturnStage.VEHICLE_PICKED_UP, ValetReturnStage.fromBookingStatus(BookingStatus.EN_ROUTE_TO_RETURN))
    assertEquals(ValetReturnStage.RETURNED, ValetReturnStage.fromBookingStatus(BookingStatus.RETURNED_SUCCESS))
  }

  @Test
  fun `verify legal compliance pillars and neutral brand colors`() {
    assertTrue(AirportData.LEGAL_COMPLIANCE_PILLARS.size >= 4)
    AirportData.RENTAL_COMPANIES.forEach { agency ->
      assertEquals(0xFF1E3A5FL, agency.brandHexColor)
    }
  }
}
