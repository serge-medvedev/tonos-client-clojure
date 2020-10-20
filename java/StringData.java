package ton.client.dto;

import com.sun.jna.Structure;
import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import java.util.List;
import java.util.Arrays;

public class StringData extends Structure {
    public static class ByValue extends StringData implements Structure.ByValue {
        public ByValue() {
            super();
            this.content = Pointer.NULL;
            this.len = 0;
        }

        public ByValue(String content) {
            super();
            this.content = new Memory(content.length() + 1);
            this.content.setString(0, content);
            this.len = content.length();
        }
    }

    public Pointer content;
    public int len;

    public StringData() {
        super();
    }

    public String toString() {
        return new String(this.content.getByteArray(0, this.len));
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(new String[]{"content", "len"});
    }
}

