package org.foi.nwtis.mkovac.zadaca_3.zrna;

import org.foi.nwtis.mkovac.zadaca_3.jpa.LetoviPolasci;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;

/**
 * 
 * @author Marijan Kovač
 *
 */
@Stateless
public class LetoviPolasciFacade {
  @PersistenceContext(unitName = "nwtis_dz3_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    System.out.println("LetoviPolasciFacade- init");
    cb = em.getCriteriaBuilder();
  }

  public void create(LetoviPolasci letoviPolasci) {
    em.persist(letoviPolasci);
  }

  public void edit(LetoviPolasci letoviPolasci) {
    em.merge(letoviPolasci);
  }

  public void remove(LetoviPolasci letoviPolasci) {
    em.remove(em.merge(letoviPolasci));
  }

  public LetoviPolasci find(Object id) {
    return em.find(LetoviPolasci.class, id);
  }
}
