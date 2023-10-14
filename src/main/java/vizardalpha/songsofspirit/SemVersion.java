package vizardalpha.songsofspirit;

import lombok.Getter;

/**
 * Represents a semantic version string e.g.: 1.2.3
 * It must consist of three parts separated by a dot (.): <major>.<minor>.<patch>
 */
@Getter
public class SemVersion {
    private final int major;

    private final int minor;

    private final int patch;

    public SemVersion(String version) {
        int[] versionParts = segments(version);

        this.major = versionParts[0];
        this.minor = versionParts[1];
        this.patch = versionParts[2];
    }

    public boolean isNewer(SemVersion version) {
        // compare order matters
        if (version.getMajor() > major) {
            return true;
        } else if (version.getMinor() > minor) {
            return true;
        } else if (version.getPatch() > patch) {
            return true;
        }

        return false;
    }

    /**
     * Will split a version string e.g. "1.2.3" into it's segments
     */
    private static int[] segments(String version) {
        if (version == null || version.isEmpty()) {
            throw new IllegalArgumentException("Empty version");
        }

        String[] split = version.split("\\.");
        if (split.length != 3) {
            throw new IllegalArgumentException("Version must contain of three segments separated by a dot (.): <major>.<minor>.<patch>");
        }

        try {
            int[] versionParts = new int[3];

            versionParts[0] = Integer.parseInt(split[0]);
            versionParts[1] = Integer.parseInt(split[1]);
            versionParts[2] = Integer.parseInt(split[2]);

            return versionParts;
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse version parts to integer", e);
        }
    }
}
