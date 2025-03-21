package refactoring.model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import org.eclipse.core.resources.IProject;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;

import org.eclipse.jdt.internal.corext.util.JdtFlags;

public class RefactoringScopeFactory {
	/*
	 * Adds to <code> projects </code> IJavaProject objects for all projects directly or indirectly referencing focus. @param projects IJavaProjects will be added to this set
	 */
	private static void addReferencingProjects(IJavaProject focus, Set<IJavaProject> projects) throws JavaModelException {
		for (IProject referencingProject : focus.getProject().getReferencingProjects()) {
			IJavaProject candidate= JavaCore.create(referencingProject);
			if (candidate == null || projects.contains(candidate) || !candidate.exists())
				continue; // break cycle
			IClasspathEntry entry= getReferencingClassPathEntry(candidate, focus);
			if (entry != null) {
				projects.add(candidate);
				if (entry.isExported())
					addReferencingProjects(candidate, projects);
			}
		}
	}

	private static void addRelatedReferencing(IJavaProject focus, Set<IJavaProject> projects) throws CoreException {
		for (IProject referencingProject : focus.getProject().getReferencingProjects()) {
			IJavaProject candidate= JavaCore.create(referencingProject);
			if (candidate == null || projects.contains(candidate) || !candidate.exists())
				continue; // break cycle
			IClasspathEntry entry= getReferencingClassPathEntry(candidate, focus);
			if (entry != null) {
				projects.add(candidate);
				if (entry.isExported()) {
					addRelatedReferencing(candidate, projects);
					addRelatedReferenced(candidate, projects);
				}
			}
		}
	}

	private static void addRelatedReferenced(IJavaProject focus, Set<IJavaProject> projects) throws CoreException {
		for (IProject referencedProject : focus.getProject().getReferencedProjects()) {
			IJavaProject candidate= JavaCore.create(referencedProject);
			if (candidate == null || projects.contains(candidate) || !candidate.exists())
				continue; // break cycle
			IClasspathEntry entry= getReferencingClassPathEntry(focus, candidate);
			if (entry != null) {
				projects.add(candidate);
				if (entry.isExported()) {
					addRelatedReferenced(candidate, projects);
					addRelatedReferencing(candidate, projects);
				}
			}
		}
	}

	/**
	 * Creates a new search scope with all compilation units possibly referencing <code>javaElement</code>,
	 * considering the visibility of the element, references only from source
	 *
	 * @param javaElement the java element
	 * @return the search scope
	 * @throws JavaModelException if an error occurs
	 */
	public static IJavaSearchScope create(IJavaElement javaElement) throws JavaModelException {
		return RefactoringScopeFactory.create(javaElement, true, true);
	}

	/**
	 * Creates a new search scope with all compilation units possibly referencing <code>javaElement</code>,
	 * references only from source
	 *
	 * @param javaElement the java element
	 * @param considerVisibility consider visibility of javaElement iff <code>true</code>
	 * @return the search scope
	 * @throws JavaModelException if an error occurs
	 */
	public static IJavaSearchScope create(IJavaElement javaElement, boolean considerVisibility) throws JavaModelException {
		return RefactoringScopeFactory.create(javaElement, considerVisibility, true);
	}


	/**
	 * Creates a new search scope with all compilation units possibly referencing <code>javaElement</code>.
	 *
	 * @param javaElement the java element
	 * @param considerVisibility consider visibility of javaElement iff <code>true</code>
	 * @param sourceReferencesOnly consider references in source only (no references in binary)
	 * @return the search scope
	 * @throws JavaModelException if an error occurs
	 */
	public static IJavaSearchScope create(IJavaElement javaElement, boolean considerVisibility, boolean sourceReferencesOnly) throws JavaModelException {
		if (considerVisibility && javaElement instanceof IMember) {
			IMember member= (IMember) javaElement;
			if (JdtFlags.isPrivate(member)) {
				if (member.getCompilationUnit() != null)
					return SearchEngine.createJavaSearchScope(new IJavaElement[] { member.getCompilationUnit()});
				else
					return SearchEngine.createJavaSearchScope(new IJavaElement[] { member});
			}
			// Removed code that does some optimizations regarding package visible members. The problem is that
			// there can be a package fragment with the same name in a different source folder or project. So we
			// have to treat package visible members like public or protected members.
		}


		IJavaProject javaProject= javaElement.getJavaProject();
		return SearchEngine.createJavaSearchScope(getAllScopeElements(javaProject, sourceReferencesOnly), false);
	}

	/**
	 * Creates a new search scope comprising <code>members</code>.
	 *
	 * @param members the members
	 * @return the search scope
	 * @throws JavaModelException if an error occurs
	 */
	public static IJavaSearchScope create(IMember[] members) throws JavaModelException {
		return create(members, true);
	}

	/**
	 * Creates a new search scope comprising <code>members</code>.
	 *
	 * @param members the members
	 * @param sourceReferencesOnly consider references in source only (no references in binary)
	 * @return the search scope
	 * @throws JavaModelException if an error occurs
	 */
	public static IJavaSearchScope create(IMember[] members, boolean sourceReferencesOnly) throws JavaModelException {
		Assert.isTrue(members != null && members.length > 0);
		IMember candidate= members[0];
		int visibility= getVisibility(candidate);
		for (int i= 1; i < members.length; i++) {
			int mv= getVisibility(members[i]);
			if (mv > visibility) {
				visibility= mv;
				candidate= members[i];
			}
		}
		return create(candidate, true, sourceReferencesOnly);
	}

	/**
	 * Creates a new search scope comprising <code>members</code>.
	 * Includes the scopes of all projects that elements in {@code members} belong to.
	 *
	 * @param members the members
	 * @param sourceReferencesOnly consider references in source only (no references in binary)
	 * @return the search scope
	 * @throws JavaModelException if an error occurs
	 */
	public static IJavaSearchScope createProjectsScope(IMember[] members, boolean sourceReferencesOnly) throws JavaModelException {
		Assert.isTrue(members != null && members.length > 0);
		Set<IJavaElement> scopeElements = new LinkedHashSet<>();
		for (IMember member : members) {
			IJavaProject javaProject= member.getJavaProject();
			IJavaElement[] projectScopeElements = getAllScopeElements(javaProject, sourceReferencesOnly);
			scopeElements.addAll(Arrays.asList(projectScopeElements));
		}
		return SearchEngine.createJavaSearchScope(scopeElements.toArray(new IJavaElement[0]), false);
	}

	/**
	 * Creates a new search scope with all projects possibly referenced
	 * from the given <code>javaElements</code>.
	 *
	 * @param javaElements the java elements
	 * @return the search scope
	 */
	public static IJavaSearchScope createReferencedScope(IJavaElement[] javaElements) {
		Set<IJavaProject> projects= new HashSet<>();
		for (IJavaElement javaElement : javaElements) {
			projects.add(javaElement.getJavaProject());
		}
		IJavaProject[] prj= projects.toArray(new IJavaProject[projects.size()]);
		return SearchEngine.createJavaSearchScope(prj, true);
	}

	/**
	 * Creates a new search scope with all projects possibly referenced
	 * from the given <code>javaElements</code>.
	 *
	 * @param javaElements the java elements
	 * @param includeMask the include mask
	 * @return the search scope
	 */
	public static IJavaSearchScope createReferencedScope(IJavaElement[] javaElements, int includeMask) {
		Set<IJavaProject> projects= new HashSet<>();
		for (IJavaElement javaElement : javaElements) {
			projects.add(javaElement.getJavaProject());
		}
		IJavaProject[] prj= projects.toArray(new IJavaProject[projects.size()]);
		return SearchEngine.createJavaSearchScope(prj, includeMask);
	}

	/**
	 * Creates a new search scope containing all projects which reference or are referenced by the specified project.
	 *
	 * @param project the project
	 * @param includeMask the include mask
	 * @return the search scope
	 * @throws CoreException if a referenced project could not be determined
	 */
	public static IJavaSearchScope createRelatedProjectsScope(IJavaProject project, int includeMask) throws CoreException {
		IJavaProject[] projects= getRelatedProjects(project);
		return SearchEngine.createJavaSearchScope(projects, includeMask);
	}

	/*
	 * @param projects a collection of IJavaProject
	 * @return Array of IPackageFragmentRoot, one element for each packageFragmentRoot which lies within a project in <code> projects </code> .
	 */
	private static IPackageFragmentRoot[] getAllScopeElements(IJavaProject project, boolean onlySourceRoots) throws JavaModelException {
		List<IPackageFragmentRoot> result= new ArrayList<>();
		for (IJavaProject javaProject : getReferencingProjects(project)) {
			// Add all package fragment roots except archives
			for (IPackageFragmentRoot root : javaProject.getPackageFragmentRoots()) {
				if (!onlySourceRoots || root.getKind() == IPackageFragmentRoot.K_SOURCE)
					result.add(root);
			}
		}
		return result.toArray(new IPackageFragmentRoot[result.size()]);
	}

	/*
	 * Finds, if possible, a classpathEntry in one given project such that this classpath entry references another given project. If more than one entry exists for the referenced project and at least one is exported, then an exported entry will be returned.
	 */
	private static IClasspathEntry getReferencingClassPathEntry(IJavaProject referencingProject, IJavaProject referencedProject) throws JavaModelException {
		IClasspathEntry result= null;
		IPath path= referencedProject.getProject().getFullPath();
		for (IClasspathEntry entry : referencingProject.getResolvedClasspath(true)) {
			if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT && path.equals(entry.getPath())) {
				if (entry.isExported())
					return entry;
				// Consider it as a candidate. May be there is another entry that is
				// exported.
				result= entry;
			}
		}
		return result;
	}

	private static IJavaProject[] getRelatedProjects(IJavaProject focus) throws CoreException {
		final Set<IJavaProject> projects= new HashSet<>();

		addRelatedReferencing(focus, projects);
		addRelatedReferenced(focus, projects);

		projects.add(focus);
		return projects.toArray(new IJavaProject[projects.size()]);
	}

	private static Collection<IJavaProject> getReferencingProjects(IJavaProject focus) throws JavaModelException {
		Set<IJavaProject> projects= new HashSet<>();

		addReferencingProjects(focus, projects);
		projects.add(focus);
		return projects;
	}

	private static int getVisibility(IMember member) throws JavaModelException {
		if (JdtFlags.isPrivate(member))
			return 0;
		if (JdtFlags.isPackageVisible(member))
			return 1;
		if (JdtFlags.isProtected(member))
			return 2;
		return 4;
	}

	private RefactoringScopeFactory() {
		// no instances
	}
}
