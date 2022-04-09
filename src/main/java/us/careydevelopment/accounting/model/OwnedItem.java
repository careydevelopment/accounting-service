package us.careydevelopment.accounting.model;

public abstract class OwnedItem {

    private UserLightweight owner;

    public UserLightweight getOwner() {
        return owner;
    }

    public void setOwner(UserLightweight owner) {
        this.owner = owner;
    }
}
