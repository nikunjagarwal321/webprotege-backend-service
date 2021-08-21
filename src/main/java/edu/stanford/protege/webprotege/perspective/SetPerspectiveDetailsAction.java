package edu.stanford.protege.webprotege.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-03
 */
public class SetPerspectiveDetailsAction implements ProjectAction<SetPerspectiveDetailsResult> {

    public static final String CHANNEL = "webprotege.perspectives.SetPerspectiveDetails";

    private ProjectId projectId;

    private ImmutableList<PerspectiveDetails> perspectiveDetails;

    public SetPerspectiveDetailsAction(@Nonnull ProjectId projectId,
                                       @Nonnull ImmutableList<PerspectiveDetails> perspectiveDetails) {
        this.projectId = checkNotNull(projectId);
        this.perspectiveDetails = checkNotNull(perspectiveDetails);
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

    @Nonnull
    public ImmutableList<PerspectiveDetails> getPerspectiveDetails() {
        return perspectiveDetails;
    }
}
