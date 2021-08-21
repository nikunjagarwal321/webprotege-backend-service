package edu.stanford.protege.webprotege.sharing;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
public class GetProjectSharingSettingsAction implements ProjectAction<GetProjectSharingSettingsResult> {

    public static final String CHANNEL = "webprotege.projects.GetProjectSharingSettings";

    private ProjectId projectId;

    private GetProjectSharingSettingsAction() {
    }

    private GetProjectSharingSettingsAction(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    public static GetProjectSharingSettingsAction create(ProjectId projectId) {
        return new GetProjectSharingSettingsAction(projectId);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectSharingSettingsAction)) {
            return false;
        }
        GetProjectSharingSettingsAction other = (GetProjectSharingSettingsAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectSharingSettingsAction")
                .addValue(projectId)
                .toString();
    }
}
