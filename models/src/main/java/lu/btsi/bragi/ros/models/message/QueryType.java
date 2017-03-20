package lu.btsi.bragi.ros.models.message;

/**
 * Created by gillesbraun on 20/03/2017.
 */
public enum QueryType {
    Open_Orders_For_Location(1),
    Unpaid_Orders(0),
    ;

    private final int numberOfParams;

    QueryType(int numberOfParams) {
        this.numberOfParams = numberOfParams;
    }

    public int getNumberOfParams() {
        return numberOfParams;
    }

}