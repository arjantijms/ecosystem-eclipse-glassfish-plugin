/******************************************************************************
 * Copyright (c) 2018 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/

package org.eclipse.payara.tools.ui.serverview;

import static org.eclipse.payara.tools.PayaraToolsPlugin.is31OrAbove;
import static org.eclipse.payara.tools.utils.WtpUtil.load;
import static org.eclipse.wst.server.core.IServer.STATE_STARTED;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.payara.tools.server.PayaraServer;
import org.eclipse.payara.tools.ui.serverview.dynamicnodes.ApplicationNode;
import org.eclipse.payara.tools.ui.serverview.dynamicnodes.DeployedApplicationsNode;
import org.eclipse.payara.tools.ui.serverview.dynamicnodes.DeployedWebServicesNode;
import org.eclipse.payara.tools.ui.serverview.dynamicnodes.ResourcesNode;
import org.eclipse.payara.tools.ui.serverview.dynamicnodes.TreeNode;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.ui.internal.viewers.BaseContentProvider;

/**
 * This provides provides the dynamic nodes; the tree nodes that are inserted underneath a running
 * Payara / GlassFish server node in the Servers view.
 *
 * @see org.eclipse.payara.tools.ui.serverview.dynamicnodes
 *
 */
@SuppressWarnings("restriction")
public class ServerViewDynamicNodeProvider extends BaseContentProvider implements ITreeContentProvider {

    static String GLASSFISH_MANAGEMENT = "GlassFish Management"; //$NON-NLS-1$

    public ServerViewDynamicNodeProvider() {
        return;
    }

    @Override
    public Object[] getChildren(Object parentElement) {

        if (parentElement instanceof IServer) {
            IServer server = (IServer) parentElement;

            // Only active for a Payara or Glassfish 3.1.x server which is running, as we query
            // the server dynamically for the various artefacts it has running and/or available.

            boolean is31x = is31OrAbove(server.getRuntime());

            if ((is31x && server.getServerState() == STATE_STARTED)) {

                PayaraServer payaraServer = load(server, PayaraServer.class);

                if (payaraServer != null) {

                    TreeNode root = new TreeNode(GLASSFISH_MANAGEMENT, GLASSFISH_MANAGEMENT);

                    // Deployed Applications Node
                    root.addChild(new DeployedApplicationsNode(payaraServer));

                    // Resources Node
                    root.addChild(new ResourcesNode(payaraServer));

                    // Deployed web-services node
                    root.addChild(new DeployedWebServicesNode(payaraServer));

                    return new Object[] { root };
                }
            }
        }

        if (parentElement instanceof TreeNode) {
            TreeNode root = (TreeNode) parentElement;
            return root.getChildren();
        }

        return null;
    }

    @Override
    public Object[] getElements(Object parentElement) {
        return getChildren(parentElement);
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof DeployedApplicationsNode) {
            return ((DeployedApplicationsNode) element).getServer();
        }

        if (element instanceof ApplicationNode) {
            return ((ApplicationNode) element).getParent();
        }

        if (element instanceof TreeNode) {
            return ((TreeNode) element).getParent();
        }

        return null;
    }

    @Override

    public boolean hasChildren(Object element) {
        if (element instanceof IServer) {
            return true;
        }

        if (element instanceof DeployedApplicationsNode) {
            return true;

        }

        if (element instanceof ApplicationNode) {
            return true;
        }

        if (element instanceof TreeNode) {
            return ((TreeNode) element).getChildren().length > 0;
        }

        return false;
    }

}
