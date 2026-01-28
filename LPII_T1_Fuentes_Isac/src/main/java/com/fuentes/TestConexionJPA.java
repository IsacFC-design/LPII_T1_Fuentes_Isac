package com.fuentes;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestConexionJPA {
    public static void main(String[] args) {
        System.out.println("=== TEST DE CONEXIÓN JPA ===");
        System.out.println("Intentando conectar a BD1_Fuentes...");
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            // 1. Crear EntityManagerFactory usando el nombre de persistence-unit
            emf = Persistence.createEntityManagerFactory("LPII_T1_Fuentes_IsacPU");
            System.out.println("✅ EntityManagerFactory creado exitosamente");
            
            // 2. Crear EntityManager
            em = emf.createEntityManager();
            System.out.println("✅ EntityManager creado exitosamente");
            
            // 3. Probar conexión con una consulta simple
            boolean conexionExitosa = em.createNativeQuery("SELECT 1").getSingleResult() != null;
            
            if (conexionExitosa) {
                System.out.println("✅ Conexión a MySQL establecida correctamente");
                System.out.println("✅ Base de datos: BD1_Fuentes");
                System.out.println("✅ Persistence unit: LPII_T1_Fuentes_IsacPU");
            }
            
        } catch (Exception e) {
            System.err.println("❌ ERROR DE CONEXIÓN:");
            e.printStackTrace();
            
            // Mensajes comunes de error
            if (e.getMessage().contains("Access denied")) {
                System.err.println("\nPROBLEMA: Credenciales incorrectas");
                System.err.println("SOLUCIÓN: Verifica usuario/contraseña en persistence.xml");
            } else if (e.getMessage().contains("Unknown database")) {
                System.err.println("\nPROBLEMA: Base de datos no existe");
                System.err.println("SOLUCIÓN: Ejecuta el script BD1_Fuentes.sql en MySQL");
            } else if (e.getMessage().contains("Communications link failure")) {
                System.err.println("\nPROBLEMA: MySQL no está corriendo");
                System.err.println("SOLUCIÓN: Inicia el servicio MySQL");
            }
            
        } finally {
            // 4. Cerrar recursos
            if (em != null && em.isOpen()) {
                em.close();
                System.out.println("EntityManager cerrado");
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
                System.out.println("EntityManagerFactory cerrado");
            }
        }
        
        System.out.println("=== FIN DEL TEST ===");
    }
}