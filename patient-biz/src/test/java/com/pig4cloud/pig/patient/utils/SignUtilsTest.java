package com.pig4cloud.pig.patient.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class SignUtilsTest {
    @Test
    public void test() {
        String str = SignUtils.sign("bp_adfjigaskfahfkae",
                                    "fcc5c63c-6fc2-11ed-a4a5-74867af2b7e0",
                                    "123", 1669712283L);
        Assertions.assertThat(str)
                  .isEqualTo("a123a343be5016fd993ff0dab120f68c");
    }
}
