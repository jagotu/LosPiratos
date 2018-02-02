package com.vztekoverflow.lospiratos.viewmodel.actions.attacks;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.actions.Attack;
import com.vztekoverflow.lospiratos.viewmodel.actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.Mortar;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ObjectProperty;

import java.util.ArrayList;
import java.util.List;

public class MortarShot extends Attack implements ParameterizedAction {
    public static final int MORTAR_DAMAGE = 10;

    @Override
    protected boolean recomputeVisible() {
        return getRelatedShip().hasEnhancement(Mortar.class);
    }

    @Override
    protected boolean recomputePlannable() {
        return getRelatedShip().hasActiveEnhancement(Mortar.class) &&
                shipHasPlannedLessThan(getRelatedShip().getEnhancement(Mortar.class).getMortarsCount(), MortarShot.class);
    }

    @Override
    public void performOnTargetInternal() {
        if(getTarget() == null) throw new IllegalArgumentException("target must be set before Mortar can shoot.");
        applyDamageTo(MORTAR_DAMAGE, getTarget());
    }

    @Override
    public String getČeskéJméno() {
        return "střela z houfnice";
    }

    @Override
    protected Action createCopy() {
        return new MortarShot();
    }

    public int getRange(){
        return range.get();
    }

    public IntegerBinding rangeProperty(){
        return range;
    }

    private IntegerBinding range = new IntegerBinding() {
        @Override
        protected int computeValue() {
            Mortar mortar = getRelatedShip().getEnhancement(Mortar.class);
            if(mortar == null) return 0;
            return mortar.getRange();
        }
    };

    public MortarShot() {
        params.add(target);
        target.set(null);
    }

    @Override
    public Iterable<ActionParameter> getAvailableParameters() {
        return params;
    }
    private List<ActionParameter> params = new ArrayList<>();
    private AxialCoordinateActionParameter target = new AxialCoordinateActionParameter(){
        @Override
        public String getČeskéJméno() {
            return "cíl";
        }
    };

    /*
     * equivalent for calling params.get(0).property().get()
     */
    public AxialCoordinate getTarget() {
        return target.get();
    }

    /*
     * equivalent for calling params.get(0).property().set()
     */
    public void setTarget(AxialCoordinate target) {
        this.target.set(target);
    }

    /*
     * equivalent for calling params.get(0).property()
     */
    public ObjectProperty<AxialCoordinate> enhancementIconProperty(){
        return target.property();
    }
}
