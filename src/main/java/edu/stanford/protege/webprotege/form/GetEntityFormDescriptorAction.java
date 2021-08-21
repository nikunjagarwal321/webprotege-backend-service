package edu.stanford.protege.webprotege.form;

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
 * 2020-01-16
 */
@AutoValue

@JsonTypeName("GetEntityFormDescriptor")
public abstract class GetEntityFormDescriptorAction implements ProjectAction<GetEntityFormDescriptorResult> {

    public static final String CHANNEL = "webprotege.forms.GetEntityFormDescriptor";

    @JsonCreator
    public static GetEntityFormDescriptorAction create(@JsonProperty("projectId") ProjectId projectId,
                                                       @JsonProperty("formId") FormId formId) {
        return new AutoValue_GetEntityFormDescriptorAction(projectId, formId);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract FormId getFormId();
}
