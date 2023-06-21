package org.foi.nwtis.mkovac.aplikacija_4.zrna;

import org.foi.nwtis.mkovac.aplikacija_4.jpa.LetoviPolasci;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * Facade klasa za letove
 * 
 * @author Marijan Kovač
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

  public LetoviPolasci findLast() throws NoResultException {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<LetoviPolasci> cq = cb.createQuery(LetoviPolasci.class);
    Root<LetoviPolasci> root = cq.from(LetoviPolasci.class);
    cq.orderBy(cb.desc(root.get("id")));
    TypedQuery<LetoviPolasci> q = em.createQuery(cq);
    q.setMaxResults(1);
    return q.getSingleResult();
  }
}
