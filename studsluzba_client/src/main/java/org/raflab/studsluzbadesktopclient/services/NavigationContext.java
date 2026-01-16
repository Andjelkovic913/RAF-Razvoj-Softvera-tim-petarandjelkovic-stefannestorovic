package org.raflab.studsluzbadesktopclient.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a complete navigation state including view, tabs, and custom data.
 * Used to properly restore UI state when navigating through history.
 */
public class NavigationContext {
    
    private String viewName;
    private NavigationType type;
    private Integer selectedTabIndex;
    private Double scrollPosition;
    private Map<String, Object> customData = new HashMap<>();
    private boolean cached;
    
    public enum NavigationType {
        VIEW_CHANGE,    // Root view change (different FXML)
        TAB_CHANGE,     // Tab selection within same view
        DATA_CHANGE     // Same view but different data (e.g., different student)
    }
    
    // Constructors
    public NavigationContext() {
        this.type = NavigationType.VIEW_CHANGE;
    }
    
    public NavigationContext(String viewName) {
        this.viewName = viewName;
        this.type = NavigationType.VIEW_CHANGE;
    }
    
    public NavigationContext(String viewName, NavigationType type) {
        this.viewName = viewName;
        this.type = type;
    }
    
    // Factory methods for common scenarios
    public static NavigationContext forViewChange(String viewName, boolean cached) {
        NavigationContext context = new NavigationContext(viewName, NavigationType.VIEW_CHANGE);
        context.setCached(cached);
        return context;
    }
    
    public static NavigationContext forTabChange(String viewName, int tabIndex) {
        NavigationContext context = new NavigationContext(viewName, NavigationType.TAB_CHANGE);
        context.setSelectedTabIndex(tabIndex);
        return context;
    }
    
    public static NavigationContext forDataChange(String viewName, String dataKey, Object dataValue) {
        NavigationContext context = new NavigationContext(viewName, NavigationType.DATA_CHANGE);
        context.addCustomData(dataKey, dataValue);
        return context;
    }
    
    // Helper methods
    public void addCustomData(String key, Object value) {
        customData.put(key, value);
    }
    
    public Object getCustomData(String key) {
        return customData.get(key);
    }
    
    public boolean hasCustomData(String key) {
        return customData.containsKey(key);
    }

    /**
     * Creates a unique key for this navigation state.
     */
    public String getStateKey() {
        StringBuilder key = new StringBuilder(viewName != null ? viewName : "");
        
        if (type == NavigationType.TAB_CHANGE && selectedTabIndex != null) {
            key.append("_tab_").append(selectedTabIndex);
        }
        
        if (type == NavigationType.DATA_CHANGE && !customData.isEmpty()) {
            if (customData.containsKey("studentId")) {
                key.append("_student_").append(customData.get("studentId"));
            }
            if (customData.containsKey("nastavnikId")) {
                key.append("_nastavnik_").append(customData.get("nastavnikId"));
            }
            if (customData.containsKey("ispitId")) {
                key.append("_ispit_").append(customData.get("ispitId"));
            }
        }
        
        return key.toString();
    }
    
    // Getters and setters
    public String getViewName() {
        return viewName;
    }
    
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
    
    public NavigationType getType() {
        return type;
    }
    
    public void setType(NavigationType type) {
        this.type = type;
    }
    
    public Integer getSelectedTabIndex() {
        return selectedTabIndex;
    }
    
    public void setSelectedTabIndex(Integer selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }
    
    public Double getScrollPosition() {
        return scrollPosition;
    }
    
    public void setScrollPosition(Double scrollPosition) {
        this.scrollPosition = scrollPosition;
    }
    
    public Map<String, Object> getCustomData() {
        return customData;
    }
    
    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
    
    public boolean isCached() {
        return cached;
    }
    
    public void setCached(boolean cached) {
        this.cached = cached;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NavigationContext that = (NavigationContext) obj;
        return Objects.equals(getStateKey(), that.getStateKey());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getStateKey());
    }
    
    @Override
    public String toString() {
        return "NavigationContext{viewName='" + viewName + "', type=" + type + 
               ", tabIndex=" + selectedTabIndex + ", cached=" + cached + "}";
    }
}
