/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import controles.exceptions.NonexistentEntityException;
import controles.exceptions.PreexistingEntityException;
import entidades.Albums;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Canciones;
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
public class AlbumssJpaController implements Serializable {

    public AlbumssJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Albums albums) throws PreexistingEntityException, Exception {
        if (albums.getCancionesList() == null) {
            albums.setCancionesList(new ArrayList<Canciones>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Canciones> attachedCancionesList = new ArrayList<Canciones>();
            for (Canciones cancionesListCancionesToAttach : albums.getCancionesList()) {
                cancionesListCancionesToAttach = em.getReference(cancionesListCancionesToAttach.getClass(), cancionesListCancionesToAttach.getClave());
                attachedCancionesList.add(cancionesListCancionesToAttach);
            }
            albums.setCancionesList(attachedCancionesList);
            em.persist(albums);
            for (Canciones cancionesListCanciones : albums.getCancionesList()) {
                cancionesListCanciones.getAlbumsList().add(albums);
                cancionesListCanciones = em.merge(cancionesListCanciones);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAlbumss(albums.getCveAlbum()) != null) {
                throw new PreexistingEntityException("Albumss " + albums + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

   public void edit(Albums album) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Albums persistentAlbums = em.find(Albums.class, album.getCveAlbum());
            List<Canciones> listaCancionesOld = persistentAlbums.getCancionesList();
            List<Canciones> listaCancionesNew = album.getCancionesList();
            List<Canciones> attachedListaCancionesNew = new ArrayList<Canciones>();
       
            if (listaCancionesNew != null) {
                for (Canciones listaCancionesNewCancionToAttach : listaCancionesNew) {
                    listaCancionesNewCancionToAttach = em.getReference(listaCancionesNewCancionToAttach.getClass(), listaCancionesNewCancionToAttach.getClave());
                    attachedListaCancionesNew.add(listaCancionesNewCancionToAttach);
                }
            }
            listaCancionesNew = attachedListaCancionesNew;
            album.setCancionesList(listaCancionesNew);
            album = em.merge(album);
            for (Canciones listaCancionesOldCancion : listaCancionesOld) {
                if (!listaCancionesNew.contains(listaCancionesOldCancion)) {
                    listaCancionesOldCancion.getAlbumsList().remove(album);
                    listaCancionesOldCancion = em.merge(listaCancionesOldCancion);
                }
            }
            for (Canciones listaCancionesNewCancion : listaCancionesNew) {
                if (!listaCancionesOld.contains(listaCancionesNewCancion)) {
                    listaCancionesNewCancion.getAlbumsList().add(album);
                    listaCancionesNewCancion = em.merge(listaCancionesNewCancion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = album.getCveAlbum();
                if (findAlbumss(id) == null) {
                    throw new NonexistentEntityException("The album with id " + id + " no longer exists.");
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
            Albums albums;
            try {
                albums = em.getReference(Albums.class, id);
                albums.getCveAlbum();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The albums with id " + id + " no longer exists.", enfe);
            }
            List<Canciones> cancionesList = albums.getCancionesList();
            for (Canciones cancionesListCanciones : cancionesList) {
                cancionesListCanciones.getAlbumsList().remove(albums);
                cancionesListCanciones = em.merge(cancionesListCanciones);
            }
            em.remove(albums);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Albums> findAlbumssEntities() {
        return findAlbumssEntities(true, -1, -1);
    }

    public List<Albums> findAlbumssEntities(int maxResults, int firstResult) {
        return findAlbumssEntities(false, maxResults, firstResult);
    }

    private List<Albums> findAlbumssEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Albums.class));
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

    public Albums findAlbumss(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Albums.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlbumssCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Albums> rt = cq.from(Albums.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void insertarModelo(DefaultTableModel modelo) throws Exception {
        try {
            List<Albums> listaAlbums = obtenerTodosAlbumss();
            if (listaAlbums != null) {
                int size = listaAlbums.size();
                for (int i = 0; i < size; i++) {
                    Object[] datos = new Object[4];
                    Albums albums = listaAlbums.get(i);
                    datos[0] = albums.getCveAlbum();
                    datos[1] = albums.getTitulo();
                    datos[2] = albums.getDisquera();
                    Fecha fecha = new Fecha(new Date(albums.getFecha().getYear(), albums.getFecha().getMonth(), albums.getFecha().getDate()));
                    datos[3] = fecha;
                    modelo.addRow(datos);
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public List<Albums> obtenerTodosAlbumss() throws Exception {
        try {
            return findAlbumssEntities();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

}
