/******************************************************************************
 * Copyright (c) 2018 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/

package org.eclipse.payara.tools.facets;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.payara.tools.facets.internal.GlassfishDeploymentDescriptorFactory;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

public class GlassfishEjbFacetInstallDelegate implements IDelegate {

    @Override
    public void execute(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor) throws CoreException {
        IGlassfishDeploymentDescriptor ejbDescriptor = GlassfishDeploymentDescriptorFactory
                .getEjbDeploymentDescriptor(project);

        if (ejbDescriptor != null) {
            ejbDescriptor.store(monitor);
        }
    }

}
