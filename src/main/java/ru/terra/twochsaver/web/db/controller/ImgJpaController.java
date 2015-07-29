package ru.terra.twochsaver.web.db.controller;

import ru.terra.server.db.controllers.AbstractJpaController;
import ru.terra.twochsaver.web.db.controller.exceptions.NonexistentEntityException;
import ru.terra.twochsaver.web.db.controller.exceptions.PreexistingEntityException;
import ru.terra.twochsaver.web.db.entity.Img;
import ru.terra.twochsaver.web.db.entity.Thr;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author terranz
 */
public class ImgJpaController extends AbstractJpaController<Img> {

    public ImgJpaController() {
        super(Img.class);
    }

    public void create(Img img) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Thr thrId = img.getThrId();
            if (thrId != null) {
                thrId = em.getReference(thrId.getClass(), thrId.getId());
                img.setThrId(thrId);
            }
            em.persist(img);
            if (thrId != null) {
                thrId.getImgList().add(img);
                thrId = em.merge(thrId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findImg(img.getUrl()) != null) {
                throw new PreexistingEntityException("Img " + img + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Integer id) throws Exception {

    }

    public void update(Img img) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Img persistentImg = em.find(Img.class, img.getUrl());
            Thr thrIdOld = persistentImg.getThrId();
            Thr thrIdNew = img.getThrId();
            if (thrIdNew != null) {
                thrIdNew = em.getReference(thrIdNew.getClass(), thrIdNew.getId());
                img.setThrId(thrIdNew);
            }
            img = em.merge(img);
            if (thrIdOld != null && !thrIdOld.equals(thrIdNew)) {
                thrIdOld.getImgList().remove(img);
                thrIdOld = em.merge(thrIdOld);
            }
            if (thrIdNew != null && !thrIdNew.equals(thrIdOld)) {
                thrIdNew.getImgList().add(img);
                thrIdNew = em.merge(thrIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = img.getUrl();
                if (findImg(id) == null) {
                    throw new NonexistentEntityException("The img with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void delete(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Img img;
            try {
                img = em.getReference(Img.class, id);
                img.getUrl();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The img with id " + id + " no longer exists.", enfe);
            }
            Thr thrId = img.getThrId();
            if (thrId != null) {
                thrId.getImgList().remove(img);
                thrId = em.merge(thrId);
            }
            em.remove(img);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Img> findImgEntities() {
        return findImgEntities(true, -1, -1);
    }

    public List<Img> findImgEntities(int maxResults, int firstResult) {
        return findImgEntities(false, maxResults, firstResult);
    }

    private List<Img> findImgEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Img.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Img findImg(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Img.class, id);
        } finally {
            em.close();
        }
    }

    public int getImgCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Img> rt = cq.from(Img.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public int getImagesCountForThread(Thr thr) {
        EntityManager em = getEntityManager();
        try {
            return ((Number) em.createNamedQuery("Img.countForThr").setParameter("thr", thr).getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
