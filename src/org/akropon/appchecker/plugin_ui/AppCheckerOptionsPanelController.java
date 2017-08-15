package org.akropon.appchecker.plugin_ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

/**
 * Provides attaching settings panel to it's appointed place in options menu.
 * 
 * U can find options in Tools->Options->Miscellaneous->AppChecker
 * 
 * @author akropon
 */
@org.openide.util.NbBundle.Messages({
		"AdvancedOption_DisplayName_AppChecker=AppChecker", 
		"AdvancedOption_Keywords_AppChecker=appchecker"})
@OptionsPanelController.SubRegistration(
		displayName = "#AdvancedOption_DisplayName_AppChecker",
		keywords = "#AdvancedOption_Keywords_AppChecker",
		keywordsCategory = "Advanced/AppChecker"
)
public final class AppCheckerOptionsPanelController extends OptionsPanelController {

	private AppCheckerPanel panel;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private boolean changed;

	public void update() {
		getPanel().load();
		changed = false;
	}

	public void applyChanges() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				getPanel().store();
				changed = false;
			}
		});
	}

	public void cancel() {
		// need not do anything special, if no changes have been persisted yet
	}

	public boolean isValid() {
		return getPanel().valid();
	}

	public boolean isChanged() {
		return changed;
	}

	public HelpCtx getHelpCtx() {
		return null; // new HelpCtx("...ID") if you have a help set
	}

	public JComponent getComponent(Lookup masterLookup) {
		return getPanel();
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	private AppCheckerPanel getPanel() {
		if (panel == null) {
			panel = new AppCheckerPanel(this);
		}
		return panel;
	}

	void changed() {
		if (!changed) {
			changed = true;
			pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
		}
		pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
	}

}
