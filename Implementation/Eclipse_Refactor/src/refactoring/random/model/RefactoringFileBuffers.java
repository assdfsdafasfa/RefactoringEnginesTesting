package refactoring.random.model;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

public final class RefactoringFileBuffers {
	public static ITextFileBuffer acquire(final ICompilationUnit unit) throws CoreException {
//		Assert.isNotNull(unit);
		final IResource resource = unit.getResource();
		if (resource != null && resource.getType() == IResource.FILE) {
			final IPath path = resource.getFullPath();
			FileBuffers.getTextFileBufferManager().connect(path, LocationKind.IFILE, new NullProgressMonitor());
			return FileBuffers.getTextFileBufferManager().getTextFileBuffer(path, LocationKind.IFILE);
		}
		return null;
	}

	/**
	 * Returns the text file buffer for the specified compilation unit.
	 *
	 * @param unit the compilation unit whose text file buffer to retrieve
	 * @return the associated text file buffer, or <code>null</code> if no text file
	 *         buffer is managed for the compilation unit
	 */
	public static ITextFileBuffer getTextFileBuffer(final ICompilationUnit unit) {
//		Assert.isNotNull(unit);
		final IResource resource = unit.getResource();
		if (resource == null || resource.getType() != IResource.FILE)
			return null;
		return FileBuffers.getTextFileBufferManager().getTextFileBuffer(resource.getFullPath(), LocationKind.IFILE);
	}

	/**
	 * Releases the text file buffer associated with the compilation unit.
	 *
	 * @param unit the compilation unit whose text file buffer has to be released
	 * @throws CoreException if the buffer could not be successfully released
	 */
	public static void release(final ICompilationUnit unit) throws CoreException {
//		Assert.isNotNull(unit);
		final IResource resource = unit.getResource();
		if (resource != null && resource.getType() == IResource.FILE)
			FileBuffers.getTextFileBufferManager().disconnect(resource.getFullPath(), LocationKind.IFILE,
					new NullProgressMonitor());
	}

	private RefactoringFileBuffers() {
		// Not for instantiation
	}
}
