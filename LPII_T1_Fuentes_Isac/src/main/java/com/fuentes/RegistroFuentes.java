package com.fuentes;

import com.fuentes.entity.*;
import javax.persistence.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RegistroFuentes extends JFrame {
    
    // ========== COMPONENTES DE LA INTERFAZ ==========
    private JComboBox<Producto> comboProductos;
    private JTextField txtCosto, txtMotivo;
    private JLabel lblFecha;
    private JButton btnRegistrar, btnLimpiar, btnSalir;
    
    // ========== OBJETOS JPA ==========
    private EntityManagerFactory emf;
    private EntityManager em;
    
    // ========== CONSTRUCTOR ==========
    public RegistroFuentes() {
        super("Registro de Inventario - Distribuidora Multinacional");
        inicializarConexion();
        inicializarComponentes();
        cargarProductos();
    }
    
    // ========== MÉTODO MAIN ==========
    public static void main(String[] args) {
        // Ejecutar en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            RegistroFuentes ventana = new RegistroFuentes();
            ventana.setVisible(true);
        });
    }
    
    // ========== INICIALIZAR CONEXIÓN JPA ==========
    private void inicializarConexion() {
        try {
            emf = Persistence.createEntityManagerFactory("LPII_T1_Fuentes_IsacPU");
            em = emf.createEntityManager();
            System.out.println("Conexión a BD establecida");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al conectar con la base de datos:\n" + e.getMessage(), 
                "Error de Conexión", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    // ========== INICIALIZAR COMPONENTES GUI ==========
    private void inicializarComponentes() {
        // Configurar ventana principal
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null); // Centrar ventana
        setLayout(new BorderLayout(10, 10));
        
        // Panel principal con márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // ========== PANEL SUPERIOR: TÍTULO ==========
        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel titulo = new JLabel("REGISTRO DE INVENTARIO");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(new Color(0, 70, 140));
        panelTitulo.add(titulo);
        
        // ========== PANEL CENTRAL: FORMULARIO ==========
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Datos del Ingreso al Inventario"
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Fila 0: Fecha actual
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Fecha del Sistema:"), gbc);
        
        gbc.gridx = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        lblFecha = new JLabel(sdf.format(new Date()));
        lblFecha.setForeground(Color.BLUE);
        panelFormulario.add(lblFecha, gbc);
        
        // Fila 1: Producto
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Producto (*):"), gbc);
        
        gbc.gridx = 1;
        comboProductos = new JComboBox<>();
        comboProductos.setRenderer(new ProductoRenderer());
        panelFormulario.add(comboProductos, gbc);
        
        // Fila 2: Costo de Ingreso
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Costo de Ingreso (*):"), gbc);
        
        gbc.gridx = 1;
        txtCosto = new JTextField(15);
        panelFormulario.add(txtCosto, gbc);
        
        // Fila 3: Motivo
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Motivo de Ingreso (*):"), gbc);
        
        gbc.gridx = 1;
        txtMotivo = new JTextField(15);
        panelFormulario.add(txtMotivo, gbc);
        
        // ========== PANEL INFERIOR: BOTONES ==========
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
     // --- BOTÓN REGISTRAR (Verde Esmeralda) ---
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBackground(new Color(46, 204, 113)); 
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        btnRegistrar.setFocusPainted(false); 
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnRegistrar.setPreferredSize(new Dimension(110, 35));

        // --- BOTÓN LIMPIAR (Naranja Suave) ---
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(new Color(243, 156, 18));
        btnLimpiar.setForeground(Color.BLACK);
        btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setPreferredSize(new Dimension(110, 35));

        // --- BOTÓN SALIR (Rojo Coral) ---
        btnSalir = new JButton("Salir");
        btnSalir.setBackground(new Color(231, 76, 60));
        btnSalir.setForeground(Color.BLACK);
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSalir.setFocusPainted(false);
        btnSalir.setPreferredSize(new Dimension(110, 35));
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnSalir);
        
        // ========== AGREGAR PANELES A VENTANA ==========
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        // ========== CONFIGURAR EVENTOS ==========
        configurarEventos();
    }
    
    // ========== CARGAR PRODUCTOS EN COMBOBOX ==========
    private void cargarProductos() {
        try {
            Query query = em.createQuery("SELECT p FROM Producto p ORDER BY p.nomProd");
            List<Producto> productos = query.getResultList();
            
            comboProductos.removeAllItems();
            
            if (productos.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No hay productos registrados.\nDebe registrar productos primero.", 
                    "Advertencia", 
                    JOptionPane.WARNING_MESSAGE);
            } else {
                for (Producto producto : productos) {
                    comboProductos.addItem(producto);
                }
                comboProductos.setSelectedIndex(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar productos: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ========== CONFIGURAR EVENTOS DE BOTONES ==========
    private void configurarEventos() {
        // Botón Registrar
        btnRegistrar.addActionListener(e -> registrarInventario());
        
        // Botón Limpiar
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        
        // Botón Salir
        btnSalir.addActionListener(e -> {
            cerrarConexion();
            System.exit(0);
        });
        
        // Tecla Enter en campos de texto
        txtCosto.addActionListener(e -> registrarInventario());
        txtMotivo.addActionListener(e -> registrarInventario());
        
        // Actualizar fecha cada minuto
        Timer timer = new Timer(60000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            lblFecha.setText(sdf.format(new Date()));
        });
        timer.start();
    }
    
    // ========== REGISTRAR INVENTARIO ==========
    private void registrarInventario() {
        // Validar campos
        if (!validarCampos()) {
            return;
        }
        
        try {
            // Crear nuevo inventario
            Inventario inventario = new Inventario();
            inventario.setProducto((Producto) comboProductos.getSelectedItem());
            inventario.setCostoIngreso(Double.parseDouble(txtCosto.getText()));
            inventario.setMotivoIngreso(txtMotivo.getText());
            
            // Guardar en BD
            em.getTransaction().begin();
            em.persist(inventario);
            em.getTransaction().commit();
            
            // Mostrar mensaje de éxito
            String mensaje = String.format(
                "✅ REGISTRO EXITOSO\n\n" +
                "Número de Inventario: %d\n" +
                "Producto: %s\n" +
                "Costo: S/. %.2f\n" +
                "Fecha: %s",
                inventario.getNroInventario(),
                inventario.getProducto().getNomProd(),
                inventario.getCostoIngreso(),
                new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())
            );
            
            JOptionPane.showMessageDialog(this, 
                mensaje, 
                "Registro Completado", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Limpiar formulario después de registro exitoso
            limpiarFormulario();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Error: El costo debe ser un número válido.\nEjemplo: 1250.50", 
                "Error de Formato", 
                JOptionPane.ERROR_MESSAGE);
            txtCosto.requestFocus();
            txtCosto.selectAll();
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            
            JOptionPane.showMessageDialog(this, 
                "❌ ERROR AL REGISTRAR\n" + e.getMessage(), 
                "Error de Registro", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // ========== VALIDAR CAMPOS DEL FORMULARIO ==========
    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();
        
        // Validar producto seleccionado
        if (comboProductos.getSelectedItem() == null) {
            errores.append("- Debe seleccionar un producto\n");
        }
        
        // Validar costo
        String costoStr = txtCosto.getText().trim();
        if (costoStr.isEmpty()) {
            errores.append("- El costo es obligatorio\n");
        } else {
            try {
                double costo = Double.parseDouble(costoStr);
                if (costo <= 0) {
                    errores.append("- El costo debe ser mayor a 0\n");
                }
            } catch (NumberFormatException e) {
                errores.append("- El costo debe ser un número válido\n");
            }
        }
        
        // Validar motivo
        String motivo = txtMotivo.getText().trim();
        if (motivo.isEmpty()) {
            errores.append("- El motivo es obligatorio\n");
        } else if (motivo.length() < 5) {
            errores.append("- El motivo debe tener al menos 5 caracteres\n");
        }
        
        // Si hay errores, mostrar mensaje
        if (errores.length() > 0) {
            JOptionPane.showMessageDialog(this, 
                "Por favor corrija los siguientes errores:\n\n" + errores.toString(), 
                "Validación de Datos", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    // ========== LIMPIAR FORMULARIO ==========
    private void limpiarFormulario() {
        if (comboProductos.getItemCount() > 0) {
            comboProductos.setSelectedIndex(0);
        }
        txtCosto.setText("");
        txtMotivo.setText("");
        txtCosto.requestFocus();
    }
    
    // ========== CERRAR CONEXIÓN ==========
    private void cerrarConexion() {
        try {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
            System.out.println("Conexiones cerradas");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // ========== RENDERER PERSONALIZADO PARA COMBOBOX ==========
    private class ProductoRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Producto) {
                Producto producto = (Producto) value;
                setText(producto.getNomProd() + " - Stock: " + producto.getStockActual() + 
                       " - Cat: " + producto.getCategoria().getDescripcion());
            }
            
            return this;
        }
    }
}
