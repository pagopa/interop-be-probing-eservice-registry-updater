package it.pagopa.interop.probing.eservice.registry.updater.unit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceState;

class EserviceStateTest {
  @Test
  @DisplayName("getValue should return the correct value")
  void testGetValue_thenReturnsCorrectValue() {
    assertEquals("ACTIVE", EserviceState.ACTIVE.getValue());
  }

  @Test
  @DisplayName("fromValue with 'ACTIVE' should return EserviceState.ACTIVE")
  void testFromValue_givenValueACTIVE_thenReturnsEserviceStateACTIVE() {
    EserviceState status = EserviceState.fromValue("ACTIVE");

    assertEquals(EserviceState.ACTIVE, status);
  }

  @Test
  @DisplayName("fromValue with 'INACTIVE' should return EserviceStatus.INACTIVE")
  void testFromValue_givenValueINACTIVE_thenReturnsEserviceStatusINACTIVE() {
    EserviceState status = EserviceState.fromValue("INACTIVE");

    assertEquals(EserviceState.INACTIVE, status);
  }

  @Test
  @DisplayName("fromValue with invalid value should throw an exception")
  void testFromValue_givenInvalidValue_thenThrowException() {
    assertThrows(IllegalArgumentException.class, () -> EserviceState.fromValue("SUCCESS"));
  }

  @Test
  @DisplayName("toString should return the string representation of EserviceStatus.KO")
  void testToString_givenValidValueKO_thenReturnsStringValue() {
    assertEquals("INACTIVE", EserviceState.INACTIVE.toString());
  }
}
