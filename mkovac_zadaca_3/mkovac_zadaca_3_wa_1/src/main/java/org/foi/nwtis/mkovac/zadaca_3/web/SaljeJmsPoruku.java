package org.foi.nwtis.mkovac.zadaca_3.web;

import java.io.IOException;
import org.foi.nwtis.mkovac.zadaca_3.zrna.JmsPosiljatelj;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * HttpServlet za slanje JMS poruke. Služi samo za testiranje JMS.
 * 
 * @author Marijan Kovač
 *
 */
@WebServlet(name = "SaljeJmsPoruku", urlPatterns = {"/SaljeJmsPoruku"})
public class SaljeJmsPoruku extends HttpServlet {

  private static final long serialVersionUID = 520306005277410260L;

  @EJB
  JmsPosiljatelj jmsPosiljatelj;

  /**
   * Na GET zahtjev obavlja slanje JMS poruke zadane parametrom upita
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    var poruka = req.getParameter("poruka");
    if (poruka != null && !poruka.isEmpty()) {
      if (jmsPosiljatelj.saljiPoruku(poruka)) {
        System.out.println("Poruka je poslana!");
        return;
      }
      System.out.println("Greška kod slanja");
      return;
    }
    System.out.println("Poruka nema tekst!");
  }

}
