package org.fuin.dsl.ddd.gen.extensions

import java.util.Map
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.dsl.ddd.gen.base.ComputingCodeReferenceRegistry

/**
 * Provides extension methods for Map.
 */
class MapExtensions {
    
    static val String CODE_REFERENCE_REGISTRY_KEY = "CODE_REFERENCE_REGISTRY"
    
    /**
     * Returns the registry from the map. If it does not exist, it will be created.
     * 
     * @param map Map with registry.
     * 
     * @return Registry.
     */
    def static CodeReferenceRegistry getCodeReferenceRegistry(Map<String, Object> map) {
        var CodeReferenceRegistry reg = map.get(CODE_REFERENCE_REGISTRY_KEY) as CodeReferenceRegistry
        if (reg === null) {
            reg = new ComputingCodeReferenceRegistry()
            map.put(CODE_REFERENCE_REGISTRY_KEY, reg)
        }
        return reg
    }
    
}