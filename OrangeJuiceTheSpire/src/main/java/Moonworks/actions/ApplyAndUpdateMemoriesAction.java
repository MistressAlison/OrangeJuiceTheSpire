package Moonworks.actions;

import Moonworks.powers.BookOfMemoriesPower;
import Moonworks.util.MemoryHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.HashSet;

import static Moonworks.powers.BookOfMemoriesPower.getViability;

public class ApplyAndUpdateMemoriesAction extends AbstractGameAction {
    private final HashSet<AbstractCard> cardsToAdd = new HashSet<>();
    private final AbstractPlayer p = AbstractDungeon.player;

    public ApplyAndUpdateMemoriesAction(ArrayList<AbstractCard> cards) {
        //For each card...
        for (AbstractCard c : cards) {
            //If it is viable to add...
            if (getViability(c)) {
                //Set the default values for cooldown and active
                MemoryHelper.convertMemory(c);

                //Add it to the list of cards
                cardsToAdd.add(c);
            }
        }
    }

    public ApplyAndUpdateMemoriesAction(AbstractCard card) {
        //If it is viable to add...
        if (getViability(card)) {
            //Set the default values for cooldown and active
            MemoryHelper.convertMemory(card);

            //Add it to the list of cards
            cardsToAdd.add(card);
        }
    }

    @Override
    public void update() {
        //If we actually have cards to add...
        if (cardsToAdd.size() > 0) {
            //See if we have the power
            AbstractPower pow = p.getPower(BookOfMemoriesPower.POWER_ID);

            //We already have the power, update the list
            if (pow instanceof BookOfMemoriesPower) {
                ((BookOfMemoriesPower) pow).addCardsToSet(cardsToAdd);
                pow.flash();
            } else {
                //Apply the power. If we didn't have it, this will initialize it with our list
                this.addToBot(new ApplyPowerAction(p, p, new BookOfMemoriesPower(p, cardsToAdd)));
            }
        }

        //Finish the action
        this.isDone = true;
    }
}
