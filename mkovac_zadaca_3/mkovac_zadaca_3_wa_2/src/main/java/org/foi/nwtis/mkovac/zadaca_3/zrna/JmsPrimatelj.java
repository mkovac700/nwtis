package org.foi.nwtis.mkovac.zadaca_3.zrna;

import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

@MessageDriven(mappedName = "jms/nwtis_queue_dz3",
    activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
            propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "jakarta.jms.Queue")})
public class JmsPrimatelj implements MessageListener {

  @EJB
  SakupljacJmsPoruka sakupljacJmsPoruka;

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
