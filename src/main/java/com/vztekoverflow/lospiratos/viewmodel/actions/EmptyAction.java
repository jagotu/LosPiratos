package com.vztekoverflow.lospiratos.viewmodel.actions;

public class EmptyAction extends Action {
    @Override
    protected Action createCopyAndResetThis() {
        return new EmptyAction();
    }

    @Override
    protected boolean recomputeVisible() {
        return true;
    }

    @Override
    protected boolean recomputePlannable() {
        return true;
    }

    @Override
    protected void recomputeCost() {
        //void
    }

    @Override
    protected void performOnShipInternal() {
        //void
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.empty;
    }

    @Override
    public String getČeskéJméno() {
        return "prázdná akce";
    }
}
