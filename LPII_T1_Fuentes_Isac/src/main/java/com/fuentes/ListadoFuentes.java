package com.fuentes;

import com.fuentes.entity.Inventario;
import javax.persistence.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ListadoFuentes extends JFrame {
    
    // ========== COMPONENTES DE LA INTERFAZ ==========
    private JTable tablaInventarios;
    private DefaultTableModel modeloTabla;
    private JButton btnActualizar, btnExportar, btnSalir;
    private JLabel lblContador, lblFechaActual;
    
    // ========== OBJETOS JPA ==========
    private EntityManagerFactory emf;
    private EntityManager em;
    
    // ========== CONSTRUCTOR ==========
    public ListadoFuentes() {
        super("Listado de Inventarios - Distribuidora Multinacional");
        inicializarConexion();
        inicializarComponentes();
        cargarInventarios();
    }
    
    // ========== MÉTODO MAIN ==========
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            ListadoFuentes ventana = new ListadoFuentes();
            ventana.setVisible(true);
        });
    }
    
    // ========== INICIALIZAR CONEXIÓN JPA ==========
    private void inicializarConexion() {
        try {
            emf = Persistence.createEntityManagerFactory("LPII_T1_Fuentes_IsacPU");
            em = emf.createEntityManager();
            System.out.println("Conexión a BD establecida para listado");
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
        setSize(900, 600);
        setLocationRelativeTo(null); // Centrar ventana
        setLayout(new BorderLayout(10, 10));
        
        // Panel principal con márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // ========== PANEL SUPERIOR: TÍTULO Y CONTROLES ==========
        JPanel panelSuperior = new JPanel(new BorderLayout());
        
        // Título
        JLabel titulo = new JLabel("📋 LISTADO DE INVENTARIOS REGISTRADOS");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(0, 70, 140));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Panel de información
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panelInfo.setBackground(new Color(240, 240, 240));
        
        lblFechaActual = new JLabel();
        actualizarFecha();
        panelInfo.add(new JLabel("Fecha consulta:"));
        panelInfo.add(lblFechaActual);
        
        lblContador = new JLabel("Cargando registros...");
        lblContador.setFont(new Font("Arial", Font.BOLD, 12));
        panelInfo.add(lblContador);
        
        panelSuperior.add(titulo, BorderLayout.NORTH);
        panelSuperior.add(panelInfo, BorderLayout.CENTER);
        
        // ========== PANEL CENTRAL: TABLA ==========
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Detalle de Inventarios"
        ));
        
        // Crear modelo de tabla
        String[] columnas = {
            "Nro Inventario", 
            "Fecha", 
            "Producto", 
            "Categoría ID", 
            "Costo Ingreso", 
            "Motivo"
        };
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable
            }
        };
        
        tablaInventarios = new JTable(modeloTabla);
        
        // Personalizar tabla
        personalizarTabla();
        
        // Agregar scroll a la tabla
        JScrollPane scrollTabla = new JScrollPane(tablaInventarios);
        scrollTabla.setPreferredSize(new Dimension(850, 350));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);
        
        // ========== PANEL INFERIOR: BOTONES ==========
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        btnActualizar = crearBoton("🔄 Actualizar", new Color(0, 100, 200), Color.BLACK);
        btnExportar = crearBoton("📊 Exportar a CSV", new Color(0, 150, 0), Color.BLACK);
        btnSalir = crearBoton("🚪 Salir", new Color(180, 0, 0), Color.BLACK);
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnExportar);
        panelBotones.add(btnSalir);
        
        // ========== AGREGAR PANELES A VENTANA ==========
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        // ========== CONFIGURAR EVENTOS ==========
        configurarEventos();
        
        // Actualizar fecha cada minuto
        Timer timer = new Timer(60000, e -> actualizarFecha());
        timer.start();
    }
    
    // ========== PERSONALIZAR TABLA ==========
    private void personalizarTabla() {
        // Configurar header
        JTableHeader header = tablaInventarios.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.BLACK);
        
        // Configurar filas
        tablaInventarios.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaInventarios.setRowHeight(25);
        tablaInventarios.setSelectionBackground(new Color(200, 220, 255));
        tablaInventarios.setSelectionForeground(Color.BLACK);
        tablaInventarios.setGridColor(new Color(220, 220, 220));
        
        // Centrar columnas numéricas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Ajustar anchos de columnas
        tablaInventarios.getColumnModel().getColumn(0).setPreferredWidth(80);  // Nro
        tablaInventarios.getColumnModel().getColumn(1).setPreferredWidth(120); // Fecha
        tablaInventarios.getColumnModel().getColumn(2).setPreferredWidth(150); // Producto
        tablaInventarios.getColumnModel().getColumn(3).setPreferredWidth(80);  // Categoría ID
        tablaInventarios.getColumnModel().getColumn(4).setPreferredWidth(100); // Costo
        tablaInventarios.getColumnModel().getColumn(5).setPreferredWidth(200); // Motivo
        
        // Alinear columnas
        tablaInventarios.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaInventarios.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tablaInventarios.getColumnModel().getColumn(4).setCellRenderer(new CurrencyRenderer());
        
        // Ordenar por columna
        tablaInventarios.setAutoCreateRowSorter(true);
    }
    
    // ========== CARGAR INVENTARIOS DESDE BD ==========
    private void cargarInventarios() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        try {
            // Consulta JPQL con JOIN para obtener todos los datos
            String jpql = "SELECT i FROM Inventario i " +
                         "JOIN FETCH i.producto p " +
                         "JOIN FETCH p.categoria c " +
                         "ORDER BY i.fecha DESC";
            
            Query query = em.createQuery(jpql);
            List<Inventario> inventarios = query.getResultList();
            
            // Formateador de fecha
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            // Agregar cada inventario a la tabla
            for (Inventario inv : inventarios) {
                Object[] fila = {
                    inv.getNroInventario(),
                    inv.getFecha() != null ? sdf.format(inv.getFecha()) : "N/A",
                    inv.getProducto().getNomProd(),
                    inv.getProducto().getCategoria().getIdCate(),
                    inv.getCostoIngreso(),
                    inv.getMotivoIngreso()
                };
                modeloTabla.addRow(fila);
            }
            
            // Actualizar contador
            lblContador.setText("Total registros: " + inventarios.size());
            lblContador.setForeground(inventarios.isEmpty() ? Color.RED : new Color(0, 100, 0));
            
            if (inventarios.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay inventarios registrados.\nUse la ventana de registro para agregar nuevos.",
                    "Sin Datos",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar inventarios:\n" + e.getMessage(),
                "Error de Consulta",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // ========== CONFIGURAR EVENTOS DE BOTONES ==========
    private void configurarEventos() {
        // Botón Actualizar
        btnActualizar.addActionListener(e -> {
            btnActualizar.setEnabled(false);
            btnActualizar.setText("Cargando...");
            
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    cargarInventarios();
                    return null;
                }
                
                @Override
                protected void done() {
                    btnActualizar.setEnabled(true);
                    btnActualizar.setText("🔄 Actualizar");
                    actualizarFecha();
                }
            };
            worker.execute();
        });
        
        // Botón Exportar a CSV
        btnExportar.addActionListener(e -> exportarACSV());
        
        // Botón Salir
        btnSalir.addActionListener(e -> {
            cerrarConexion();
            System.exit(0);
        });
        
        // Doble click en fila muestra detalles
        tablaInventarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mostrarDetallesFila();
                }
            }
        });
        
        // Tecla F5 para actualizar
        getRootPane().registerKeyboardAction(
            e -> cargarInventarios(),
            KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    // ========== EXPORTAR A CSV ==========
    private void exportarACSV() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay datos para exportar.",
                "Exportación Cancelada",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar listado como CSV");
        fileChooser.setSelectedFile(new java.io.File("inventarios.csv"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new java.io.File(file.getAbsolutePath() + ".csv");
            }
            
            try (java.io.PrintWriter writer = new java.io.PrintWriter(file, "UTF-8")) {
                // Escribir encabezados
                for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
                    writer.print(modeloTabla.getColumnName(i));
                    if (i < modeloTabla.getColumnCount() - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
                
                // Escribir datos
                for (int row = 0; row < modeloTabla.getRowCount(); row++) {
                    for (int col = 0; col < modeloTabla.getColumnCount(); col++) {
                        Object value = modeloTabla.getValueAt(row, col);
                        writer.print(value != null ? value.toString() : "");
                        if (col < modeloTabla.getColumnCount() - 1) {
                            writer.print(",");
                        }
                    }
                    writer.println();
                }
                
                JOptionPane.showMessageDialog(this,
                    "✅ Datos exportados exitosamente a:\n" + file.getAbsolutePath(),
                    "Exportación Completada",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al exportar: " + e.getMessage(),
                    "Error de Exportación",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // ========== MOSTRAR DETALLES DE FILA ==========
    private void mostrarDetallesFila() {
        int filaSeleccionada = tablaInventarios.getSelectedRow();
        if (filaSeleccionada >= 0) {
            // Convertir índice de vista a índice de modelo (por si está ordenada)
            int modeloIndex = tablaInventarios.convertRowIndexToModel(filaSeleccionada);
            
            // Obtener número de inventario
            int nroInventario = (int) modeloTabla.getValueAt(modeloIndex, 0);
            
            try {
                // Buscar inventario completo en BD
                Inventario inv = em.find(Inventario.class, nroInventario);
                
                if (inv != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String detalles = String.format(
                        "📋 DETALLES COMPLETOS DEL INVENTARIO\n\n" +
                        "Número: %d\n" +
                        "Fecha Registro: %s\n\n" +
                        "PRODUCTO:\n" +
                        "  Nombre: %s\n" +
                        "  Stock Actual: %d\n" +
                        "  Categoría: %s (ID: %d)\n" +
                        "  Frecuencia Compra: %s\n\n" +
                        "INVENTARIO:\n" +
                        "  Costo de Ingreso: S/. %.2f\n" +
                        "  Motivo: %s",
                        inv.getNroInventario(),
                        inv.getFecha() != null ? sdf.format(inv.getFecha()) : "N/A",
                        inv.getProducto().getNomProd(),
                        inv.getProducto().getStockActual(),
                        inv.getProducto().getCategoria().getDescripcion(),
                        inv.getProducto().getCategoria().getIdCate(),
                        inv.getProducto().getCategoria().getFrecuenciaCompra(),
                        inv.getCostoIngreso(),
                        inv.getMotivoIngreso()
                    );
                    
                    JOptionPane.showMessageDialog(this,
                        detalles,
                        "Detalles del Inventario #" + nroInventario,
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // ========== MÉTODOS AUXILIARES ==========
    
    private JButton crearBoton(String texto, Color fondo, Color textoColor) {
        JButton boton = new JButton(texto);
        boton.setBackground(fondo);
        boton.setForeground(textoColor);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setPreferredSize(new Dimension(150, 35));
        boton.setFocusPainted(false);
        return boton;
    }
    
    private void actualizarFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        lblFechaActual.setText(sdf.format(new java.util.Date()));
    }
    
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
    
    // ========== RENDERER PARA FORMATO DE MONEDA ==========
    private class CurrencyRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof Number) {
                setText(String.format("S/. %.2f", ((Number) value).doubleValue()));
                setHorizontalAlignment(JLabel.RIGHT);
                setForeground(new Color(0, 100, 0)); // Verde para montos
                setFont(new Font("Arial", Font.BOLD, 11));
            }
            
            return this;
        }
    }
}
