package org.foi.nwtis.mkovac.aplikacija_5.zrna;

import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

/**
 * Zrno za primanje JMS poruka
 * 
 * @author Marijan Kovač
 *
 */
@MessageDriven(mappedName = "jms/NWTiS_mkovac",
    activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
            propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "jakarta.jms.Queue")})
public class JmsPrimatelj implements MessageListener {

  @EJB
  SakupljacJmsPoruka sakupljacJmsPoruka;

  /**
   * Osluškuje dolazak poruke te obavlja dodavanje poruke u SakupljacJmsPoruka
   */
  @Override
  public void onMessage(Message message) {
    if (message instanceof TextMessage) {
      try {
        var msg = (TextMessage) message;
        Logger.getGlobal().log(Level.INFO, "Stigla poruka: " + msg.getText());
        sakupljacJmsPoruka.dodajPoruku(msg.getText());
      } catch (Exception ex) {
        Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
      }
    }
  }

}
