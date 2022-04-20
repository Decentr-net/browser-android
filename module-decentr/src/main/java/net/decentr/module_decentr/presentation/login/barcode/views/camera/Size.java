package net.decentr.module_decentr.presentation.login.barcode.views.camera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class Size {
    private final int zaa;
    private final int zab;

    public int getHeight() {
        return this.zab;
    }

    public int getWidth() {
        return this.zaa;
    }

    public int hashCode() {
        int var1 = this.zab;
        int var2 = this.zaa;
        return var1 ^ (var2 << 16 | var2 >>> 16);
    }

    @NonNull
    public static Size parseSize(@NonNull String string) throws NumberFormatException {
        if (string == null) {
            IllegalArgumentException string1 = new IllegalArgumentException("string must not be null");
            throw string1;
        } else {
            int var1 = string.indexOf(42);
            if (var1 < 0) {
                var1 = string.indexOf(120);
            }

            if (var1 < 0) {
                throw zaa(string);
            } else {
                try {
                    Size var2 = new Size(Integer.parseInt(string.substring(0, var1)), Integer.parseInt(string.substring(var1 + 1)));
                    return var2;
                } catch (NumberFormatException var3) {
                    throw zaa(string);
                }
            }
        }
    }

    @NonNull
    public String toString() {
        int var1 = this.zaa;
        int var3 = this.zab;
        StringBuilder var2 = new StringBuilder(23);
        var2.append(var1);
        var2.append("x");
        var2.append(var3);
        return var2.toString();
    }

    public Size(int width, int height) {
        this.zaa = width;
        this.zab = height;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (obj instanceof Size) {
            Size obj1 = (Size)obj;
            return this.zaa == obj1.zaa && this.zab == obj1.zab;
        } else {
            return false;
        }
    }

    private static NumberFormatException zaa(String var0) {
        StringBuilder var2 = new StringBuilder(var0.length() + 16);
        var2.append("Invalid Size: \"");
        var2.append(var0);
        var2.append("\"");
        NumberFormatException var1 = new NumberFormatException(var2.toString());
        throw var1;
    }
}
