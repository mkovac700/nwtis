package org.foi.nwtis.mkovac.aplikacija_2.zrna;

import java.util.List;
import org.foi.nwtis.mkovac.aplikacija_2.jpa.Dnevnik;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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

  public List<Dnevnik> findAll() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Dnevnik> cq = cb.createQuery(Dnevnik.class);
    cq.select(cq.from(Dnevnik.class));
    return em.createQuery(cq).getResultList();
  }

  public List<Dnevnik> findAll(int odBroja, int broj) {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Dnevnik> cq = cb.createQuery(Dnevnik.class);
    cq.select(cq.from(Dnevnik.class));
    TypedQuery<Dnevnik> q = em.createQuery(cq);
    q.setMaxResults(broj);
    q.setFirstResult(odBroja);
    return q.getResultList();
  }

  public List<Dnevnik> findAll(String vrsta, int odBroja, int broj) {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Dnevnik> cq = cb.createQuery(Dnevnik.class);
    Root<Dnevnik> rt = cq.from(Dnevnik.class);
    if (vrsta != null && !vrsta.isEmpty())
      cq.where(cb.equal(rt.get("vrsta"), vrsta));
    TypedQuery<Dnevnik> q = em.createQuery(cq);
    q.setMaxResults(broj);
    q.setFirstResult(odBroja);
    return q.getResultList();
  }
}
