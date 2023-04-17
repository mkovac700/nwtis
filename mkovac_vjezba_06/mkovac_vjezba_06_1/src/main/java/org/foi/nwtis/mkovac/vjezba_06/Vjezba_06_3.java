package org.foi.nwtis.mkovac.vjezba_06;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "Vjezba_06_3", urlPatterns = {"/Vjezba_06_3"})
public class Vjezba_06_3 extends HttpServlet {

  private static final long serialVersionUID = -25077235507969930L;
  // private DretvaVremena dv; //ne mozemo ovako jer na kreiranje vise
  // dretvi na interrupt se gasi samo zadnja jer se ostale prepisu u
  // memoriji
  List<DretvaVremena> dretve = new ArrayList<DretvaVremena>();

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {

    int brojCiklusa =
        Integer.parseInt(request.getParameter("brojCiklusa"));
    int trajanjeCiklusa =
        Integer.parseInt(request.getParameter("trajanjeCiklusa"));

    DretvaVremena dv =
        new DretvaVremena(brojCiklusa, trajanjeCiklusa);
    this.dretve.add(dv);
    dv.start();
  }

  @Override
  public void destroy() {
    // TODO tu ide sve sto se misli napravit prije nego se ugasi
    // servlet
    for (DretvaVremena d : this.dretve) {
      if (d != null && d.isAlive()) // da se ne interrupta ako ne
                                    // postoji tj ako je vec gotova
        d.interrupt();
    }
    super.destroy();
  }


}
