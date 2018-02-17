package com.vztekoverflow.lospiratos.viewmodel.actions.attacks;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.actions.*;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.Mortar;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ObservableValue;

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
    public void performOnShipInternal() {
        if (getTarget() == null) throw new IllegalArgumentException("target must be set before Mortar can shoot.");
        applyDamageTo(MORTAR_DAMAGE, getTarget());
    }

    @Override
    public String getČeskéJméno() {
        return "střela z houfnice";
    }

    @Override
    protected Action createCopyAndResetThis() {
        MortarShot result = new MortarShot();
        result.setTarget(this.getTarget());
        this.setTarget(null);
        return result;
    }

    public MortarShot() {
        params.add(target);
        target.set(null);
        relatedShipJustChanged.addListener(__ -> {
            if (getRelatedShip() == null) {
                target.setRange(0);
                return;
            }
            target.groundZeroProperty().unbind();
            target.groundZeroProperty().bind(getRelatedShip().getPosition().coordinateProperty());

            Mortar mortar = getRelatedShip().getEnhancement(Mortar.class);
            if (mortar == null) {
                target.setRange(0);
            } else {
                target.setRange(mortar.getRange());
            }
        });
    }

    @Override
    public Iterable<ActionParameter> getAvailableParameters() {
        return params;
    }

    private List<ActionParameter> params = new ArrayList<>();
    private RangedAxialCoordinateActionParameter target = new RangedAxialCoordinateActionParameter() {
        @Override
        public String getČeskéJméno() {
            return "cíl";
        }
    };

    @Override
    public BooleanExpression satisfiedProperty() {
        return target.validProperty();
    }

    /**
     * equivalent for calling params.get(0).property().get()
     */
    public AxialCoordinate getTarget() {
        return target.get();
    }

    /**
     * equivalent for calling params.get(0).property().set()
     */
    public void setTarget(AxialCoordinate target) {
        this.target.set(target);
    }

    /**
     * equivalent for calling params.get(0).property()
     */
    public ObservableValue<AxialCoordinate> targetProperty() {
        return target.property();
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.mortar;
    }
}
