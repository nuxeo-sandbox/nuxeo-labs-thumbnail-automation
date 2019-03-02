/*
 * (C) Copyright 2019 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Thibaud Arguillere
 */
package nuxeo.labs.thumbnail.automation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.thumbnail.ThumbnailService;
import org.nuxeo.ecm.core.api.thumbnail.ThumbnailServiceImpl;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * First implementation: we assume a single configuration
 * 
 * @since 9.10
 */
public class ThumbnailAutomationServiceImpl extends DefaultComponent implements ThumbnailAutomationService {

    private static final Log log = LogFactory.getLog(ThumbnailAutomationServiceImpl.class);

    public static final String CONFIG_EXT_POINT = "configuration";

    protected ThumbnailAutomationDescriptor config = null;

    /**
     * Component activated notification. Called when the component is activated. All component dependencies are resolved
     * at that moment. Use this method to initialize the component.
     *
     * @param context the component context.
     */
    @Override
    public void activate(ComponentContext context) {
        super.activate(context);
    }

    /**
     * Component deactivated notification. Called before a component is unregistered. Use this method to do cleanup if
     * any and free any resources held by the component.
     *
     * @param context the component context.
     */
    @Override
    public void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    /**
     * Application started notification. Called after the application started. You can do here any initialization that
     * requires a working application (all resolved bundles and components are active at that moment)
     *
     * @param context the component context. Use it to get the current bundle context
     * @throws Exception
     */
    @Override
    public void applicationStarted(ComponentContext context) {
    }

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {

        if (CONFIG_EXT_POINT.equals(extensionPoint)) {
            if (config == null) {
                config = (ThumbnailAutomationDescriptor) contribution;
            } else {
                log.warn("More than one contribution loaded => we handle only the first one");
            }
        } else {
            log.warn("Received configuration for extension point '" + extensionPoint
                    + "', but we do not handle this extension point.");
        }
    }

    @Override
    public void unregisterContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        // Logic to do when unregistering any contribution
    }

    @Override
    public Blob getThumbnail(DocumentModel doc, CoreSession session) {
        
        if(config == null) {
            log.error("No configuration: Returning default thumbnail");
            return getDefaultThumbnail(doc, session);
        }

        String chainId = config.getChainId();
        if(StringUtils.isBlank(chainId)) {
            log.error("No chainID passed to the service: Returning default thumbnail");
            return getDefaultThumbnail(doc, session);
        }

        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext octx = new OperationContext();
        octx.setInput(doc);
        octx.setCoreSession(session);
        OperationChain chain = new OperationChain("getThumbnailCallbackChain");
        chain.add(chainId);

        Blob result;
        try {
            result = (Blob) as.run(octx, chain);
        } catch (OperationException e) {
            throw new NuxeoException(e);
        }

        return result;
    }
    
    protected Blob getDefaultThumbnail(DocumentModel doc, CoreSession session) {
        
        ThumbnailServiceImpl service = (ThumbnailServiceImpl) Framework.getService(ThumbnailService.class);
        
        return service.getDefaultFactory().getThumbnail(doc, session);
    }

}
