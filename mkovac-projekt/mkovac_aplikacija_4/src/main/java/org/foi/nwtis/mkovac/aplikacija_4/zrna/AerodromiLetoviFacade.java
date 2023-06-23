package org.foi.nwtis.mkovac.aplikacija_4.zrna;

import java.util.List;
import org.foi.nwtis.mkovac.aplikacija_4.jpa.AerodromiLetovi;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * Facade klasa za aerodrome letove
 * 
 * @author Marijan Kovač
 */
@Stateless
public class AerodromiLetoviFacade {

  @PersistenceContext(unitName = "nwtis_dz3_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    System.out.println("AerodromiLetoviFacade- init");
    cb = em.getCriteriaBuilder();
  }

  public void create(AerodromiLetovi aerodromiLetovi) {
    em.persist(aerodromiLetovi);
  }

  public void edit(AerodromiLetovi aerodromiLetovi) {
    em.merge(aerodromiLetovi);
  }

  public void remove(AerodromiLetovi aerodromiLetovi) {
    em.remove(em.merge(aerodromiLetovi));
  }

  public AerodromiLetovi find(Object id) {
    return em.find(AerodromiLetovi.class, id);
  }

  public AerodromiLetovi findOne(String icao) throws NoResultException {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<AerodromiLetovi> cq = cb.createQuery(AerodromiLetovi.class);
    Root<AerodromiLetovi> rt = cq.from(AerodromiLetovi.class);
    cq.where(cb.equal(rt.get("airport").get("icao"), icao));
    return em.createQuery(cq).getSingleResult();
  }

  public List<AerodromiLetovi> findAll() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<AerodromiLetovi> cq = cb.createQuery(AerodromiLetovi.class);
    cq.select(cq.from(AerodromiLetovi.class));
    return em.createQuery(cq).getResultList();
  }

  public List<AerodromiLetovi> findAll(boolean preuzimanje) {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<AerodromiLetovi> cq = cb.createQuery(AerodromiLetovi.class);
    Root<AerodromiLetovi> rt = cq.from(AerodromiLetovi.class);
    cq.where(cb.equal(rt.get("preuzimanje"), preuzimanje));
    return em.createQuery(cq).getResultList();
  }

  public int count() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<AerodromiLetovi> rt = cq.from(AerodromiLetovi.class);
    cq.select(cb.count(rt));
    Query q = em.createQuery(cq);
    return ((Long) q.getSingleResult()).intValue();
  }

}
