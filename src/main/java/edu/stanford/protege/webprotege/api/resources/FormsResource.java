package edu.stanford.protege.webprotege.api.resources;



import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.forms.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.Collectors.toMap;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
public class FormsResource {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ActionExecutor executor;

    @Nonnull
    private final FormResourceFactory formResourceFactory;


    public FormsResource(@Nonnull ProjectId projectId,
                         @Nonnull ActionExecutor executor,
                         @Nonnull FormResourceFactory formResourceFactory) {
        this.projectId = checkNotNull(projectId);
        this.executor = checkNotNull(executor);
        this.formResourceFactory = checkNotNull(formResourceFactory);
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/")
    public Response getForms(@Context UserId userId, @Context ExecutionContext executionContext) {
        var formsResult = executor.execute(new GetProjectFormDescriptorsAction(projectId), new edu.stanford.protege.webprotege.ipc.ExecutionContext(executionContext.getUserId()));
        var formDescriptors = formsResult.formDescriptors();
        var formSelectorsMap = formsResult.formSelectors().stream().collect(toMap(EntityFormSelector::getFormId,
                                                                                     EntityFormSelector::getCriteria,
                                                                                     (left, right) -> left));
        var formPurposeMap = formsResult.formSelectors().stream().collect(toMap(EntityFormSelector::getFormId,
                                                                                     EntityFormSelector::getPurpose,
                                                                                     (left, right) -> left));
        var result = formDescriptors.stream()
                       .map(descriptor -> {
                           var formId = descriptor.getFormId();
                           var criteria = formSelectorsMap.get(descriptor.getFormId());
                           var purpose = formPurposeMap.getOrDefault(descriptor.getFormId(), FormPurpose.ENTITY_EDITING);
                           return EntityFormDescriptor.valueOf(projectId, formId, descriptor, purpose, criteria);
                       })
                       .collect(toImmutableList());;
        return Response.accepted(result).build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Path("/")
    public Response setForms(@Context UserId userId,
                             @Context UriInfo uriInfo,
                             @Context ExecutionContext executionContext,
                             List<EntityFormDescriptor> entityFormDescriptors) {
        for(var entityFormDescriptor : entityFormDescriptors) {
            var formDescriptor = entityFormDescriptor.getDescriptor();
            var criteria = entityFormDescriptor.getSelectorCriteria();
            var selectionCriteria = criteria.asCompositeRootCriteria();
            var purpose = entityFormDescriptor.getPurpose();
            var action = new SetEntityFormDescriptorAction(projectId, formDescriptor, purpose, selectionCriteria);
            executor.execute(action, new edu.stanford.protege.webprotege.ipc.ExecutionContext(executionContext.getUserId()));
        }
        return Response.created(uriInfo.getRequestUri()).build();
    }

    @Path("{formId : [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public FormResource locateProjectResource(@PathParam("formId") FormId formId) {
        return formResourceFactory.create(projectId, formId);
    }
}
