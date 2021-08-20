package edu.stanford.protege.webprotege.inject;

import edu.stanford.protege.webprotege.app.GetApplicationSettingsActionHandler;
import edu.stanford.protege.webprotege.app.SetApplicationSettingsActionHandler;
import edu.stanford.protege.webprotege.chgpwd.ResetPasswordActionHandler;
import edu.stanford.protege.webprotege.csv.GetCSVGridActionHandler;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.handlers.*;
import edu.stanford.protege.webprotege.events.GetProjectEventsActionHandler;
import edu.stanford.protege.webprotege.mail.GetEmailAddressActionHandler;
import edu.stanford.protege.webprotege.mail.SetEmailAddressActionHandler;
import edu.stanford.protege.webprotege.permissions.GetProjectPermissionsActionHandler;
import edu.stanford.protege.webprotege.permissions.RebuildPermissionsActionHandler;
import edu.stanford.protege.webprotege.perspective.GetPerspectivesActionHandler;
import edu.stanford.protege.webprotege.project.*;
import edu.stanford.protege.webprotege.user.CreateUserAccountActionHandler;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ActionHandlersModule {

    
    public ApplicationActionHandler provideGetAvailableProjectsHandler(GetAvailableProjectsActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideGetProjectDetailsActionHandler(GetProjectDetailsActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideLoadProjectActionHandler(LoadProjectActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideCreateNewProjectActionHandler(CreateNewProjectActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideGetProjectEventsActionHandler(GetProjectEventsActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideGetCurrentUserInSessionActionHandler(
            GetCurrentUserInSessionActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideSetEmailAddressActionHandler(SetEmailAddressActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideGetEmailAddressActionHandler(GetEmailAddressActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideMoveProjectsToTrashActionHandler(MoveProjectToTrashActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideRemoveProjectsFromTrashActionHandler(
            RemoveProjectFromTrashActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideGetCSVGridActionHandler(GetCSVGridActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideResetPasswordActionHandler(ResetPasswordActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideCreateUserAccountActionHandler(CreateUserAccountActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideGetPermissionsActionHandler(GetProjectPermissionsActionHandler handler) {
        return handler;
    }
    
    public ApplicationActionHandler provideGetSystemSettingsActionHandler(GetApplicationSettingsActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler provideSetAdminSettingsActionHandler(SetApplicationSettingsActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler providesRebuildPermissionsActionHandler(RebuildPermissionsActionHandler handler) {
        return handler;
    }

    
    public ApplicationActionHandler providesGetAvailableProjectsWithPermissionActionHandler(GetAvailableProjectsWithPermissionActionHandler handler) {
        return handler;
    }


    
    public ApplicationActionHandler provideGetPerspectivesActionHandler(GetPerspectivesActionHandler handler) {
        return handler;
    }
}
