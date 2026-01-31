package com.gestiontareas.componentes.bd;

import org.exist.xmldb.EXistResource;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

/**
 * Componente encargado de gestionar la conexión con la base de datos eXist-db
 */
public class ConectorBD {
    
    private static final String DRIVER = "org.exist.xmldb.DatabaseImpl";
    private static final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    private static final String COLECCION_BASE = "/db/tareas";
    
    private String usuario;
    private String password;
    private Collection coleccion;
    
    /**
     * Constructor del componente
     * @param usuario Usuario de eXist-db
     * @param password Contraseña de eXist-db
     */
    public ConectorBD(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }
    
    /**
     * Inicializa la conexión con la base de datos
     * @return true si la conexión es exitosa
     * @throws XMLDBException Si hay error en la conexión
     */
    public boolean conectar() throws XMLDBException {
        try {
            // Registrar el driver de eXist-db
            Class<?> cl = Class.forName(DRIVER);
            Database database = (Database) cl.getDeclaredConstructor().newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);
            
            // Obtener la colección raíz
            Collection root = DatabaseManager.getCollection(URI + "/db", usuario, password);
            
            if (root == null) {
                System.err.println("No se pudo conectar a la base de datos");
                return false;
            }
            
            // Crear o obtener la colección de tareas
            coleccion = crearObtenerColeccion(root, "tareas");
            
            if (coleccion != null) {
                System.out.println("Conexión exitosa con eXist-db");
                System.out.println("Colección: " + coleccion.getName());
                return true;
            }
            
            return false;
            
        } catch (ClassNotFoundException e) {
            System.err.println("Driver de eXist-db no encontrado: " + e.getMessage());
            throw new XMLDBException(0, "Driver no encontrado");
        } catch (Exception e) {
            System.err.println("Error al conectar con eXist-db: " + e.getMessage());
            throw new XMLDBException(0, e.getMessage());
        }
    }
    
    /**
     * Crea o obtiene una colección en la base de datos
     * @param parent Colección padre
     * @param nombre Nombre de la colección
     * @return La colección creada u obtenida
     * @throws XMLDBException Si hay error
     */
    private Collection crearObtenerColeccion(Collection parent, String nombre) throws XMLDBException {
        Collection child = parent.getChildCollection(nombre);
        
        if (child == null) {
            // La colección no existe, crearla usando CollectionManagementService
            System.out.println("Creando colección: " + nombre);
            org.xmldb.api.modules.CollectionManagementService mgtService = 
                (org.xmldb.api.modules.CollectionManagementService) parent.getService(
                    "CollectionManagementService", "1.0");
            child = mgtService.createCollection(nombre);
        }
        
        return child;
    }
    
    /**
     * Obtiene la colección actual
     * @return La colección de tareas
     */
    public Collection getColeccion() {
        return coleccion;
    }
    
    /**
     * Cierra la conexión con la base de datos
     */
    public void desconectar() {
        if (coleccion != null) {
            try {
                coleccion.close();
                System.out.println("Conexión cerrada correctamente");
            } catch (XMLDBException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    
    /**
     * Limpia los recursos de una colección
     * @param col Colección a limpiar
     */
    public static void limpiarRecursos(Collection col) {
        if (col != null) {
            try {
                col.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Limpia los recursos de un recurso XML
     * @param res Recurso a limpiar
     */
    public static void limpiarRecursos(EXistResource res) {
        if (res != null) {
            try {
                res.freeResources();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Verifica si la conexión está activa
     * @return true si está conectado
     */
    public boolean estaConectado() {
        return coleccion != null;
    }
    
    // Getters
    public String getUsuario() {
        return usuario;
    }
    
    public String getURI() {
        return URI;
    }
    
    public String getColeccionBase() {
        return COLECCION_BASE;
    }
}