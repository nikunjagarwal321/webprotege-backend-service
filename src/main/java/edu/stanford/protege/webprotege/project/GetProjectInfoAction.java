package edu.stanford.protege.webprotege.project;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Aug 2018
 */
@JsonTypeName("webprotege.projects.GetProjectInfo")
public class GetProjectInfoAction implements ProjectAction<GetProjectInfoResult>, HasProjectId {

    public static final String CHANNEL = "webprotege.projects.GetProjectInfo";

    private final ProjectId projectId;

    @JsonCreator
    private GetProjectInfoAction(@JsonProperty("projectId") ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    public static GetProjectInfoAction create(ProjectId projectId) {
        return new GetProjectInfoAction(projectId);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public ProjectId projectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectInfoAction)) {
            return false;
        }
        GetProjectInfoAction other = (GetProjectInfoAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectInfoAction")
                .addValue(projectId)
                .toString();
    }
}
