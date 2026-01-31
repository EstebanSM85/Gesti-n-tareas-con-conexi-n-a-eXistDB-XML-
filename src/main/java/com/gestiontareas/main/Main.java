package com.gestiontareas.main;

import com.gestiontareas.componentes.bd.ConectorBD;
import com.gestiontareas.componentes.gestion.GestorTareas;
import com.gestiontareas.ui.VentanaPrincipal;

import javax.swing.*;

/**
 * Clase principal de la aplicación de gestión de tareas
 * Aplicación basada en componentes que utiliza eXist-db
 * 
 * @author Esteban Sanchez
 * @version 1.0.0
 */
public class Main {
    
    // Credenciales de eXist-db
    private static final String USUARIO = "admin";
    private static final String PASSWORD = "admin";
    
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("  Sistema de Gestión de Tareas - eXist-db");
        System.out.println("  Versión 1.0.0");
        System.out.println("  Autor: Esteban Sanchez");
        System.out.println("==============================================\n");
        
        // Configurar el Look and Feel del sistema
        configurarLookAndFeel();
        
        // Iniciar la aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                iniciarAplicacion();
            } catch (Exception e) {
                System.err.println("Error al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();
                mostrarErrorInicio(e.getMessage());
            }
        });
    }
    
    /**
     * Inicializa la aplicación y sus componentes
     */
    private static void iniciarAplicacion() {
        System.out.println("Iniciando aplicación...\n");
        
        // PASO 1: Crear el componente de conexión a BD
        System.out.println("PASO 1: Creando componente ConectorBD...");
        ConectorBD conectorBD = new ConectorBD(USUARIO, PASSWORD);
        
        // PASO 2: Conectar con la base de datos
        System.out.println("PASO 2: Conectando con eXist-db...");
        try {
            boolean conectado = conectorBD.conectar();
            
            if (!conectado) {
                String mensaje = "No se pudo conectar con eXist-db.\n\n" +
                               "Verifique que:\n" +
                               "1. eXist-db esté instalado y en ejecución\n" +
                               "2. El servicio esté escuchando en: localhost:8080\n" +
                               "3. Las credenciales sean correctas (usuario: admin, password: admin)";
                mostrarErrorInicio(mensaje);
                return;
            }
            
            System.out.println("✓ Conexión establecida exitosamente\n");
            
        } catch (Exception e) {
            String mensaje = "Error al conectar con eXist-db:\n" + e.getMessage() + "\n\n" +
                           "Asegúrese de que eXist-db esté en ejecución en localhost:8080";
            mostrarErrorInicio(mensaje);
            return;
        }
        
        // PASO 3: Crear el componente gestor de tareas
        System.out.println("PASO 3: Creando componente GestorTareas...");
        GestorTareas gestorTareas = new GestorTareas(conectorBD);
        System.out.println("✓ Gestor de tareas inicializado\n");
        
        // PASO 4: Crear y mostrar la interfaz gráfica
        System.out.println("PASO 4: Inicializando interfaz gráfica...");
        VentanaPrincipal ventana = new VentanaPrincipal(conectorBD, gestorTareas);
        
        // Agregar hook para cerrar la conexión al salir
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nCerrando aplicación...");
            conectorBD.desconectar();
            System.out.println("Aplicación cerrada correctamente");
        }));
        
        ventana.setVisible(true);
        System.out.println("✓ Interfaz gráfica lista\n");
        System.out.println("==============================================");
        System.out.println("  Aplicación iniciada correctamente");
        System.out.println("==============================================\n");
    }
    
    /**
     * Configura el Look and Feel de la interfaz
     */
    private static void configurarLookAndFeel() {
        try {
            // Intentar usar el Look and Feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo configurar el Look and Feel del sistema");
            // Continuar con el Look and Feel por defecto
        }
    }
    
    /**
     * Muestra un mensaje de error al iniciar
     */
    private static void mostrarErrorInicio(String mensaje) {
        System.err.println("\n❌ ERROR: " + mensaje + "\n");
        
        JOptionPane.showMessageDialog(null, 
            mensaje, 
            "Error al iniciar la aplicación", 
            JOptionPane.ERROR_MESSAGE);
        
        System.exit(1);
    }
}