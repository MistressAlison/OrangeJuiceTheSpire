package Moonworks.patches;

import Moonworks.powers.BookOfMemoriesPower;
import Moonworks.powers.MeltingMemoriesPower;
import Moonworks.relics.FleetingButterfly;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RenderPowerOnCardPatch {


    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
    public static class RenderOnCardPatch
    {
        private static final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("powers/powers.atlas"));
        private static final TextureAtlas.AtlasRegion image = atlas.findRegion("128/" + "ritual");

        @SpirePostfixPatch
        public static void RenderOnCard(AbstractCard __instance, SpriteBatch sb) {
            if (AbstractDungeon.player != null && validLocation(__instance)) {
                AbstractPower pow = AbstractDungeon.player.getPower(MeltingMemoriesPower.POWER_ID);
                AbstractRelic rel = AbstractDungeon.player.getRelic(FleetingButterfly.ID);
                if ((rel instanceof FleetingButterfly && !((FleetingButterfly) rel).triggeredThisCombat)||(pow instanceof MeltingMemoriesPower && ((MeltingMemoriesPower) pow).cardsPlayed < pow.amount)) {
                    if (BookOfMemoriesPower.getViability(__instance)) {
                        renderHelper(sb, image, __instance.current_x, __instance.current_y, __instance);
                    }
                }
            }
        }

        //Don't bother rendering if it isn't in one of 4 immediately viewable locations. We also don't want to render in master deck
        private static boolean validLocation(AbstractCard c) {
            return AbstractDungeon.player.hand.contains(c) ||
                    AbstractDungeon.player.drawPile.contains(c) ||
                    AbstractDungeon.player.discardPile.contains(c) ||
                    AbstractDungeon.player.exhaustPile.contains(c);
        }


        private static void renderHelper(SpriteBatch sb, TextureAtlas.AtlasRegion img, float drawX, float drawY, AbstractCard C) {
            sb.setColor(Color.WHITE);
            float dx = 155f;
            float dy = 210f;
            //sb.draw(img, drawX + img.offsetX - (float) img.originalWidth / 2.0F, drawY + img.offsetY - (float) img.originalHeight / 2.0F, (float) img.originalWidth / 2.0F - img.offsetX, (float) img.originalHeight / 2.0F - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, C.drawScale * Settings.scale, C.drawScale * Settings.scale, C.angle);
            sb.draw(img, drawX + dx + img.offsetX - (float) img.originalWidth / 2.0F, drawY + dy + img.offsetY - (float) img.originalHeight / 2.0F,
                    (float) img.originalWidth / 2.0F - img.offsetX - dx, (float) img.originalHeight / 2.0F - img.offsetY - dy,
                    (float) img.packedWidth, (float) img.packedHeight,
                    C.drawScale * Settings.scale, C.drawScale * Settings.scale, C.angle);
        }
    }
}
