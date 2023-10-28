package vizardalpha.songsofspirit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class SemVersionTest {
    @Test
    void isNewer() {
        Assertions.assertThat(SemVersion.from("1.0.0").isNewer(SemVersion.from("0.0.1"))).isTrue();
        Assertions.assertThat(SemVersion.from("0.1.0").isNewer(SemVersion.from("0.0.1"))).isTrue();
        Assertions.assertThat(SemVersion.from("0.0.2").isNewer(SemVersion.from("0.0.1"))).isTrue();

        Assertions.assertThat(SemVersion.from("0.0.1").isNewer(SemVersion.from("1.0.0"))).isFalse();
        Assertions.assertThat(SemVersion.from("0.0.1").isNewer(SemVersion.from("0.1.0"))).isFalse();
        Assertions.assertThat(SemVersion.from("0.0.1").isNewer(SemVersion.from("0.0.2"))).isFalse();

        Assertions.assertThat(SemVersion.from("0.0.1").isNewer(SemVersion.from("0.0.1"))).isFalse();
    }
}