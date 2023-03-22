package org.foi.nwtis.mkovac.zadaca_1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class GlavniPosluziteljTest {

  private static Konfiguracija konf;
  private GlavniPosluzitelj gp;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    // poziva se prije samih testova, neke init postavke koje se vise ne mijenjaju
    konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju("NWTiS_mkovac_1.txt");
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception {
    // npr ovdje se, ako se koristi neka temp datoteka, moze obrisati
    konf = null;
  }

  @BeforeEach
  void setUp() throws Exception {
    // stvari koje se kroz testove ponavljaju, npr instanciranje klase jer nam treba svaki put
    this.gp = new GlavniPosluzitelj(GlavniPosluziteljTest.konf);
  }

  @AfterEach
  void tearDown() throws Exception {
    this.gp = null;
  }

  @Disabled("Još treba implementirati")
  @Test
  void testPokreniPosluzitelja() {
    fail("Not yet implemented");
  }

  @Test
  // given when then... da se bolje razlikuju metode (korisno kad ih ima puno)
  void akoImamoIspravnuDatotekuKadaPokrenemoUcitajKorisnikeTadaImamoKorisnike() {
    // fail("Not yet implemented");
    try {
      gp.ucitajKorisnike();
      assertEquals(10, gp.korisnici.size());
      assertEquals("Pero", gp.korisnici.get("pkos").ime());
      assertEquals("Kos", gp.korisnici.get("pkos").prezime());
      assertEquals(true, gp.korisnici.get("pkos").administrator());
      assertNull(gp.korisnici.get("mkovac"));
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Disabled("Još treba implementirati")
  @Test
  void testOtvoriMreznaVrata() {
    fail("Not yet implemented");
  }

}
