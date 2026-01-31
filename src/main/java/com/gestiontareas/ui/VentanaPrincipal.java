package com.gestiontareas.ui;

import com.gestiontareas.componentes.bd.ConectorBD;
import com.gestiontareas.componentes.gestion.GestorTareas;
import com.gestiontareas.modelo.Tarea;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Ventana principal de la aplicación de gestión de tareas
 */
public class VentanaPrincipal extends JFrame {
    
    private ConectorBD conectorBD;
    private GestorTareas gestorTareas;
    
    // Componentes de la interfaz
    private JTable tablaTareas;
    private DefaultTableModel modeloTabla;
    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private JComboBox<String> cmbEstado;
    private JComboBox<String> cmbPrioridad;
    private JTextField txtFechaVencimiento;
    private JButton btnCrear;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JLabel lblEstadoConexion;
    
    private String tareaSeleccionadaId = null;
    
    /**
     * Constructor de la ventana
     * @param conectorBD Componente de conexión a BD
     * @param gestorTareas Componente de gestión de tareas
     */
    public VentanaPrincipal(ConectorBD conectorBD, GestorTareas gestorTareas) {
        this.conectorBD = conectorBD;
        this.gestorTareas = gestorTareas;
        
        inicializarComponentes();
        configurarEventos();
        cargarTareas();
    }
    
    /**
     * Inicializa todos los componentes de la interfaz
     */
    private void inicializarComponentes() {
        setTitle("Sistema de Gestión de Tareas - eXist-db");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior - Estado de conexión
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBackground(new Color(46, 125, 50));
        lblEstadoConexion = new JLabel("● Conectado a eXist-db");
        lblEstadoConexion.setForeground(Color.WHITE);
        lblEstadoConexion.setFont(new Font("Arial", Font.BOLD, 14));
        panelSuperior.add(lblEstadoConexion);
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central - Tabla de tareas
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columnas = {"ID", "Título", "Estado", "Prioridad", "Fecha Vencimiento"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaTareas = new JTable(modeloTabla);
        tablaTareas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaTareas.getTableHeader().setReorderingAllowed(false);
        tablaTareas.setRowHeight(25);
        
        JScrollPane scrollTabla = new JScrollPane(tablaTareas);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Lista de Tareas"));
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel derecho - Formulario
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelDerecho.setPreferredSize(new Dimension(350, 0));
        
        // Título del formulario
        JLabel lblFormulario = new JLabel("Gestión de Tareas");
        lblFormulario.setFont(new Font("Arial", Font.BOLD, 16));
        lblFormulario.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(lblFormulario);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Campo Título
        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(lblTitulo);
        txtTitulo = new JTextField();
        txtTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(txtTitulo);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Campo Descripción
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(lblDescripcion);
        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        scrollDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(scrollDescripcion);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Campo Estado
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(lblEstado);
        cmbEstado = new JComboBox<>(new String[]{"pendiente", "en_proceso", "completada"});
        cmbEstado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cmbEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(cmbEstado);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Campo Prioridad
        JLabel lblPrioridad = new JLabel("Prioridad:");
        lblPrioridad.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(lblPrioridad);
        cmbPrioridad = new JComboBox<>(new String[]{"baja", "media", "alta"});
        cmbPrioridad.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cmbPrioridad.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(cmbPrioridad);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Campo Fecha Vencimiento
        JLabel lblFecha = new JLabel("Fecha Vencimiento (YYYY-MM-DD):");
        lblFecha.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(lblFecha);
        txtFechaVencimiento = new JTextField();
        txtFechaVencimiento.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtFechaVencimiento.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtFechaVencimiento.setText(LocalDate.now().plusDays(7).toString());
        panelDerecho.add(txtFechaVencimiento);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 20)));
        
     // Botones de acción
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(2, 2, 10, 10));
        panelBotones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnCrear = new JButton("Crear");
        btnCrear.setBackground(new Color(76, 175, 80));
        btnCrear.setForeground(Color.BLACK);  // CAMBIADO A NEGRO
        btnCrear.setFocusPainted(false);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(33, 150, 243));
        btnActualizar.setForeground(Color.BLACK);  // CAMBIADO A NEGRO
        btnActualizar.setFocusPainted(false);
        btnActualizar.setEnabled(false);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(244, 67, 54));
        btnEliminar.setForeground(Color.BLACK);  // CAMBIADO A NEGRO
        btnEliminar.setFocusPainted(false);
        btnEliminar.setEnabled(false);

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(new Color(158, 158, 158));
        btnRefrescar.setForeground(Color.BLACK);  // CAMBIADO A NEGRO
        btnRefrescar.setFocusPainted(false);

        panelBotones.add(btnCrear);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);

        panelDerecho.add(panelBotones);
        
        add(panelDerecho, BorderLayout.EAST);
    }
    
    /**
     * Configura los eventos de los componentes
     */
    private void configurarEventos() {
        // Selección de fila en la tabla
        tablaTareas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = tablaTareas.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    cargarTareaEnFormulario(filaSeleccionada);
                    btnActualizar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                } else {
                    limpiarFormulario();
                    btnActualizar.setEnabled(false);
                    btnEliminar.setEnabled(false);
                }
            }
        });
        
        // Botón Crear
        btnCrear.addActionListener(e -> crearTarea());
        
        // Botón Actualizar
        btnActualizar.addActionListener(e -> actualizarTarea());
        
        // Botón Eliminar
        btnEliminar.addActionListener(e -> eliminarTarea());
        
        // Botón Refrescar
        btnRefrescar.addActionListener(e -> cargarTareas());
    }
    
    /**
     * Carga todas las tareas en la tabla
     */
    private void cargarTareas() {
        modeloTabla.setRowCount(0);
        List<Tarea> tareas = gestorTareas.obtenerTodasLasTareas();
        
        for (Tarea tarea : tareas) {
            Object[] fila = {
                tarea.getId().substring(0, 8) + "...",
                tarea.getTitulo(),
                tarea.getEstado(),
                tarea.getPrioridad(),
                tarea.getFechaVencimiento() != null ? tarea.getFechaVencimiento().toString() : "N/A"
            };
            modeloTabla.addRow(fila);
        }
        
        lblEstadoConexion.setText("● Conectado - " + tareas.size() + " tareas cargadas");
    }
    
    /**
     * Carga los datos de una tarea en el formulario
     */
    private void cargarTareaEnFormulario(int fila) {
        String idCorto = (String) modeloTabla.getValueAt(fila, 0);
        String titulo = (String) modeloTabla.getValueAt(fila, 1);
        
        // Buscar la tarea completa
        List<Tarea> todasLasTareas = gestorTareas.obtenerTodasLasTareas();
        for (Tarea tarea : todasLasTareas) {
            if (tarea.getTitulo().equals(titulo)) {
                tareaSeleccionadaId = tarea.getId();
                txtTitulo.setText(tarea.getTitulo());
                txtDescripcion.setText(tarea.getDescripcion());
                cmbEstado.setSelectedItem(tarea.getEstado());
                cmbPrioridad.setSelectedItem(tarea.getPrioridad());
                txtFechaVencimiento.setText(tarea.getFechaVencimiento() != null ? 
                    tarea.getFechaVencimiento().toString() : "");
                break;
            }
        }
    }
    
    /**
     * Crea una nueva tarea
     */
    private void crearTarea() {
        if (!validarFormulario()) {
            return;
        }
        
        Tarea nuevaTarea = new Tarea();
        nuevaTarea.setTitulo(txtTitulo.getText().trim());
        nuevaTarea.setDescripcion(txtDescripcion.getText().trim());
        nuevaTarea.setEstado((String) cmbEstado.getSelectedItem());
        nuevaTarea.setPrioridad((String) cmbPrioridad.getSelectedItem());
        
        try {
            LocalDate fechaVenc = LocalDate.parse(txtFechaVencimiento.getText().trim());
            nuevaTarea.setFechaVencimiento(fechaVenc);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de fecha inválido. Use YYYY-MM-DD", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (gestorTareas.crearTarea(nuevaTarea)) {
            JOptionPane.showMessageDialog(this, "Tarea creada exitosamente");
            limpiarFormulario();
            cargarTareas();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Error al crear la tarea", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Actualiza la tarea seleccionada
     */
    private void actualizarTarea() {
        if (tareaSeleccionadaId == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una tarea primero");
            return;
        }
        
        if (!validarFormulario()) {
            return;
        }
        
        Tarea tareaActualizada = new Tarea();
        tareaActualizada.setId(tareaSeleccionadaId);
        tareaActualizada.setTitulo(txtTitulo.getText().trim());
        tareaActualizada.setDescripcion(txtDescripcion.getText().trim());
        tareaActualizada.setEstado((String) cmbEstado.getSelectedItem());
        tareaActualizada.setPrioridad((String) cmbPrioridad.getSelectedItem());
        
        try {
            LocalDate fechaVenc = LocalDate.parse(txtFechaVencimiento.getText().trim());
            tareaActualizada.setFechaVencimiento(fechaVenc);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de fecha inválido. Use YYYY-MM-DD", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (gestorTareas.actualizarTarea(tareaActualizada)) {
            JOptionPane.showMessageDialog(this, "Tarea actualizada exitosamente");
            limpiarFormulario();
            cargarTareas();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Error al actualizar la tarea", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Elimina la tarea seleccionada
     */
    private void eliminarTarea() {
        if (tareaSeleccionadaId == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una tarea primero");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar esta tarea?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (gestorTareas.eliminarTarea(tareaSeleccionadaId)) {
                JOptionPane.showMessageDialog(this, "Tarea eliminada exitosamente");
                limpiarFormulario();
                cargarTareas();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al eliminar la tarea", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Valida los campos del formulario
     */
    private boolean validarFormulario() {
        if (txtTitulo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título es obligatorio");
            return false;
        }
        
        if (txtFechaVencimiento.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fecha de vencimiento es obligatoria");
            return false;
        }
        
        return true;
    }
    
    /**
     * Limpia todos los campos del formulario
     */
    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtDescripcion.setText("");
        cmbEstado.setSelectedIndex(0);
        cmbPrioridad.setSelectedIndex(0);
        txtFechaVencimiento.setText(LocalDate.now().plusDays(7).toString());
        tareaSeleccionadaId = null;
        tablaTareas.clearSelection();
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}