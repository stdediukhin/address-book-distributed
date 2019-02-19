package com.epam.addressbook.account;

import com.epam.addressbook.account.data.AccountDataGateway;
import com.epam.addressbook.account.data.AccountRecord;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    private AccountDataGateway gateway = mock(AccountDataGateway.class);
    private AccountController controller = new AccountController(gateway);

    @Test
    public void getAccounts_whenPersonIsFound_returnsInfos() {
        AccountRecord recordToFind = new AccountRecord(5L, 10L, "Some Name", "Some Password");
        doReturn(singletonList(recordToFind)).when(gateway).findAllByPersonId(anyLong());


        List<AccountInfo> result = controller.getAccounts(5);


        verify(gateway).findAllByPersonId(5L);
        assertThat(result).containsExactly(
                new AccountInfo(5L, 10L, "Some Name", "Some Password", "account info"));
    }
}