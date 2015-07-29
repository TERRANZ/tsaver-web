package ru.terra.twochsaver.web.db.controller;

import ru.terra.server.db.controllers.AbstractJpaController;
import ru.terra.twochsaver.web.db.controller.exceptions.IllegalOrphanException;
import ru.terra.twochsaver.web.db.controller.exceptions.NonexistentEntityException;
import ru.terra.twochsaver.web.db.entity.Img;
import ru.terra.twochsaver.web.db.entity.Thr;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author terranz
 */
public class ThrJpaController extends AbstractJpaController<Thr> {

    public ThrJpaController() {
        super(Thr.class);
    }

    public void create(Thr thr) {
        if (thr.getImgList() == null) {
            thr.setImgList(new ArrayList<Img>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Img> attachedImgList = new ArrayList<Img>();
            for (Img imgListImgToAttach : thr.getImgList()) {
                imgListImgToAttach = em.getReference(imgListImgToAttach.getClass(), imgListImgToAttach.getUrl());
                attachedImgList.add(imgListImgToAttach);
            }
            thr.setImgList(attachedImgList);
            em.persist(thr);
            for (Img imgListImg : thr.getImgList()) {
                Thr oldThrIdOfImgListImg = imgListImg.getThrId();
                imgListImg.setThrId(thr);
                imgListImg = em.merge(imgListImg);
                if (oldThrIdOfImgListImg != null) {
                    oldThrIdOfImgListImg.getImgList().remove(imgListImg);
                    oldThrIdOfImgListImg = em.merge(oldThrIdOfImgListImg);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void update(Thr thr) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Thr persistentThr = em.find(Thr.class, thr.getId());
            List<Img> imgListOld = persistentThr.getImgList();
            List<Img> imgListNew = thr.getImgList();
            List<String> illegalOrphanMessages = null;
            for (Img imgListOldImg : imgListOld) {
                if (!imgListNew.contains(imgListOldImg)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Img " + imgListOldImg + " since its thrId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Img> attachedImgListNew = new ArrayList<Img>();
            for (Img imgListNewImgToAttach : imgListNew) {
                imgListNewImgToAttach = em.getReference(imgListNewImgToAttach.getClass(), imgListNewImgToAttach.getUrl());
                attachedImgListNew.add(imgListNewImgToAttach);
            }
            imgListNew = attachedImgListNew;
            thr.setImgList(imgListNew);
            thr.setUpdated(new Date());
            thr = em.merge(thr);
            for (Img imgListNewImg : imgListNew) {
                if (!imgListOld.contains(imgListNewImg)) {
                    Thr oldThrIdOfImgListNewImg = imgListNewImg.getThrId();
                    imgListNewImg.setThrId(thr);
                    imgListNewImg = em.merge(imgListNewImg);
                    if (oldThrIdOfImgListNewImg != null && !oldThrIdOfImgListNewImg.equals(thr)) {
                        oldThrIdOfImgListNewImg.getImgList().remove(imgListNewImg);
                        oldThrIdOfImgListNewImg = em.merge(oldThrIdOfImgListNewImg);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = thr.getId();
                if (findThr(id) == null) {
                    throw new NonexistentEntityException("The thr with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void delete(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Thr thr;
            try {
                thr = em.getReference(Thr.class, id);
                thr.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The thr with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Img> imgListOrphanCheck = thr.getImgList();
            for (Img imgListOrphanCheckImg : imgListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Thr (" + thr + ") cannot be destroyed since the Img " + imgListOrphanCheckImg + " in its imgList field has a non-nullable thrId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(thr);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Thr> findThrEntities() {
        return findThrEntities(true, -1, -1);
    }

    public List<Thr> findUnfinished() {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("Thr.findUnFinished", Thr.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Thr> findThrEntities(int maxResults, int firstResult) {
        return findThrEntities(false, maxResults, firstResult);
    }

    private List<Thr> findThrEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Thr.class));
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

    public Thr findThr(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Thr.class, id);
        } finally {
            em.close();
        }
    }

    public int getThrCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Thr> rt = cq.from(Thr.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Thr findByUrl(String url) {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("Thr.findByUrl", Thr.class).setParameter("url", url).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
