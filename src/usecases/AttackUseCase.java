package usecases;

import backend.unit.Unit;
import backend.unit.properties.ActiveAbility;
import backend.util.GameState;
import backend.util.VoogaEntity;

import java.util.Collection;
import java.util.Map;

/**
 * This is a description of how to code Exacmple Code Case 3
 */
public class AttackUseCase {

    /**
     * This is the code that will be put in the setOnClick method for the front
     * end's Unit class.
     *
     * @param backendunit this is a reference to the corresponding back end unit that
     *                    the front end unit will be displaying.
     * @param gamestate   state of the game before the attack. This will be modified by
     *                    the attack.
     */
    void setOnClick(Unit backendunit, GameState gamestate) {
        ActiveAbility attack = promptUserToPickAbility(backendunit.getActiveAbilities());
        Unit unitToAttack = promptUserForTarget(gamestate.getGrid()
                .getUnits());
        attackUnit(backendunit, unitToAttack, attack, gamestate);
    }

    /**
     * This will be filled in with JavaFX code to allow the user to select one
     * of the ActiveAbilities from the map
     *
     * @param map containing all the active abilities of the unit
     * @return one active ability, chosen at random. (This will be chosen by the user in the front end)
     */
    ActiveAbility<VoogaEntity> promptUserToPickAbility(
            Map<String, ActiveAbility<VoogaEntity>> map) {
        return ((ActiveAbility<VoogaEntity>[]) map.values().toArray())[(int) (map
                .values().toArray().length * Math.random())];
    }

    /**
     * This will be filled in with JavaFX code to allow the user to select one
     * of the Units from the collection
     *
     * @param units collection containing all of the units
     * @return one unit that the user will attack. (This will be chosen by the user in the front end)
     */
    Unit promptUserForTarget(Collection<Unit> units) {
        return ((Unit[]) units.toArray())[(int) (units.toArray().length * Math
                .random())];
    }

    /**
     * This is the code that will be written by the front end that will be
     * executed when the user clicks to use an attack.
     *
     * @param a         attacking unit
     * @param b         defending unit
     * @param attack    attack to use
     * @param gamestate state of the game before the attack. This will be modified by
     *                  the attack.
     */
    void attackUnit(Unit a, Unit b, ActiveAbility<Unit> attack,
                    GameState gamestate) {
        attack.affect(a, b, gamestate);
    }

}
