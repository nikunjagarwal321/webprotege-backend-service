package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.dispatch.Action;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 */
@AutoValue

@JsonTypeName("GetAvailableProjects")
public abstract class GetAvailableProjectsAction implements Action<GetAvailableProjectsResult>, Request<GetAvailableProjectsResult> {

    public static final String CHANNEL = "webprotege.projects.GetAvailableProjects";

    @JsonCreator
    public static GetAvailableProjectsAction create() {
        return new AutoValue_GetAvailableProjectsAction();
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
