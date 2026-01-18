package org.raflab.studsluzbadesktopclient.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

/**
 * Service for managing navigation history with back/forward functionality.
 * Supports configurable max history depth via application.properties.
 * Tracks ALL navigation changes including tab selections per specification.
 */
@Service
public class NavigationHistoryService {

    private final Deque<NavigationContext> backStack = new ArrayDeque<>();
    private final Deque<NavigationContext> forwardStack = new ArrayDeque<>();
    private NavigationContext currentContext = null;
    private boolean isNavigating = false;
    
    @Value("${navigation.history.maxDepth:50}")
    private int maxDepth;

    /**
     * Legacy class for backward compatibility - wraps NavigationContext
     * @deprecated Use NavigationContext directly
     */
    @Deprecated
    public static class NavigationState {
        private final NavigationContext context;

        public NavigationState(String fxmlName, Object contextData, boolean cached) {
            this.context = NavigationContext.forViewChange(fxmlName, cached);
            if (contextData != null) {
                this.context.addCustomData("legacyData", contextData);
            }
        }

        public NavigationState(String fxmlName) {
            this(fxmlName, null, false);
        }

        public String getFxmlName() {
            return context.getViewName();
        }

        public Object getContextData() {
            return context.getCustomData("legacyData");
        }

        public boolean isCached() {
            return context.isCached();
        }
        
        public NavigationContext getContext() {
            return context;
        }
    }

    //public void navigateTo(String fxmlName) {
//        navigateTo(fxmlName, null, false);
//    }


    public void navigateTo(String fxmlName, Object contextData, boolean cached) {
        NavigationContext context = NavigationContext.forViewChange(fxmlName, cached);
        if (contextData != null) {
            context.addCustomData("legacyData", contextData);
        }
        navigateToContext(context);
    }
    
    /**
     * Records a tab change within the current view.
     * @param viewName The current view name
     * @param tabIndex The new tab index
     */
    public void registerTabChange(String viewName, int tabIndex) {
        if (isNavigating) return; // Don't track changes during history navigation
        
        NavigationContext context = NavigationContext.forTabChange(viewName, tabIndex);
        navigateToContext(context);
    }
    
    /**
     * Records navigation with full context.
     */
    public void navigateToContext(NavigationContext context) {
        if (isNavigating) return; // kada ides kroz istoriju onda ne pamtiss promene
        
        if (currentContext != null) { // ovde ne ulazi prvi put
            // bez duplikata
            if (!currentContext.getStateKey().equals(context.getStateKey())) {
                backStack.push(currentContext);
                while (backStack.size() > maxDepth) {
                    backStack.removeLast();
                }
            }
        }
        
        currentContext = context; // prvi put je main
        
        // Clear forward history on new navigation (standard browser behavior)
        forwardStack.clear();
    }

    /**
     * Goes back to the previous state.
     * @return Optional containing the previous context, or empty if at beginning
     */
    public Optional<NavigationContext> goBackContext() {
        if (backStack.isEmpty()) {
            return Optional.empty();
        }
        
        if (currentContext != null) {
            forwardStack.push(currentContext);
        }
        
        currentContext = backStack.pop();
        return Optional.of(currentContext);
    }

    /**
     * Goes forward to the next state.
     * @return Optional containing the next context, or empty if at end
     */
    public Optional<NavigationContext> goForwardContext() {
        if (forwardStack.isEmpty()) {
            return Optional.empty();
        }
        
        if (currentContext != null) {
            backStack.push(currentContext);
        }
        
        currentContext = forwardStack.pop();
        return Optional.of(currentContext);
    }
    
    /**
     * Legacy method for backward compatibility
     * @deprecated Use goBackContext() instead
     */
    @Deprecated
    public Optional<NavigationState> goBack() {
        return goBackContext().map(ctx -> {
            NavigationState state = new NavigationState(ctx.getViewName());
            return state;
        });
    }
    
    /**
     * Legacy method for backward compatibility
     * @deprecated Use goForwardContext() instead
     */
    @Deprecated
    public Optional<NavigationState> goForward() {
        return goForwardContext().map(ctx -> {
            NavigationState state = new NavigationState(ctx.getViewName());
            return state;
        });
    }

    public boolean canGoBack() {
        return !backStack.isEmpty();
    }

    public boolean canGoForward() {
        return !forwardStack.isEmpty();
    }

    public NavigationContext getCurrentContext() {
        return currentContext;
    }
    
    /**
     * Legacy method
     * @deprecated Use getCurrentContext() instead
     */
    @Deprecated
    public NavigationState getCurrentState() {
        if (currentContext == null) return null;
        return new NavigationState(currentContext.getViewName());
    }

    public void clearHistory() {
        backStack.clear();
        forwardStack.clear();
        currentContext = null;
    }

    public int getBackStackSize() {
        return backStack.size();
    }

    public int getForwardStackSize() {
        return forwardStack.size();
    }
    
    /**
     * Sets the navigation flag to prevent recording during history navigation.
     */
    public void setNavigating(boolean navigating) {
        this.isNavigating = navigating;
    }
    
    public boolean isNavigating() {
        return isNavigating;
    }
}
