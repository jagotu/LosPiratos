package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class RepairEnhancement extends EnhancementAbstractTransaction {

    @Override
    protected Action createCopy() {
        return new RepairEnhancement();
    }

    @Override
    public void performOnTarget() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        return "oprava vylepšení";
    }

    @Override
    protected Resource recomputeCost() {
        throw new NotImplementedException();
    }
}
