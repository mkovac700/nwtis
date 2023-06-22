package org.foi.nwtis.mkovac.aplikacija_4.zrna;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.mkovac.aplikacija_4.jpa.Korisnici;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * Facade klasa za korisnike
 * 
 * @author Marijan Kovač
 */
@Stateless
public class KorisniciFacade {
  @PersistenceContext(unitName = "nwtis_dz3_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    System.out.println("KorisniciFacade- init");
    cb = em.getCriteriaBuilder();
  }

  public void create(Korisnici korisnik) {
    em.persist(korisnik);
  }

  public void edit(Korisnici korisnik) {
    em.merge(korisnik);
  }

  public void remove(Korisnici korisnik) {
    em.remove(em.merge(korisnik));
  }

  public Korisnici find(Object id) {
    return em.find(Korisnici.class, id);
  }

  public Korisnici findOne(String korisnik) throws NoResultException {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Korisnici> cq = cb.createQuery(Korisnici.class);
    Root<Korisnici> rt = cq.from(Korisnici.class);

    cq.where(cb.equal(rt.get("korisnik"), korisnik));

    return em.createQuery(cq).getSingleResult();
  }

  public List<Korisnici> findAll() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Korisnici> cq = cb.createQuery(Korisnici.class);
    cq.select(cq.from(Korisnici.class));
    return em.createQuery(cq).getResultList();
  }

  public List<Korisnici> findAll(String traziImeKorisnika, String traziPrezimeKorisnika) {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Korisnici> cq = cb.createQuery(Korisnici.class);
    Root<Korisnici> rt = cq.from(Korisnici.class);

    List<Predicate> predicates = new ArrayList<>();

    if (traziImeKorisnika != null && !traziImeKorisnika.trim().isEmpty())
      predicates.add(cb.like(cb.lower(rt.get("ime")), "%" + traziImeKorisnika.toLowerCase() + "%"));

    if (traziPrezimeKorisnika != null && !traziPrezimeKorisnika.trim().isEmpty())
      predicates.add(
          cb.like(cb.lower(rt.get("prezime")), "%" + traziPrezimeKorisnika.toLowerCase() + "%"));

    if (!predicates.isEmpty())
      cq.where(predicates.toArray(new Predicate[0]));

    TypedQuery<Korisnici> q = em.createQuery(cq);

    return q.getResultList();
  }
}
