package org.foi.nwtis.mkovac.aplikacija_3.zrna;

import java.util.List;
import org.foi.nwtis.mkovac.aplikacija_3.jpa.AerodromiLetovi;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

/**
 * Facade klasa za aerodrome letove
 * 
 * @author Marijan Kovač
 */
@Stateless
public class AerodromiLetoviFacade {

  @PersistenceContext(unitName = "nwtis_ap3_pu")
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

  public List<AerodromiLetovi> findAll() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<AerodromiLetovi> cq = cb.createQuery(AerodromiLetovi.class);
    cq.select(cq.from(AerodromiLetovi.class));
    return em.createQuery(cq).getResultList();
  }

}
