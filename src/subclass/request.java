package subclass;

import com.google.gson.JsonElement;

public class request extends JSON {
    private String type = null;
    public request(){}

    public request(String[] s) {
        super(null, null);
        this.type = s[1];
//        System.out.println(this.type);
        if (this.type.equals("exit")) return;
        super.key = s[3];
        if (this.type.equals("set")) {
            super.value = s[5];
        }
    }

    @Override
    public String getKey() {
        return super.getKey();
    }

    @Override
    public String getValue() {
        return super.getValue();
    }

    public String getType() {
        return type;
    }

    @Override
    public void setKey(String key) {
        super.setKey(key);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    public void setType(String type) {
        this.type = type;
    }
}