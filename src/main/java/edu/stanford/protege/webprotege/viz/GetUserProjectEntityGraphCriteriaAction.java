package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
@AutoValue

@JsonTypeName("GetUserProjectEntityGraphCriteria")
public abstract class GetUserProjectEntityGraphCriteriaAction implements ProjectAction<GetUserProjectEntityGraphCriteriaResult> {

    public static final String CHANNEL = "webprotege.graph.GetUserProjectEntityGraphCriteria";

    @JsonCreator
    public static GetUserProjectEntityGraphCriteriaAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId) {
        return new AutoValue_GetUserProjectEntityGraphCriteriaAction(projectId);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();
}
