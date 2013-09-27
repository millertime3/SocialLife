package com.milyfe.untitled.services;

import com.milyfe.untitled.model.Message;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 9/21/13
 * Time: 1:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageLoaderServiceTest {

    @InjectMocks
    MessageLoaderServiceImpl messageLoaderService;
    @Mock
    FacebookService facebookService;
    @Mock
    TwitterService twitterService;

    @Before
    public void startUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetMessagesOnlyKicksOffAuthenicatedServices() {
        when(facebookService.hasAuthentication()).thenReturn(true);
        when(facebookService.getMessagesByDays(anyInt())).thenReturn(Lists.newArrayList(new Message()));
        messageLoaderService.loadMessagesFromAccessableServices();
        verify(twitterService,never()).getMessagesByDays(anyInt());
        verify(facebookService).getMessagesByDays(anyInt());
    }
}
