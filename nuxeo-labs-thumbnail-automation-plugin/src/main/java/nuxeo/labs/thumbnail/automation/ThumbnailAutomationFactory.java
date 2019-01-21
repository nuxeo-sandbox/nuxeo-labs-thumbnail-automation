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

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.thumbnail.ThumbnailFactory;
import org.nuxeo.runtime.api.Framework;

/**
 * @since 7.2
 */
public class ThumbnailAutomationFactory implements ThumbnailFactory {

    @Override
    public Blob getThumbnail(DocumentModel inDoc, CoreSession inSession) throws NuxeoException {

        Blob result = null;

        ThumbnailAutomationService service = Framework.getService(ThumbnailAutomationService.class);
        result = service.getThumbnail(inDoc, inSession);

        return result;
    }

    @Override
    public Blob computeThumbnail(DocumentModel inDoc, CoreSession inSession) {

        return null;
    }

}
