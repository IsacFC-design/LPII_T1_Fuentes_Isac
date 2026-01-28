package com.fuentes;

public class TestRegistro {
    public static void main(String[] args) {
        System.out.println("=== INSTRUCCIONES DE USO ===");
        System.out.println("\n1. Ejecutar RegistroFuentes.java:");
        System.out.println("   - Click derecho → Run As → Java Application");
        
        System.out.println("\n2. Flujo de trabajo:");
        System.out.println("   a. Seleccionar producto del combo");
        System.out.println("   b. Ingresar costo (ej: 1250.50)");
        System.out.println("   c. Ingresar motivo (mínimo 5 caracteres)");
        System.out.println("   d. Click en 'Registrar' o presionar Enter");
        
        System.out.println("\n3. Validaciones automáticas:");
        System.out.println("   - Producto requerido");
        System.out.println("   - Costo debe ser número > 0");
        System.out.println("   - Motivo mínimo 5 caracteres");
        
        System.out.println("\n4. Mensajes:");
        System.out.println("   - ✅ Éxito: Muestra número de inventario generado");
        System.out.println("   - ❌ Error: Describe el problema encontrado");
        
        System.out.println("\n=== DATOS DE PRUEBA ===");
        System.out.println("Productos disponibles en BD:");
        System.out.println("1. Laptop HP Pavilion (Stock: 10)");
        System.out.println("2. Camiseta de Algodón M (Stock: 50)");
        System.out.println("3. Arroz Extra 5kg (Stock: 100)");
        
        System.out.println("\nEjemplos de registro válido:");
        System.out.println("Costo: 2500.00 | Motivo: Reposición de stock semanal");
        System.out.println("Costo: 45.75   | Motivo: Compra por oferta especial");
        
        System.out.println("\n⚠ Presiona Enter para abrir el registro...");
        try {
            System.in.read();
            RegistroFuentes.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}