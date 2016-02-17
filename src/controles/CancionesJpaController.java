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
import entidades.Albums;
import entidades.Canciones;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Francisco
 */
public class CancionesJpaController implements Serializable {

    public CancionesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Canciones canciones) throws PreexistingEntityException, Exception {
        if (canciones.getAlbumsList() == null) {
            canciones.setAlbumsList(new ArrayList<Albums>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Albums> attachedAlbumsList = new ArrayList<Albums>();
            for (Albums albumsListAlbumsToAttach : canciones.getAlbumsList()) {
                albumsListAlbumsToAttach = em.getReference(albumsListAlbumsToAttach.getClass(), albumsListAlbumsToAttach.getCveAlbum());
                attachedAlbumsList.add(albumsListAlbumsToAttach);
            }
            canciones.setAlbumsList(attachedAlbumsList);
            em.persist(canciones);
            for (Albums albumsListAlbums : canciones.getAlbumsList()) {
                albumsListAlbums.getCancionesList().add(canciones);
                albumsListAlbums = em.merge(albumsListAlbums);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCanciones(canciones.getClave()) != null) {
                throw new PreexistingEntityException("Canciones " + canciones + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
     public void edit(Canciones cancion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cancion = em.merge(cancion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = cancion.getClave();
                if (findCanciones(id) == null) {
                    throw new NonexistentEntityException("The cancion with id " + id + " no longer exists.");
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
            Canciones canciones;
            try {
                canciones = em.getReference(Canciones.class, id);
                canciones.getClave();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The canciones with id " + id + " no longer exists.", enfe);
            }
            List<Albums> albumsList = canciones.getAlbumsList();
            for (Albums albumsListAlbums : albumsList) {
                albumsListAlbums.getCancionesList().remove(canciones);
                albumsListAlbums = em.merge(albumsListAlbums);
            }
            em.remove(canciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Canciones> findCancionesEntities() {
        return findCancionesEntities(true, -1, -1);
    }

    public List<Canciones> findCancionesEntities(int maxResults, int firstResult) {
        return findCancionesEntities(false, maxResults, firstResult);
    }

    private List<Canciones> findCancionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Canciones.class));
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

    public Canciones findCanciones(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Canciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getCancionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Canciones> rt = cq.from(Canciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void insertarModelo(DefaultTableModel modelo) throws Exception {
        try {
            List<Canciones> listaCanciones = obtenerTodasCanciones();
            if (listaCanciones != null) {
                int size = listaCanciones.size();
                for (int i = 0; i < size; i++) {
                    Object[] datos = new Object[3];
                    Canciones cancion = listaCanciones.get(i);
                    datos[0] = cancion.getClave();
                    datos[1] = cancion.getInterprete();
                    datos[2] = cancion.getAutor();
                    modelo.addRow(datos);
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());

        }
    }

    public List<Canciones> obtenerTodasCanciones() throws Exception {
        try {
            return findCancionesEntities();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

}
