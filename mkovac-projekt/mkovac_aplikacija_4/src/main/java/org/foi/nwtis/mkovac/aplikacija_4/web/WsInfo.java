package org.foi.nwtis.mkovac.aplikacija_4.web;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/info")
public class WsInfo {
  private static Session session;

  @OnOpen
  public void onOpen(Session session) {
    WsInfo.session = session;
  }

  @OnMessage
  public void onMessage(String message, Session session) {
    if (message.equals("dajMeteo(info)")) {
      // TODO
    }
  }

  @OnClose
  public void onClose(Session session, CloseReason reason) {

  }

  public static void posaljiObavijest(String poruka) {
    try {
      String jsonPoruka = new Gson().toJson(poruka);
      if (session != null)
        session.getBasicRemote().sendText(jsonPoruka);
      else
        throw new IOException("Nema povezanih WS klijenata!");
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }
}
