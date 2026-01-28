package com.fuentes;

import com.fuentes.entity.*;
import javax.persistence.*;
import java.util.List;

public class TestEntidades {
    public static void main(String[] args) {
        System.out.println("=== TEST DE ENTIDADES JPA ===");
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            // 1. Crear EntityManager
            emf = Persistence.createEntityManagerFactory("LPII_T1_Fuentes_IsacPU");
            em = emf.createEntityManager();
            
            System.out.println("✅ Conexión establecida");
            
            // ========== TEST 1: Consultar Categorías ==========
            System.out.println("\n--- CATEGORÍAS ---");
            Query queryCategorias = em.createQuery("SELECT c FROM Categoria c");
            List<Categoria> categorias = queryCategorias.getResultList();
            
            for (Categoria c : categorias) {
                System.out.println(c);
                System.out.println("  Frecuencia: " + c.getFrecuenciaCompra());
            }
            
            // ========== TEST 2: Consultar Productos ==========
            System.out.println("\n--- PRODUCTOS ---");
            Query queryProductos = em.createQuery("SELECT p FROM Producto p");
            List<Producto> productos = queryProductos.getResultList();
            
            for (Producto p : productos) {
                System.out.println(p);
                System.out.println("  Categoría: " + p.getCategoria().getDescripcion());
            }
            
            // ========== TEST 3: Consultar Inventarios ==========
            System.out.println("\n--- INVENTARIOS ---");
            Query queryInventarios = em.createQuery("SELECT i FROM Inventario i");
            List<Inventario> inventarios = queryInventarios.getResultList();
            
            for (Inventario i : inventarios) {
                System.out.println(i);
                System.out.println("  Motivo: " + i.getMotivoIngreso());
            }
            
            // ========== TEST 4: Insertar nueva categoría ==========
            System.out.println("\n--- INSERTAR NUEVA CATEGORÍA ---");
            em.getTransaction().begin();
            
            Categoria nuevaCategoria = new Categoria();
            nuevaCategoria.setDescripcion("Librería");
            nuevaCategoria.setFrecuenciaCompra(Categoria.FrecuenciaCompra.Por_Stock);
            
            em.persist(nuevaCategoria);
            em.getTransaction().commit();
            
            System.out.println("✅ Nueva categoría insertada: " + nuevaCategoria.getIdCate());
            
            // ========== TEST 5: Contar registros ==========
            System.out.println("\n--- RESUMEN ---");
            long countCategorias = (long) em.createQuery("SELECT COUNT(c) FROM Categoria c").getSingleResult();
            long countProductos = (long) em.createQuery("SELECT COUNT(p) FROM Producto p").getSingleResult();
            long countInventarios = (long) em.createQuery("SELECT COUNT(i) FROM Inventario i").getSingleResult();
            
            System.out.println("Total Categorías: " + countCategorias);
            System.out.println("Total Productos: " + countProductos);
            System.out.println("Total Inventarios: " + countInventarios);
            
            System.out.println("\n✅ TODAS LAS ENTIDADES FUNCIONAN CORRECTAMENTE");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR:");
            e.printStackTrace();
            
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                System.out.println("Transacción revertida");
            }
            
        } finally {
            // Cerrar recursos
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
            System.out.println("\nRecursos liberados");
        }
        
        System.out.println("=== FIN DEL TEST ===");
    }
}
