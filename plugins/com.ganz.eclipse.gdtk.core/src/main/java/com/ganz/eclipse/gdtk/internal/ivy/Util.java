package com.ganz.eclipse.gdtk.internal.ivy;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.resolve.ResolveOptions;

public class Util {
	public static String buildResolveId(ModuleDescriptor md, boolean useExtendedResolveId) {
		StringBuffer sb = new StringBuffer(ResolveOptions.getDefaultResolveId(md));
		if (useExtendedResolveId) {
			ModuleRevisionId mrid = md.getModuleRevisionId();
			String status = md.getStatus();
			String branch = mrid.getBranch();
			String revision = mrid.getRevision();
			sb.append("-");
			if (status != null) {
				sb.append(status);
			}
			if (branch != null) {
				sb.append(branch);
			}
			if (revision != null) {
				sb.append(revision);
			}
		}
		return sb.toString();
	}
}
