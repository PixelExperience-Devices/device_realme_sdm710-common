package vendor.oppo.hardware.biometrics.fingerprint.V2_1;

import java.util.ArrayList;

public final class FingerprintScreenState {
    public static final int FINGERPRINT_SCREEN_OFF = 0;
    public static final int FINGERPRINT_SCREEN_ON = 1;

    public static final String toString(int o) {
        if (o == 0) {
            return "FINGERPRINT_SCREEN_OFF";
        }
        if (o == 1) {
            return "FINGERPRINT_SCREEN_ON";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Integer.toHexString(o));
        return sb.toString();
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("FINGERPRINT_SCREEN_OFF");
        if ((o & 1) == 1) {
            list.add("FINGERPRINT_SCREEN_ON");
            flipped = 0 | 1;
        }
        if (o != flipped) {
            StringBuilder sb = new StringBuilder();
            sb.append("0x");
            sb.append(Integer.toHexString((~flipped) & o));
            list.add(sb.toString());
        }
        return String.join(" | ", list);
    }
}
