package com.gestiontareas.componentes.gestion;

import com.gestiontareas.componentes.bd.ConectorBD;
import com.gestiontareas.modelo.Tarea;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Componente encargado de gestionar las operaciones CRUD sobre las tareas
 */
public class GestorTareas {
    
    private ConectorBD conectorBD;
    
    /**
     * Constructor del componente
     * @param conectorBD Componente de conexión a la base de datos
     */
    public GestorTareas(ConectorBD conectorBD) {
        this.conectorBD = conectorBD;
    }
    
    /**
     * Crea una nueva tarea en la base de datos
     * @param tarea Objeto Tarea a crear
     * @return true si se creó correctamente
     */
    public boolean crearTarea(Tarea tarea) {
        try {
            // Generar ID único si no tiene
            if (tarea.getId() == null || tarea.getId().isEmpty()) {
                tarea.setId(UUID.randomUUID().toString());
            }
            
            // Convertir la tarea a XML
            String xml = tareaToXML(tarea);
            
            // Obtener la colección
            Collection col = conectorBD.getColeccion();
            
            // Crear el recurso XML
            XMLResource recurso = (XMLResource) col.createResource(tarea.getId() + ".xml", "XMLResource");
            recurso.setContent(xml);
            col.storeResource(recurso);
            
            System.out.println("Tarea creada: " + tarea.getId());
            return true;
            
        } catch (XMLDBException e) {
            System.err.println("Error al crear tarea: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene todas las tareas de la base de datos
     * @return Lista de tareas
     */
    public List<Tarea> obtenerTodasLasTareas() {
        List<Tarea> tareas = new ArrayList<>();
        
        try {
            Collection col = conectorBD.getColeccion();
            
            // Consulta XPath para obtener todas las tareas
            XPathQueryService service = (XPathQueryService) col.getService("XPathQueryService", "1.0");
            ResourceSet resultado = service.query("//tarea");
            
            ResourceIterator iterator = resultado.getIterator();
            while (iterator.hasMoreResources()) {
                XMLResource recurso = (XMLResource) iterator.nextResource();
                Tarea tarea = xmlToTarea(recurso.getContent().toString());
                if (tarea != null) {
                    tareas.add(tarea);
                }
            }
            
            System.out.println("Tareas recuperadas: " + tareas.size());
            
        } catch (XMLDBException e) {
            System.err.println("Error al obtener tareas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tareas;
    }
    
    /**
     * Busca una tarea por su ID
     * @param id ID de la tarea
     * @return La tarea encontrada o null
     */
    public Tarea buscarTareaPorId(String id) {
        try {
            Collection col = conectorBD.getColeccion();
            Resource recurso = col.getResource(id + ".xml");
            
            if (recurso != null) {
                XMLResource xmlRecurso = (XMLResource) recurso;
                return xmlToTarea(xmlRecurso.getContent().toString());
            }
            
        } catch (XMLDBException e) {
            System.err.println("Error al buscar tarea: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Actualiza una tarea existente
     * @param tarea Tarea con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean actualizarTarea(Tarea tarea) {
        try {
            Collection col = conectorBD.getColeccion();
            
            // Eliminar el recurso anterior
            Resource recursoAnterior = col.getResource(tarea.getId() + ".xml");
            if (recursoAnterior != null) {
                col.removeResource(recursoAnterior);
            }
            
            // Crear el nuevo recurso con los datos actualizados
            String xml = tareaToXML(tarea);
            XMLResource recurso = (XMLResource) col.createResource(tarea.getId() + ".xml", "XMLResource");
            recurso.setContent(xml);
            col.storeResource(recurso);
            
            System.out.println("Tarea actualizada: " + tarea.getId());
            return true;
            
        } catch (XMLDBException e) {
            System.err.println("Error al actualizar tarea: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Elimina una tarea por su ID
     * @param id ID de la tarea a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarTarea(String id) {
        try {
            Collection col = conectorBD.getColeccion();
            Resource recurso = col.getResource(id + ".xml");
            
            if (recurso != null) {
                col.removeResource(recurso);
                System.out.println("Tarea eliminada: " + id);
                return true;
            } else {
                System.err.println("Tarea no encontrada: " + id);
                return false;
            }
            
        } catch (XMLDBException e) {
            System.err.println("Error al eliminar tarea: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene tareas filtradas por estado
     * @param estado Estado a filtrar ("pendiente", "en_proceso", "completada")
     * @return Lista de tareas con ese estado
     */
    public List<Tarea> obtenerTareasPorEstado(String estado) {
        List<Tarea> tareasFiltradas = new ArrayList<>();
        
        try {
            Collection col = conectorBD.getColeccion();
            XPathQueryService service = (XPathQueryService) col.getService("XPathQueryService", "1.0");
            
            String query = "//tarea[estado='" + estado + "']";
            ResourceSet resultado = service.query(query);
            
            ResourceIterator iterator = resultado.getIterator();
            while (iterator.hasMoreResources()) {
                XMLResource recurso = (XMLResource) iterator.nextResource();
                Tarea tarea = xmlToTarea(recurso.getContent().toString());
                if (tarea != null) {
                    tareasFiltradas.add(tarea);
                }
            }
            
        } catch (XMLDBException e) {
            System.err.println("Error al filtrar tareas: " + e.getMessage());
        }
        
        return tareasFiltradas;
    }
    
    /**
     * Convierte un objeto Tarea a formato XML
     * @param tarea Objeto Tarea
     * @return String XML
     */
    private String tareaToXML(Tarea tarea) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<tarea>\n");
        xml.append("  <id>").append(tarea.getId()).append("</id>\n");
        xml.append("  <titulo>").append(escaparXML(tarea.getTitulo())).append("</titulo>\n");
        xml.append("  <descripcion>").append(escaparXML(tarea.getDescripcion())).append("</descripcion>\n");
        xml.append("  <estado>").append(tarea.getEstado()).append("</estado>\n");
        xml.append("  <prioridad>").append(tarea.getPrioridad()).append("</prioridad>\n");
        xml.append("  <fechaCreacion>").append(tarea.getFechaCreacion()).append("</fechaCreacion>\n");
        if (tarea.getFechaVencimiento() != null) {
            xml.append("  <fechaVencimiento>").append(tarea.getFechaVencimiento()).append("</fechaVencimiento>\n");
        }
        xml.append("</tarea>");
        return xml.toString();
    }
    
    /**
     * Convierte XML a objeto Tarea
     * @param xml String XML
     * @return Objeto Tarea
     */
    private Tarea xmlToTarea(String xml) {
        try {
            Tarea tarea = new Tarea();
            
            // Extraer valores usando expresiones simples
            tarea.setId(extraerValor(xml, "id"));
            tarea.setTitulo(extraerValor(xml, "titulo"));
            tarea.setDescripcion(extraerValor(xml, "descripcion"));
            tarea.setEstado(extraerValor(xml, "estado"));
            tarea.setPrioridad(extraerValor(xml, "prioridad"));
            
            String fechaCreacion = extraerValor(xml, "fechaCreacion");
            if (fechaCreacion != null && !fechaCreacion.isEmpty()) {
                tarea.setFechaCreacion(LocalDate.parse(fechaCreacion));
            }
            
            String fechaVencimiento = extraerValor(xml, "fechaVencimiento");
            if (fechaVencimiento != null && !fechaVencimiento.isEmpty()) {
                tarea.setFechaVencimiento(LocalDate.parse(fechaVencimiento));
            }
            
            return tarea;
            
        } catch (Exception e) {
            System.err.println("Error al convertir XML a Tarea: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Extrae el valor de un elemento XML
     * @param xml String XML
     * @param tag Etiqueta a buscar
     * @return Valor extraído
     */
    private String extraerValor(String xml, String tag) {
        String inicio = "<" + tag + ">";
        String fin = "</" + tag + ">";
        
        int posInicio = xml.indexOf(inicio);
        int posFin = xml.indexOf(fin);
        
        if (posInicio != -1 && posFin != -1) {
            return xml.substring(posInicio + inicio.length(), posFin).trim();
        }
        
        return "";
    }
    
    /**
     * Escapa caracteres especiales XML
     * @param texto Texto a escapar
     * @return Texto escapado
     */
    private String escaparXML(String texto) {
        if (texto == null) return "";
        return texto.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
}