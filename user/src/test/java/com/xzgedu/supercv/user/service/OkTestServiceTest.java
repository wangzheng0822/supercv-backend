package com.xzgedu.supercv.user.service;

import com.xzgedu.supercv.user.repo.OkTestRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OkTestServiceTest {

    @InjectMocks
    private OkTestService okTestService;

    @Mock
    private OkTestRepo okTestRepo;

    @Test
    void deleteOkTest() {
        when(okTestRepo.deleteOkTest(1)).thenReturn(false);
        assertThat(okTestService.deleteOkTest(1))
                .isFalse();

        when(okTestRepo.deleteOkTest(2)).thenReturn(true);
        assertThat(okTestService.deleteOkTest(2))
                .isTrue();
    }

}