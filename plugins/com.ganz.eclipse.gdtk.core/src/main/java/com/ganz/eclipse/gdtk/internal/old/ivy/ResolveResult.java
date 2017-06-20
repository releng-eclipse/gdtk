package com.ganz.eclipse.gdtk.internal.ivy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.ivy.core.report.ResolveReport;

public class ResolveResult {

	private Set<String> errorMessages;
	private final ResolveReport report;
	private Set<ArtifactDownloadReport> artifactReports;
	private Map<ModuleRevisionId, Artifact[]> dependencyArtifacts;
	private Map<ArtifactDownloadReport, Set<String>> retrievedArtifacts;

	public ResolveResult() {
		report = null;
		artifactReports = new LinkedHashSet<ArtifactDownloadReport>();
		dependencyArtifacts = new HashMap<ModuleRevisionId, Artifact[]>();
		errorMessages = new HashSet<String>();
	}

	public ResolveResult(ResolveReport report) {
		this.report = report;
		artifactReports = new LinkedHashSet<ArtifactDownloadReport>();
		dependencyArtifacts = new HashMap<ModuleRevisionId, Artifact[]>();
		errorMessages = new HashSet<String>();
	}

	public Set<String> getErrorMessages() {
		if (report != null) {
			return (Set<String>) report.getAllProblemMessages();
		}
		return errorMessages;
	}

	public void addArtifactReports(ArtifactDownloadReport[] artifactReports) {
		this.artifactReports.addAll(Arrays.asList(artifactReports));
	}

	void setDependencyArtifacts(ModuleRevisionId mrid, Artifact[] artifacts) {
		dependencyArtifacts.put(mrid, artifacts);
	}

	void setRetrievedArtifacts(Map<ArtifactDownloadReport, Set<String>> retrievedArtifacts) {
		this.retrievedArtifacts = retrievedArtifacts;
	}

	public Map<ArtifactDownloadReport, Set<String>> getRetrievedArtifacts() {
		return retrievedArtifacts;
	}

}
