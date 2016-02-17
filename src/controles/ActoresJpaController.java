/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import controles.exceptions.NonexistentEntityException;
import controles.exceptions.PreexistingEntityException;
import entidades.Actores;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Peliculas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Francisco
 */
public class ActoresJpaController implements Serializable {

    public ActoresJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Actores actores) throws PreexistingEntityException, Exception {
        if (actores.getPeliculasList()== null) {
            actores.setPeliculasList(new ArrayList<Peliculas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Peliculas> attachedPeliculassList = new ArrayList<Peliculas>();
            for (Peliculas peliculasListPeliculassToAttach : actores.getPeliculasList()) {
                peliculasListPeliculassToAttach = em.getReference(peliculasListPeliculassToAttach.getClass(), peliculasListPeliculassToAttach.getClave());
                attachedPeliculassList.add(peliculasListPeliculassToAttach);
            }
            actores.setPeliculasList(attachedPeliculassList);
            em.persist(actores);
            for (Peliculas peliculasListPeliculass : actores.getPeliculasList()) {
                peliculasListPeliculass.getActoresList().add(actores);
                peliculasListPeliculass = em.merge(peliculasListPeliculass);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findActores(actores.getCveActor()) != null) {
                throw new PreexistingEntityException("Actores " + actores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public void edit(Actores actor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actores persistentActores = em.find(Actores.class, actor.getCveActor());
            List<Peliculas> listaPeliculassOld = persistentActores.getPeliculasList();
            List<Peliculas> listaPeliculassNew = actor.getPeliculasList();
            List<Peliculas> attachedListaPeliculassNew = new ArrayList<Peliculas>();
            if (listaPeliculassNew != null) {
                for (Peliculas listaPeliculassNewPeliculasToAttach : listaPeliculassNew) {
                    listaPeliculassNewPeliculasToAttach = em.getReference(listaPeliculassNewPeliculasToAttach.getClass(), listaPeliculassNewPeliculasToAttach.getClave());
                    attachedListaPeliculassNew.add(listaPeliculassNewPeliculasToAttach);
                }
            }
            listaPeliculassNew = attachedListaPeliculassNew;
            actor.setPeliculasList(listaPeliculassNew);
            actor = em.merge(actor);
            for (Peliculas listaPeliculassOldPeliculas : listaPeliculassOld) {
                if (!listaPeliculassNew.contains(listaPeliculassOldPeliculas)) {
                    listaPeliculassOldPeliculas.getActoresList().remove(actor);
                    listaPeliculassOldPeliculas = em.merge(listaPeliculassOldPeliculas);
                }
            }
            for (Peliculas listaPeliculassNewPeliculas : listaPeliculassNew) {
                if (!listaPeliculassOld.contains(listaPeliculassNewPeliculas)) {
                    listaPeliculassNewPeliculas.getActoresList().add(actor);
                    listaPeliculassNewPeliculas = em.merge(listaPeliculassNewPeliculas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = actor.getCveActor();
                if (findActores(id) == null) {
                    throw new NonexistentEntityException("The actor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actores actores;
            try {
                actores = em.getReference(Actores.class, id);
                actores.getCveActor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The actores with id " + id + " no longer exists.", enfe);
            }
            List<Peliculas> peliculasList = actores.getPeliculasList();
            for (Peliculas peliculasListPeliculass : peliculasList) {
                peliculasListPeliculass.getActoresList().remove(actores);
                peliculasListPeliculass = em.merge(peliculasListPeliculass);
            }
            em.remove(actores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Actores> findActoresEntities() {
        return findActoresEntities(true, -1, -1);
    }

    public List<Actores> findActoresEntities(int maxResults, int firstResult) {
        return findActoresEntities(false, maxResults, firstResult);
    }

    private List<Actores> findActoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Actores.class));
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

    public Actores findActores(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Actores.class, id);
        } finally {
            em.close();
        }
    }

    public int getActoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Actores> rt = cq.from(Actores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void insertarModelo(DefaultTableModel modelo) throws Exception {
        try {
            List<Actores> listaActores = obtenerTodosActores();
            if (listaActores != null) {
                int size = listaActores.size();
                for (int i = 0; i < size; i++) {
                    Object[] datos = new Object[2];
                    Actores actores = listaActores.get(i);
                    datos[0] = actores.getCveActor();
                    datos[1] = actores.getNombre();

                    modelo.addRow(datos);
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public List<Actores> obtenerTodosActores() throws Exception {

        try {

            return findActoresEntities();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
