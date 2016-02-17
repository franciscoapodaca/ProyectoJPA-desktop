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
import entidades.Actores;
import entidades.Peliculas;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.table.DefaultTableModel;
import objetosServicio.Fecha;

/**
 *
 * @author Francisco
 */
public class PeliculasJpaController implements Serializable {

    public PeliculasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Peliculas peliculas) throws PreexistingEntityException, Exception {
        if (peliculas.getActoresList() == null) {
            peliculas.setActoresList(new ArrayList<Actores>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Actores> attachedActoresList = new ArrayList<Actores>();
            for (Actores actoresListActoresToAttach : peliculas.getActoresList()) {
                actoresListActoresToAttach = em.getReference(actoresListActoresToAttach.getClass(), actoresListActoresToAttach.getCveActor());
                attachedActoresList.add(actoresListActoresToAttach);
            }
            peliculas.setActoresList(attachedActoresList);
            em.persist(peliculas);
            for (Actores actoresListActores : peliculas.getActoresList()) {
                actoresListActores.getPeliculasList().add(peliculas);
                actoresListActores = em.merge(actoresListActores);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPeliculas(peliculas.getClave()) != null) {
                throw new PreexistingEntityException("Peliculas " + peliculas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
     public void edit(Peliculas pelicula) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            pelicula = em.merge(pelicula);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pelicula.getClave();
                if (findPeliculas(id) == null) {
                    throw new NonexistentEntityException("The pelicula with id " + id + " no longer exists.");
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
            Peliculas peliculas;
            try {
                peliculas = em.getReference(Peliculas.class, id);
                peliculas.getClave();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The peliculas with id " + id + " no longer exists.", enfe);
            }
            List<Actores> actoresList = peliculas.getActoresList();
            for (Actores actoresListActores : actoresList) {
                actoresListActores.getPeliculasList().remove(peliculas);
                actoresListActores = em.merge(actoresListActores);
            }
            em.remove(peliculas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Peliculas> findPeliculasEntities() {
        return findPeliculasEntities(true, -1, -1);
    }

    public List<Peliculas> findPeliculasEntities(int maxResults, int firstResult) {
        return findPeliculasEntities(false, maxResults, firstResult);
    }

    private List<Peliculas> findPeliculasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Peliculas.class));
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

    public Peliculas findPeliculas(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Peliculas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeliculasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Peliculas> rt = cq.from(Peliculas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Peliculas> obtenerTodasPeliculas() throws Exception {
        try {
            return findPeliculasEntities();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void insertarModelo(DefaultTableModel modelo) throws Exception {
        try {
            List<Peliculas> listaPeliculas = obtenerTodasPeliculas();
            if (listaPeliculas != null) {
                int size = listaPeliculas.size();
                for (int i = 0; i < size; i++) {
                    Object[] datos = new Object[3];
                    Peliculas peliculas = listaPeliculas.get(i);
                    datos[0] = peliculas.getClave();
                    datos[1] = peliculas.getDirector();
                    Fecha fecha = new Fecha(new Date(peliculas.getFecha().getYear(), peliculas.getFecha().getMonth(), peliculas.getFecha().getDate()));
                    datos[2] = fecha;
                    modelo.addRow(datos);
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

}
