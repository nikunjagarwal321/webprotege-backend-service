package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.entity.CommentedEntityData;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureIndex;
import edu.stanford.protege.webprotege.mansyntax.render.HasGetRendering;
import edu.stanford.protege.webprotege.pagination.PageRequest;
import edu.stanford.protege.webprotege.pagination.Pager;
import edu.stanford.protege.webprotege.common.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.entity.CommentedEntityData.byEntity;
import static edu.stanford.protege.webprotege.entity.CommentedEntityData.byLastModified;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
public class GetCommentedEntitiesActionHandler extends AbstractProjectActionHandler<GetCommentedEntitiesAction, GetCommentedEntitiesResult> {

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final HasGetRendering renderer;

    @Nonnull
    private final EntitiesInProjectSignatureIndex entitiesInSignature;

    @Inject
    public GetCommentedEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull EntityDiscussionThreadRepository repository,
                                             @Nonnull HasGetRendering renderer,
                                             @Nonnull EntitiesInProjectSignatureIndex entitiesInSignature) {
        super(accessManager);
        this.repository = checkNotNull(repository);
        this.renderer = checkNotNull(renderer);
        this.entitiesInSignature = checkNotNull(entitiesInSignature);
    }

    @Nonnull
    @Override
    public Class<GetCommentedEntitiesAction> getActionClass() {
        return GetCommentedEntitiesAction.class;
    }

    @Nonnull
    @Override
    public GetCommentedEntitiesResult execute(@Nonnull GetCommentedEntitiesAction action,
                                              @Nonnull ExecutionContext executionContext) {
        PageRequest request = action.getPageRequest();
        List<EntityDiscussionThread> allThreads = repository.getThreadsInProject(action.getProjectId());


        Map<OWLEntity, List<EntityDiscussionThread>> commentsByEntity = allThreads.stream()
                                                                                  .collect(groupingBy(
                                                                                          EntityDiscussionThread::getEntity));

        List<CommentedEntityData> result = new ArrayList<>();
        commentsByEntity.forEach((entity, threads) -> {
            if (entitiesInSignature.containsEntityInSignature(entity)) {
                int totalThreadCount = threads.size();
                int openThreadCount = (int) threads.stream()
                                                   .filter(thread -> thread.getStatus().isOpen())
                                                   .count();
                List<Comment> entityComments = threads.stream()
                                                      .flatMap(thread -> thread.getComments()
                                                                               .stream())
                                                      .collect(toList());
                Comment lastComment = entityComments.stream()
                                                    .max(comparing(c -> c.getUpdatedAt().orElse(c.getCreatedAt()))).get();

                List<UserId> participants = entityComments.stream()
                                                          .map(Comment::getCreatedBy)
                                                          .collect(toList());
                result.add(new CommentedEntityData(
                        renderer.getRendering(entity),
                        totalThreadCount,
                        openThreadCount,
                        entityComments.size(),
                        lastComment.getUpdatedAt().orElse(lastComment.getCreatedAt()),
                        lastComment.getCreatedBy(),
                        participants
                ));
            }
        });
        if(action.getSortingKey() == SortingKey.SORT_BY_ENTITY) {
            result.sort(byEntity);
        }
        else {
            result.sort(byLastModified);
        }
        Pager<CommentedEntityData> pager = Pager.getPagerForPageSize(result, request.getPageSize());
        return GetCommentedEntitiesResult.create(action.getProjectId(), pager.getPage(request.getPageNumber()));
    }
}
