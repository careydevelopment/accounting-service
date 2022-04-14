package us.careydevelopment.accounting.service;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.careydevelopment.accounting.model.OwnedItem;
import us.careydevelopment.accounting.model.User;
import us.careydevelopment.accounting.util.SessionUtil;

@Component
public abstract class BaseService {

    @Autowired
    protected SessionUtil sessionUtil;

    @VisibleForTesting
    void setOwner(final OwnedItem ownedItem) {
        if (ownedItem.getOwner() == null) {
            final User owner = sessionUtil.getCurrentUser();
            ownedItem.setOwner(owner);
        }
    }
}
