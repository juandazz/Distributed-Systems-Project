package com.example.service;

import com.example.model.StorageNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoadBalancerService {
    
    private final Map<String, StorageNode> nodes = new ConcurrentHashMap<>();
    private final AtomicInteger roundRobinIndex = new AtomicInteger(0);
    private String loadBalancerStrategy = "ROUND_ROBIN";
    private int replicationFactor = 3;
    private long healthCheckInterval = 30000;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final RestTemplate restTemplate = new RestTemplate();
    
    @PostConstruct
    public void init() {
        loadNodesConfiguration();
        startHealthCheckScheduler();
    }
    
    /**
     * Cargar configuración de nodos desde archivo nodes.properties
     */
    private void loadNodesConfiguration() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("nodes.properties")) {
            props.load(fis);
            
            // Cargar estrategia de balanceo
            loadBalancerStrategy = props.getProperty("loadbalancer.strategy", "ROUND_ROBIN");
            
            // Cargar factor de replicación
            replicationFactor = Integer.parseInt(props.getProperty("replication.factor", "3"));
            
            // Cargar intervalo de health check
            healthCheckInterval = Long.parseLong(props.getProperty("healthcheck.interval", "30000"));
            
            // Cargar nodos
            for (String key : props.stringPropertyNames()) {
                if (key.startsWith("node")) {
                    String nodeName = key;
                    String nodeUrl = props.getProperty(key);
                    StorageNode node = new StorageNode(nodeName, nodeUrl);
                    nodes.put(nodeName, node);
                    System.out.println("✅ Nodo cargado: " + nodeName + " -> " + nodeUrl);
                }
            }
            
            System.out.println("📊 Estrategia de balanceo: " + loadBalancerStrategy);
            System.out.println("🔄 Factor de replicación: " + replicationFactor);
            System.out.println("💓 Health check interval: " + healthCheckInterval + "ms");
            
        } catch (IOException e) {
            System.err.println("⚠️ No se pudo cargar nodes.properties, usando configuración por defecto");
            // Crear nodo local por defecto
            StorageNode defaultNode = new StorageNode("default", "http://localhost:8081");
            nodes.put("default", defaultNode);
        }
        
        // Hacer health check inicial
        checkAllNodesHealth();
    }
    
    /**
     * Iniciar scheduler para health checks periódicos
     */
    private void startHealthCheckScheduler() {
        scheduler.scheduleAtFixedRate(
            this::checkAllNodesHealth,
            healthCheckInterval,
            healthCheckInterval,
            TimeUnit.MILLISECONDS
        );
        System.out.println("💓 Health check scheduler iniciado");
    }
    
    /**
     * Verificar salud de todos los nodos
     */
    private void checkAllNodesHealth() {
        for (StorageNode node : nodes.values()) {
            try {
                String healthUrl = node.getUrl() + "/actuator/health";
                restTemplate.getForObject(healthUrl, String.class);
                node.setStatus(StorageNode.NodeStatus.ONLINE);
                node.setLastHealthCheck(System.currentTimeMillis());
            } catch (Exception e) {
                node.setStatus(StorageNode.NodeStatus.OFFLINE);
            }
        }
    }
    
    /**
     * Obtener el siguiente nodo según la estrategia de balanceo
     */
    public StorageNode getNextNode() {
        List<StorageNode> availableNodes = getAvailableNodes();
        
        if (availableNodes.isEmpty()) {
            throw new RuntimeException("No hay nodos disponibles");
        }
        
        switch (loadBalancerStrategy) {
            case "LEAST_LOADED":
                return getLeastLoadedNode(availableNodes);
            case "RANDOM":
                return getRandomNode(availableNodes);
            case "ROUND_ROBIN":
            default:
                return getRoundRobinNode(availableNodes);
        }
    }
    
    /**
     * Round Robin - Distribuir secuencialmente
     */
    private StorageNode getRoundRobinNode(List<StorageNode> availableNodes) {
        int index = Math.abs(roundRobinIndex.getAndIncrement() % availableNodes.size());
        return availableNodes.get(index);
    }
    
    /**
     * Least Loaded - Nodo con menor carga
     */
    private StorageNode getLeastLoadedNode(List<StorageNode> availableNodes) {
        return availableNodes.stream()
                .min(Comparator.comparingInt(StorageNode::getCurrentLoad))
                .orElse(availableNodes.get(0));
    }
    
    /**
     * Random - Nodo aleatorio
     */
    private StorageNode getRandomNode(List<StorageNode> availableNodes) {
        Random random = new Random();
        return availableNodes.get(random.nextInt(availableNodes.size()));
    }
    
    /**
     * Obtener múltiples nodos para replicación
     */
    public List<StorageNode> getNodesForReplication() {
        List<StorageNode> availableNodes = getAvailableNodes();
        
        if (availableNodes.isEmpty()) {
            throw new RuntimeException("No hay nodos disponibles para replicación");
        }
        
        int nodesToSelect = Math.min(replicationFactor, availableNodes.size());
        List<StorageNode> selectedNodes = new ArrayList<>();
        
        // Seleccionar nodos diferentes para replicación
        List<StorageNode> shuffled = new ArrayList<>(availableNodes);
        Collections.shuffle(shuffled);
        
        for (int i = 0; i < nodesToSelect; i++) {
            selectedNodes.add(shuffled.get(i));
        }
        
        return selectedNodes;
    }
    
    /**
     * Obtener lista de nodos disponibles (ONLINE)
     */
    public List<StorageNode> getAvailableNodes() {
        return nodes.values().stream()
                .filter(StorageNode::isAvailable)
                .toList();
    }
    
    /**
     * Obtener todos los nodos
     */
    public Map<String, StorageNode> getAllNodes() {
        return new HashMap<>(nodes);
    }
    
    /**
     * Obtener información de un nodo específico
     */
    public StorageNode getNode(String nodeName) {
        return nodes.get(nodeName);
    }
    
    /**
     * Marcar nodo como ocupado
     */
    public void markNodeBusy(StorageNode node) {
        node.incrementLoad();
    }
    
    /**
     * Marcar nodo como liberado
     */
    public void markNodeFree(StorageNode node) {
        node.decrementLoad();
    }
    
    /**
     * Recargar configuración de nodos
     */
    public void reloadConfiguration() {
        nodes.clear();
        loadNodesConfiguration();
    }
    
    /**
     * Obtener estadísticas del cluster
     */
    public Map<String, Object> getClusterStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalNodes = nodes.size();
        long onlineNodes = nodes.values().stream()
                .filter(n -> n.getStatus() == StorageNode.NodeStatus.ONLINE)
                .count();
        long offlineNodes = totalNodes - onlineNodes;
        
        int totalLoad = nodes.values().stream()
                .mapToInt(StorageNode::getCurrentLoad)
                .sum();
        
        stats.put("totalNodes", totalNodes);
        stats.put("onlineNodes", onlineNodes);
        stats.put("offlineNodes", offlineNodes);
        stats.put("totalLoad", totalLoad);
        stats.put("strategy", loadBalancerStrategy);
        stats.put("replicationFactor", replicationFactor);
        stats.put("nodes", nodes.values());
        
        return stats;
    }
}
