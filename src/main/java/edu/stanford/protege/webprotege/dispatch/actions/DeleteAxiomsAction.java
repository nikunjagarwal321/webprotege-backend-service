package edu.stanford.protege.webprotege.dispatch.actions;

import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Apr 2018
 */
public class DeleteAxiomsAction implements ProjectAction<DeleteAxiomsResult> {

    public static final String CHANNEL = "webprotege.axioms.DeleteAxioms";

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Stream<OWLAxiom> axioms;

    @Nonnull
    private final String commitMessage;


    public DeleteAxiomsAction(@Nonnull ProjectId projectId,
                              @Nonnull Stream<OWLAxiom> axioms,
                              @Nonnull String commitMessage) {
        this.projectId = checkNotNull(projectId);
        this.axioms = checkNotNull(axioms);
        this.commitMessage = checkNotNull(commitMessage);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    public ProjectId projectId() {
        return projectId;
    }

    @Nonnull
    public Stream<OWLAxiom> getAxioms() {
        return axioms;
    }

    @Nonnull
    public String getCommitMessage() {
        return commitMessage;
    }
}
