package org.foi.nwtis.mkovac.aplikacija_3.zrna;

import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

/**
 * Zrno za slanje JMS poruka
 * 
 * @author Marijan Kovač
 *
 */
@Stateless
public class JmsPosiljatelj {
  public static int brojPoruka = 0;

  @Resource(mappedName = "jms/nwtis_qf_dz3")
  private ConnectionFactory connectionFactory;
  @Resource(mappedName = "jms/nwtis_queue_dz3")
  private Queue queue;

  /**
   * Kreira connection factory te šalje JMS poruku
   * 
   * @param tekstPoruke Tekst poruke
   * @return true ako je u redu, inače false
   */
  public boolean saljiPoruku(String tekstPoruke) {
    boolean status = true;
    try {
      Connection connection = connectionFactory.createConnection();
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageProducer messageProducer = session.createProducer(queue);
      TextMessage message = session.createTextMessage();

      String poruka = tekstPoruke;

      message.setText(poruka);
      messageProducer.send(message);
      messageProducer.close();
      connection.close();
    } catch (JMSException ex) {
      Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
      status = false;
    }
    return status;
  }
}
