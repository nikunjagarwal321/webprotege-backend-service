package edu.stanford.protege.webprotege.dispatch;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.EventTag;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.events.EventManager;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/02/2013
 */
public abstract class AbstractProjectChangeHandler<T, A extends Request<R>, R extends Response> extends AbstractProjectActionHandler<A, R> {

    @Nonnull
    private final EventManager<ProjectEvent> eventManager;

    @Nonnull
    private final HasApplyChanges applyChanges;

    @Nonnull
    public AbstractProjectChangeHandler(@Nonnull AccessManager accessManager,
                                        @Nonnull EventManager<ProjectEvent> eventManager,
                                        @Nonnull HasApplyChanges applyChanges) {
        super(accessManager);
        this.eventManager = checkNotNull(eventManager);
        this.applyChanges = checkNotNull(applyChanges);
    }

    @Nonnull
    @Override
    public final R execute(@Nonnull A action, @Nonnull ExecutionContext executionContext) {
        EventTag tag = eventManager.getCurrentTag();
        ChangeListGenerator<T> changeListGenerator = getChangeListGenerator(action, executionContext);
        ChangeApplicationResult<T> result = applyChanges.applyChanges(executionContext.getUserId(),
                                                                                       changeListGenerator);
        EventList<ProjectEvent> eventList = eventManager.getEventsFromTag(tag);
        return createActionResult(result, action, executionContext, eventList);
    }

    protected abstract ChangeListGenerator<T> getChangeListGenerator(A action,
                                                                     ExecutionContext executionContext);

    protected abstract R createActionResult(ChangeApplicationResult<T> changeApplicationResult,
                                            A action,
                                            ExecutionContext executionContext,
                                            EventList<ProjectEvent> eventList);



}
