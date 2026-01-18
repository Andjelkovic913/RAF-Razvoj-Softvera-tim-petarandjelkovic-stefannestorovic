package org.raflab.studsluzbadesktopclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.raflab.studsluzbadesktopclient.services.NavigationContext;
import org.raflab.studsluzbadesktopclient.services.NavigationHistoryService;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
public class MainView {

	private final ContextFXMLLoader appFXMLLoader;
	private final NavigationHistoryService navigationHistoryService;
	private Scene scene;
	private Map<String, Parent> cachedRoots = new HashMap<>();
	private boolean isNavigatingFromHistory = false;
	private String currentViewName = null;

    public MainView(ContextFXMLLoader appFXMLLoader, NavigationHistoryService navigationHistoryService) {
        this.appFXMLLoader = appFXMLLoader;
        this.navigationHistoryService = navigationHistoryService;
	}

    public Scene createScene() {
	  try {		  
		  FXMLLoader loader = appFXMLLoader.getLoader(MainView.class.getResource("/fxml/main.fxml"));
		  BorderPane borderPane = loader.load();
		  this.scene = new Scene(borderPane,1000,800);
		  scene.getStylesheets().add(Objects.requireNonNull(MainView.class.getResource("/css/stylesheet.css")).toExternalForm());

		  // registrovanje inicijalnog view-a za history
		  currentViewName = "main";
		  navigationHistoryService.navigateTo("main", null, false);
		  
		  setupNavigationHandlers();
		  
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
	  return this.scene;
	 }
	 

	private void setupNavigationHandlers() {
		// Mouse button 4/5 (Back/Forward buttons on mouse)
		// Using addEventFilter to catch events before they're consumed by other controls
		scene.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
			MouseButton btn = event.getButton();
			if (btn == MouseButton.BACK) {
				goBack();
				event.consume();
			} else if (btn == MouseButton.FORWARD) {
				goForward();
				event.consume();
			}
		});
		
		// Keyboard shortcuts: Ctrl + [ (back) and Ctrl + ] (forward)
		scene.setOnKeyPressed(event -> {
			if (event.isControlDown()) {
				if (event.getCode() == KeyCode.OPEN_BRACKET) {
					goBack();
					event.consume();
				} else if (event.getCode() == KeyCode.CLOSE_BRACKET) {
					goForward();
					event.consume();
				}
			}
		});
		
		// Touchpad swipe gestures
		scene.setOnSwipeRight(event -> {
			goBack();
			event.consume();
		});
		
		scene.setOnSwipeLeft(event -> {
			goForward();
			event.consume();
		});
		
		// Shift + Scroll for touchpad navigation (more reliable on macOS)
		// Horizontal scroll: Shift + scroll left/right
		// Vertical scroll: Shift + scroll up/down
		scene.addEventFilter(javafx.scene.input.ScrollEvent.SCROLL, event -> {
			if (event.isShiftDown()) {
				double deltaX = event.getDeltaX();
				double deltaY = event.getDeltaY();
				
				// Horizontal scroll takes priority if present
				if (Math.abs(deltaX) > Math.abs(deltaY)) {
					if (deltaX > 5) {
						goBack();
						event.consume();
					} else if (deltaX < -5) {
						goForward();
						event.consume();
					}
				} else {
					// Vertical scroll as fallback
					if (deltaY > 5) {
						goBack();
						event.consume();
					} else if (deltaY < -5) {
						goForward();
						event.consume();
					}
				}
			}
		});
	}
	

	public void goBack() {
		Optional<NavigationContext> previousContext = navigationHistoryService.goBackContext();
		previousContext.ifPresent(this::restoreNavigationContext);
	}
	


	public void goForward() {
		Optional<NavigationContext> nextContext = navigationHistoryService.goForwardContext();
		nextContext.ifPresent(this::restoreNavigationContext);
	}
	

	private void restoreNavigationContext(NavigationContext context) {
		navigationHistoryService.setNavigating(true);
		isNavigatingFromHistory = true;
		
		try {
			// If different view, change root first
			if (!context.getViewName().equals(currentViewName)) {
				changeRootInternal(context.getViewName(), context.isCached());
			}
			
			// Always restore tab selection if specified
			if (context.getSelectedTabIndex() != null) {
				restoreTabSelection(context.getSelectedTabIndex());
			}
		} finally {
			isNavigatingFromHistory = false;
			navigationHistoryService.setNavigating(false);
		}
	}
	

	private void restoreTabSelection(int tabIndex) {
		Parent root = scene.getRoot();
		if (root != null) {
			TabPane tabPane = findTabPane(root);
			if (tabPane != null && tabIndex >= 0 && tabIndex < tabPane.getTabs().size()) {
				tabPane.getSelectionModel().select(tabIndex);
			}
		}
	}
	
//nadje prvi tabPane
	private TabPane findTabPane(Parent parent) {
		if (parent instanceof TabPane) {
			return (TabPane) parent;
		}
		for (Node child : parent.getChildrenUnmodifiable()) {
			if (child instanceof TabPane) {
				return (TabPane) child;
			}
			if (child instanceof Parent) {
				TabPane found = findTabPane((Parent) child);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}
	
	public void changeRoot(String fxml) {
		changeRoot(fxml, false);
	}
	
	public void changeRoot(String fxml, boolean cache) {
		if (!isNavigatingFromHistory) {
			navigationHistoryService.navigateTo(fxml, null, cache); //dodaje u history
		}
		changeRootInternal(fxml, cache); // ucita ekran
	}
	

	private void changeRootInternal(String fxml, boolean cache) {
		try {
			Parent root;
			if (cache && cachedRoots.containsKey(fxml)) {
				root = cachedRoots.get(fxml);
			} else {
				FXMLLoader loader = appFXMLLoader.getLoader(MainView.class.getResource("/fxml/"+fxml+".fxml"));
				root = loader.load();
				if (cache) {
					cachedRoots.put(fxml, root);
				}
			}
			scene.setRoot(root);
			currentViewName = fxml;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cacheCurrentRoot(String fxml) {
		if (scene.getRoot() != null) {
			cachedRoots.put(fxml, (Parent) scene.getRoot());
		}
	}
//
//	public Node loadPane(String fxml) {
//		FXMLLoader loader = appFXMLLoader.getLoader(MainView.class.getResource("/fxml/"+fxml+".fxml"));
//		try {
//			return loader.load();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	public void openModal(String fxml) {
//		FXMLLoader loader = appFXMLLoader.getLoader(MainView.class.getResource("/fxml/"+fxml+".fxml"));
//		try {
//			Parent parent = loader.load();
//			Scene scene = new Scene(parent, 400, 300);
//	        Stage stage = new Stage();
//	        stage.initModality(Modality.APPLICATION_MODAL);
//
//	        stage.setScene(scene);
//	        stage.showAndWait();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void openModal(String fxml, String title) {
//		FXMLLoader loader = appFXMLLoader.getLoader(MainView.class.getResource("/fxml/"+fxml+".fxml"));
//		try {
//			Parent parent = loader.load();
//			Scene scene = new Scene(parent, 400, 300);
//	        Stage stage = new Stage();
//	        stage.setTitle(title);
//	        stage.initModality(Modality.APPLICATION_MODAL);
//
//	        stage.setScene(scene);
//	        stage.showAndWait();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
	public void openModal(String fxml, String title,  int weight, int height) {
		FXMLLoader loader = appFXMLLoader.getLoader(MainView.class.getResource("/fxml/"+fxml+".fxml"));
		try {
			Parent parent = loader.load();
			Scene scene = new Scene(parent, weight, height);
	        Stage stage = new Stage();
	        stage.setTitle(title);
	        stage.initModality(Modality.APPLICATION_MODAL);
	        
	        stage.setScene(scene);
	        stage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
