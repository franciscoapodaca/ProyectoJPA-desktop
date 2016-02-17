/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import controles.exceptions.IllegalOrphanException;
import controles.exceptions.NonexistentEntityException;
import controles.exceptions.PreexistingEntityException;
import entidades.Generos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Medios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Francisco
 */
public class GenerosJpaController implements Serializable {

    public GenerosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Generos generos) throws PreexistingEntityException, Exception {
        if (generos.getMediosList() == null) {
            generos.setMediosList(new ArrayList<Medios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Medios> attachedMediosList = new ArrayList<Medios>();
            for (Medios mediosListMediosToAttach : generos.getMediosList()) {
                mediosListMediosToAttach = em.getReference(mediosListMediosToAttach.getClass(), mediosListMediosToAttach.getClave());
                attachedMediosList.add(mediosListMediosToAttach);
            }
            generos.setMediosList(attachedMediosList);
            em.persist(generos);
            for (Medios mediosListMedios : generos.getMediosList()) {
                Generos oldCveGeneroOfMediosListMedios = mediosListMedios.getCveGenero();
                mediosListMedios.setCveGenero(generos);
                mediosListMedios = em.merge(mediosListMedios);
                if (oldCveGeneroOfMediosListMedios != null) {
                    oldCveGeneroOfMediosListMedios.getMediosList().remove(mediosListMedios);
                    oldCveGeneroOfMediosListMedios = em.merge(oldCveGeneroOfMediosListMedios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGeneros(generos.getCveGenero()) != null) {
                throw new PreexistingEntityException("Generos " + generos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
     public void edit(Generos genero) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            genero = em.merge(genero);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = genero.getCveGenero();
                if (findGeneros(id) == null) {
                    throw new NonexistentEntityException("The genero with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    

//    public void edit(Generos generos) throws IllegalOrphanException, NonexistentEntityException, Exception {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
//            Generos persistentGeneros = em.find(Generos.class, generos.getCveGenero());
//            List<Medios> mediosListOld = persistentGeneros.getMediosList();
//            List<Medios> mediosListNew = generos.getMediosList();
//            List<String> illegalOrphanMessages = null;
//            for (Medios mediosListOldMedios : mediosListOld) {
//                if (!mediosListNew.contains(mediosListOldMedios)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain Medios " + mediosListOldMedios + " since its cveGenero field is not nullable.");
//                }
//            }
//            if (illegalOrphanMessages != null) {
//                throw new IllegalOrphanException(illegalOrphanMessages);
//            }
//            List<Medios> attachedMediosListNew = new ArrayList<Medios>();
//            for (Medios mediosListNewMediosToAttach : mediosListNew) {
//                mediosListNewMediosToAttach = em.getReference(mediosListNewMediosToAttach.getClass(), mediosListNewMediosToAttach.getClave());
//                attachedMediosListNew.add(mediosListNewMediosToAttach);
//            }
//            mediosListNew = attachedMediosListNew;
//            generos.setMediosList(mediosListNew);
//            generos = em.merge(generos);
//            for (Medios mediosListNewMedios : mediosListNew) {
//                if (!mediosListOld.contains(mediosListNewMedios)) {
//                    Generos oldCveGeneroOfMediosListNewMedios = mediosListNewMedios.getCveGenero();
//                    mediosListNewMedios.setCveGenero(generos);
//                    mediosListNewMedios = em.merge(mediosListNewMedios);
//                    if (oldCveGeneroOfMediosListNewMedios != null && !oldCveGeneroOfMediosListNewMedios.equals(generos)) {
//                        oldCveGeneroOfMediosListNewMedios.getMediosList().remove(mediosListNewMedios);
//                        oldCveGeneroOfMediosListNewMedios = em.merge(oldCveGeneroOfMediosListNewMedios);
//                    }
//                }
//            }
//            em.getTransaction().commit();
//        } catch (Exception ex) {
//            String msg = ex.getLocalizedMessage();
//            if (msg == null || msg.length() == 0) {
//                String id = generos.getCveGenero();
//                if (findGeneros(id) == null) {
//                    throw new NonexistentEntityException("The generos with id " + id + " no longer exists.");
//                }
//            }
//            throw ex;
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
//    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Generos generos;
            try {
                generos = em.getReference(Generos.class, id);
                generos.getCveGenero();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The generos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Medios> mediosListOrphanCheck = generos.getMediosList();
            for (Medios mediosListOrphanCheckMedios : mediosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Generos (" + generos + ") cannot be destroyed since the Medios " + mediosListOrphanCheckMedios + " in its mediosList field has a non-nullable cveGenero field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(generos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Generos> findGenerosEntities() {
        return findGenerosEntities(true, -1, -1);
    }

    public List<Generos> findGenerosEntities(int maxResults, int firstResult) {
        return findGenerosEntities(false, maxResults, firstResult);
    }

    private List<Generos> findGenerosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Generos.class));
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

    public Generos findGeneros(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Generos.class, id);
        } finally {
            em.close();
        }
    }

    public int getGenerosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Generos> rt = cq.from(Generos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Generos> obtenerTodosGeneros() throws Exception {
        try {
            return findGenerosEntities();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());

        }
    }

    public void insertarModelo(DefaultTableModel modelo) throws Exception {
        try {
            List<Generos> listaGenero = obtenerTodosGeneros();
            if (listaGenero != null) {
                int size = listaGenero.size();
                for (int i = 0; i < size; i++) {
                    Object[] datos = new Object[3];
                    Generos genero = listaGenero.get(i);
                    datos[0] = genero.getCveGenero();
                    datos[1] = genero.getNombre();
                    datos[2] = genero.getTipoMedio();
                    modelo.addRow(datos);
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

}
