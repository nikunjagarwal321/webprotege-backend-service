package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.common.UserId;

import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/02/15
 */
public interface HasUserIds {

    Collection<UserId> getUserIds();
}
