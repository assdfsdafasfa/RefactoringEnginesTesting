package refactoring.random.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.manipulation.JavaManipulation;
import org.eclipse.jdt.internal.ui.preferences.formatter.ProfileVersionerCore;

public class FormatterProfileManagerCore {
	public final static String FORMATTER_SETTINGS_VERSION = "formatter_settings_version"; //$NON-NLS-1$

	public static Map<String, String> getProjectSettings(IJavaProject javaProject) {
		Map<String, String> options = new HashMap<>(javaProject.getOptions(true));
		IEclipsePreferences prefs = new ProjectScope(javaProject.getProject())
				.getNode(JavaManipulation.getPreferenceNodeId());
		if (prefs == null)
			return options;
		int profileVersion = prefs.getInt(FORMATTER_SETTINGS_VERSION, ProfileVersionerCore.getCurrentVersion());
		if (profileVersion == ProfileVersionerCore.getCurrentVersion())
			return options;
		return ProfileVersionerCore.updateAndComplete(options, profileVersion);
	}
}
