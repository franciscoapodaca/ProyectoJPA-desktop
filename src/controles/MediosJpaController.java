/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import controles.exceptions.NonexistentEntityException;
import controles.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Generos;
import entidades.Medios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Francisco
 */
public class MediosJpaController implements Serializable {

    public MediosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
     public void create(Medios medio) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(medio);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMedios(medio.getClave()) != null) {
                throw new PreexistingEntityException("Medio " + medio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
      public void edit(Medios medio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            medio = em.merge(medio);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = medio.getClave();
                if (findMedios(id) == null) {
                    throw new NonexistentEntityException("The medio with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
//
// 
//    public void edit(Medios medio) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
//            medio = em.merge(medio);
//            em.getTransaction().commit();
//        } catch (Exception ex) {
//            String msg = ex.getLocalizedMessage();
//            if (msg == null || msg.length() == 0) {
//                String id = medio.getClave();
//                if (findMedios(id) == null) {
//                    throw new NonexistentEntityException("The medio with id " + id + " no longer exists.");
//                }
//            }
//            throw ex;
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
//    }
    
     public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medios medio;
            try {
                medio = em.getReference(Medios.class, id);
                medio.getClave();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medio with id " + id + " no longer exists.", enfe);
            }
            em.remove(medio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }



//    public void destroy(String id) throws NonexistentEntityException {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
//            Medios medios;
//            try {
//                medios = em.getReference(Medios.class, id);
//                medios.getClave();
//            } catch (EntityNotFoundException enfe) {
//                throw new NonexistentEntityException("The medios with id " + id + " no longer exists.", enfe);
//            }
//            Generos cveGenero = medios.getCveGenero();
//            if (cveGenero != null) {
//                cveGenero.getMediosList().remove(medios);
//                cveGenero = em.merge(cveGenero);
//            }
//            em.remove(medios);
//            em.getTransaction().commit();
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
//    }

    public List<Medios> findMediosEntities() {
        return findMediosEntities(true, -1, -1);
    }

    public List<Medios> findMediosEntities(int maxResults, int firstResult) {
        return findMediosEntities(false, maxResults, firstResult);
    }

    private List<Medios> findMediosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medios.class));
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

    public Medios findMedios(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medios.class, id);
        } finally {
            em.close();
        }
    }

    public int getMediosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medios> rt = cq.from(Medios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Medios> obtenerTodosMedios() throws Exception {
        try {
            return findMediosEntities();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void insertarModelo(DefaultTableModel modelo) throws Exception {
        try {
            List<Medios> listaMedios = obtenerTodosMedios();
            if (listaMedios != null) {
                int size = listaMedios.size();
                for (int i = 0; i < size; i++) {
                    Object[] datos = new Object[5];
                    Medios medios = listaMedios.get(i);
                    datos[0] = medios.getClave();
                    datos[1] = medios.getTitutlo();
                    datos[2] = medios.getTipoMedio();
                    datos[3] = medios.getDuracion();
                    datos[4] = medios.getCveGenero();
                    modelo.addRow(datos);
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

}
