package org.fuin.dsl.cqrs.ui.internal;

import java.net.URL;

import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.BundleContext;

/**
 * Activator that additionally applies this plugin's {@code log4j.properties} so the
 * remote-scope diagnostics emitted by {@code org.fuin.dsl.cqrs} (catalog discovery and
 * parse errors) become visible in the log.
 *
 * <p>The generated {@link CqrsActivator} must not be edited; per its own contract
 * customizations belong in a subclass. This class is therefore registered as the
 * {@code Bundle-Activator} in {@code META-INF/MANIFEST.MF}.</p>
 */
public class CqrsDslUiActivator extends CqrsActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		configureLogging(context);
	}

	/**
	 * Applies the bundled {@code log4j.properties}. The file defines no {@code rootLogger},
	 * so {@link PropertyConfigurator} only sets up the {@code org.fuin.dsl.cqrs} logger and
	 * leaves the global Log4j/reload4j configuration shared with Eclipse and Xtext intact.
	 */
	private void configureLogging(BundleContext context) {
		URL config = context.getBundle().getEntry("log4j.properties");
		if (config != null) {
			PropertyConfigurator.configure(config);
		}
	}
}
