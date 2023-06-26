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

@ServerEndpoint("/info")
public class WsInfo {
  private static Session session;

  @Inject
  KorisniciFacade korisniciFacade;

  @Inject
  AerodromiLetoviFacade aerodromiLetoviFacade;

  @OnOpen
  public void onOpen(Session session) {
    WsInfo.session = session;
  }

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

  @OnClose
  public void onClose(Session session, CloseReason reason) {

  }

  public static void posaljiObavijest(String poruka) {
    try {
      // String jsonPoruka = new Gson().toJson(poruka);
      if (session != null)
        session.getBasicRemote().sendText(poruka);
      else
        throw new IOException("Nema povezanih WS klijenata!");
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }
}
