package edu.stanford.protege.webprotege.chgpwd;

import edu.stanford.protege.webprotege.auth.AuthenticationManager;
import edu.stanford.protege.webprotege.auth.Password;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.mail.MessagingExceptionHandler;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import edu.stanford.protege.webprotege.user.UserDetails;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class ResetPasswordActionHandler_TestCase {

    private final String EMAIL_ADDRESS = "EMAIL.address";

    @Mock
    private ResetPasswordMailer mailer;

    @Mock
    private ResetPasswordAction action;

    @Mock
    private ResetPasswordData data;

    @Mock
    private ExecutionContext context;

    private ResetPasswordActionHandler handler;

    @Mock
    private UserId userId;

    @Mock
    private UserDetailsManager userDetailsManager;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetails userDetails;

    @Before
    public void setUp() throws Exception {
        handler = new ResetPasswordActionHandler(userDetailsManager,
                authenticationManager, mailer);
        when(action.getResetPasswordData()).thenReturn(data);
        when(data.getEmailAddress()).thenReturn(EMAIL_ADDRESS);
    }


    @Test
    public void shouldReturnInvalidEmailAddressIfCannotFindAnyUser() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.empty());
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.INVALID_EMAIL_ADDRESS));
    }

    @Test
    public void shouldReturnInvalidEmailAddressIfUserEmailAddressDoesNotExist() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(userId));
        when(userDetailsManager.getUserDetails(userId)).thenReturn(Optional.of(userDetails));
        when(userDetails.getEmailAddress()).thenReturn(Optional.empty());
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.INVALID_EMAIL_ADDRESS));
    }

    @Test
    public void shouldReturnInvalidEmailAddressIfUserEmailAddressDoesEqualSuppliedEmailAddress() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(userId));
        when(userDetailsManager.getUserDetails(userId)).thenReturn(Optional.of(userDetails));
        when(userDetails.getEmailAddress()).thenReturn(Optional.of("other.address"));
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.INVALID_EMAIL_ADDRESS));
    }

    @Test
    public void shouldReturnSuccessIfEmailAddressComparesEqualIgnoreCase() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(userId));
        when(userDetailsManager.getUserDetails(userId)).thenReturn(Optional.of(userDetails));
        when(userDetails.getEmailAddress()).thenReturn(Optional.of(EMAIL_ADDRESS));
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.SUCCESS));
    }

    @Test
    public void shouldSendEmailOnSuccess() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(userId));
        when(userDetailsManager.getUserDetails(userId)).thenReturn(Optional.of(userDetails));
        when(userDetails.getEmailAddress()).thenReturn(Optional.of(EMAIL_ADDRESS));
        handler.execute(action, context);
        ArgumentCaptor<UserId> userIdCaptor = ArgumentCaptor.forClass(UserId.class);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        verify(mailer, times(1)).sendEmail(userIdCaptor.capture(), emailCaptor.capture(), any(String.class), any(MessagingExceptionHandler.class));
        assertThat(userIdCaptor.getValue(), is(userId));
        assertThat(emailCaptor.getValue(), is(EMAIL_ADDRESS));
    }


    @Test
    public void shouldReturnErrorOnException() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(userId));
        when(userDetailsManager.getUserDetails(userId)).thenReturn(Optional.of(userDetails));
        when(userDetails.getEmailAddress()).thenReturn(Optional.of(EMAIL_ADDRESS));
        doThrow(new RuntimeException()).when(authenticationManager).setPassword(
                any(UserId.class), any(Password.class));
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.INTERNAL_ERROR));
    }

    @Test
    public void shouldNotSendEmailOnException() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class)))
                .thenReturn(Optional.of(userId));
        when(userDetailsManager.getUserDetails(userId))
                .thenReturn(Optional.of(userDetails));
        when(userDetails.getEmailAddress())
                .thenReturn(Optional.of(EMAIL_ADDRESS));
        doThrow(new RuntimeException()).when(authenticationManager).setPassword(
                any(UserId.class), any(Password.class));
        handler.execute(action, context);
        verify(mailer, never()).sendEmail(any(UserId.class), any(String.class), any(String.class), any(MessagingExceptionHandler.class));
    }
}
