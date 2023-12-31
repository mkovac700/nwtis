/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.mkovac.zadaca_3.zrna;

import java.util.List;
import org.foi.nwtis.mkovac.zadaca_3.jpa.Airports;
import org.foi.nwtis.mkovac.zadaca_3.jpa.AirportsDistanceMatrix;
import org.foi.nwtis.mkovac.zadaca_3.jpa.AirportsDistanceMatrixPK;
import org.foi.nwtis.podaci.Airport;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

/**
 * Facade klasa za aerodrome
 * 
 * @author Marijan Kovač
 */
@Stateless
public class AirportFacade {
  @PersistenceContext(unitName = "nwtis_dz3_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    System.out.println("AirportFacade- init");
    cb = em.getCriteriaBuilder();
  }

  public void create(Airports airport) {
    em.persist(airport);
  }

  public void edit(Airports airport) {
    em.merge(airport);
  }

  public void remove(Airport Airport) {
    em.remove(em.merge(Airport));
  }

  public Airports find(Object id) {
    return em.find(Airports.class, id);
  }

  public List<Airports> findAll() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Airports> cq = cb.createQuery(Airports.class);
    cq.select(cq.from(Airports.class));
    return em.createQuery(cq).getResultList();
  }

  public List<Airports> findAll(int odBroja, int broj) {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Airports> cq = cb.createQuery(Airports.class);
    cq.select(cq.from(Airports.class));
    TypedQuery<Airports> q = em.createQuery(cq);
    q.setMaxResults(broj);
    q.setFirstResult(odBroja);
    return q.getResultList();
  }

  public List<Object[]> findDistances(String icaoOd, String icaoDo) {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
    Root<AirportsDistanceMatrix> rt = cq.from(AirportsDistanceMatrix.class);
    Join<AirportsDistanceMatrix, AirportsDistanceMatrixPK> join = rt.join("id");

    cq.select(cb.array(rt.get("distCtry"), join.get("country")))
        .where(cb.equal(join.get("icaoFrom"), icaoOd), cb.equal(join.get("icaoTo"), icaoDo));

    Query q = em.createQuery(cq);
    return q.getResultList();
  }

  public List<Object[]> findDistances(String icao, int odBroja, int broj) {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
    Root<AirportsDistanceMatrix> rt = cq.from(AirportsDistanceMatrix.class);
    Join<AirportsDistanceMatrix, AirportsDistanceMatrixPK> join = rt.join("id");

    cq.select(cb.array(rt.get("distTot"), join.get("icaoTo")))
        .where(cb.equal(join.get("icaoFrom"), icao)).distinct(true)
        .orderBy(cb.asc(join.get("icaoTo")));

    Query q = em.createQuery(cq);
    q.setMaxResults(broj);
    q.setFirstResult(odBroja);

    return q.getResultList();
  }

  public Object[] findDistances(String icao) {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
    Root<AirportsDistanceMatrix> rt = cq.from(AirportsDistanceMatrix.class);
    Join<AirportsDistanceMatrix, AirportsDistanceMatrixPK> join = rt.join("id");

    cq.select(cb.array(join.get("icaoTo"), join.get("country"), rt.get("distCtry")))
        .where(cb.equal(join.get("icaoFrom"), icao)).orderBy(cb.desc(rt.get("distCtry")));

    Query q = em.createQuery(cq);
    q.setMaxResults(1);

    var result = q.getResultList();
    if (result.isEmpty())
      return null;
    else
      return (Object[]) result.get(0);
  }

  public int count() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<Airports> rt = cq.from(Airports.class);
    cq.select(cb.count(rt));
    Query q = em.createQuery(cq);
    return ((Long) q.getSingleResult()).intValue();
  }
}
