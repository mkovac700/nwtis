package org.foi.nwtis.mkovac.aplikacija_4.web;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.AerodromiLetoviFacade;
import org.foi.nwtis.mkovac.aplikacija_4.zrna.KorisniciFacade;
import jakarta.inject.Inject;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Krajnja točka info
 * 
 * @author Marijan Kovač
 *
 */
@ServerEndpoint("/info")
public class WsInfo {
  private static Session session;

  @Inject
  KorisniciFacade korisniciFacade;

  @Inject
  AerodromiLetoviFacade aerodromiLetoviFacade;

  /**
   * Osluškuje otvaranje WebSocketa i inicijalizira sesiju
   * 
   * @param session Sesija
   */
  @OnOpen
  public void onOpen(Session session) {
    WsInfo.session = session;
  }

  /**
   * Osluškuje dolazak poruke te šalje odgovor putem WebSocketa
   * 
   * @param message Dolazna poruka
   * @param session Sesija
   */
  @OnMessage
  public void onMessage(String message, Session session) {

    if (message.equals("dajBrojKorisnika")) {
      int brojKorisnika = korisniciFacade.count();
      WsInfo.posaljiObavijest("Trenutni broj korisnika: " + brojKorisnika);
    }

    if (message.equals("dajBrojAerodromaZaPreuzimanje")) {
      int brojAerodromaZaPreuzimanje = aerodromiLetoviFacade.count();
      WsInfo.posaljiObavijest(
          "Trenutni broj aerodroma za preuzimanje: " + brojAerodromaZaPreuzimanje);
    }
  }

  /**
   * Osluškuje gašenje WebSocketa
   * 
   * @param session Sesija
   * @param reason Razlog
   */
  @OnClose
  public void onClose(Session session, CloseReason reason) {

  }

  /**
   * Šalje obavijest (poruku) putem WebSocketa
   * 
   * @param poruka Tekst poruke koja se šalje
   */
  public static void posaljiObavijest(String poruka) {
    try {
      if (session != null)
        session.getBasicRemote().sendText(poruka);
      else
        throw new IOException("Nema povezanih WS klijenata!");
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }
}
