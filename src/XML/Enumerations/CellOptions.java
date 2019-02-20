package XML.Enumerations;

/**
 * @author kunalupadya
 */
public enum  CellOptions {
    ROW("row"),
    COL("col"),
    STATE("state");

    private String key;

    private CellOptions (String option){
        key = option;
    }

    public String getKey() {
        return key;
    }
}