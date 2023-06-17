package org.foi.nwtis.mkovac.aplikacija_2.zrna;

import org.foi.nwtis.mkovac.aplikacija_2.jpa.Dnevnik;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;

/**
 * Facade klasa za dnevnik
 * 
 * @author Marijan Kovač
 */
@Stateless
public class DnevnikFacade {

  @PersistenceContext(unitName = "nwtis_ap2_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    System.out.println("DnevnikFacade- init");
    cb = em.getCriteriaBuilder();
  }

  public void create(Dnevnik dnevnik) {
    em.persist(dnevnik);
  }

  public void edit(Dnevnik dnevnik) {
    em.merge(dnevnik);
  }

  public void remove(Dnevnik dnevnik) {
    em.remove(em.merge(dnevnik));
  }

  public Dnevnik find(Object id) {
    return em.find(Dnevnik.class, id);
  }
}
