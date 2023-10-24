package vizardalpha.songsofspirit;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a semantic version string e.g.: 1.2.3
 * It must consist of three parts separated by a dot (.): <major>.<minor>.<patch>
 */
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class SemVersion {
    private final int major;

    private final int minor;

    private final int patch;

    public boolean isNewer(SemVersion version) {
        if (this.equals(version)) {
            return false;
        }

        int diff = major - version.getMajor();
        if (diff == 0) {
            diff = minor - version.getMinor();
            if (diff == 0) {
                diff = patch - version.getPatch();
            }
        }

        return diff > 0;
    }

    public static SemVersion from(String version) {
        if (version == null || version.isEmpty()) {
            throw new IllegalArgumentException("Empty version");
        }

        String[] split = version.split("\\.");
        if (split.length != 3) {
            throw new IllegalArgumentException("Version must contain of three segments separated by a dot (.): <major>.<minor>.<patch>");
        }

        try {
            return new SemVersion(
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2])
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse version parts to integer", e);
        }
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }
}
