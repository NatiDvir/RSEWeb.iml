package engine.rse;

import java.util.ArrayList;
import java.util.List;

public class RseOffers {

    protected List<RseOffer> rseOffer;

    public List<RseOffer> getRseOffer() {
        if (rseOffer == null) {
            rseOffer = new ArrayList<RseOffer>();
        }
        return this.rseOffer;
    }
    public int getRseOfferSize() {
        return this.rseOffer.size();
    }
    public void addRseOffer(RseOffer o) {
        getRseOffer().add(o);
    }
    public RseOffer getRseOffer(int i)
    {
        return getRseOffer().get(i);
    }
}
