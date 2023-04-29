package vizardalpha.songsofspirit.room.wine;

import java.io.IOException;
import settlement.path.AVAILABILITY;
import settlement.room.main.Room;
import settlement.room.main.RoomBlueprintImp;
import settlement.room.main.TmpArea;
import settlement.room.main.furnisher.Furnisher;
import settlement.room.main.furnisher.FurnisherItem;
import settlement.room.main.furnisher.FurnisherItemTile;
import settlement.room.main.furnisher.FurnisherStat;
import settlement.room.main.util.RoomInit;
import settlement.room.main.util.RoomInitData;
import settlement.room.sprite.RoomSprite1xN;
import settlement.room.sprite.RoomSpriteNew;
import settlement.room.sprite.RoomSpriteXxX;
import snake2d.util.file.Json;

/**
 * The most cumbersome part... Deals with the whole furnishing and construction process of a new room.
 * @author Jake
 *
 */
class WineConstructor extends Furnisher{

	final FurnisherStat workers;
	final FurnisherStat efficiency;
	private final ROOM_WINE blue;
	
	protected WineConstructor(ROOM_WINE blue, RoomInitData init) throws IOException {
		/**
		 * pass along init, and state hw many items this room has, and how many "stats". A stat can be "workers", "efficiency" or "services".
		 * These must match the actual case. We'll skip stats for now, and just do one item.
		 */
		super(init, 3, 3, 88, 44);
		this.blue = blue;
		
		workers = new FurnisherStat.FurnisherStatEmployees(this);
		efficiency = new FurnisherStat.FurnisherStatEfficiency(this, workers);
		//now we must construct the item. We sill start with
		/**
		 * SPRITES
		 */
		
		//We will use the new sprite system, which is json based. The actual visuals are in the .txt init file.
		Json sjson = init.data().json("SPRITES");
		
		//rooms use different kind of sprites. The easiest one is just simply a 1x1 tile. We'll use that.
		RoomSpriteNew Primary = new RoomSprite1xN(sjson, "PRIMARY_1X1", false);
		
		RoomSpriteNew TempleEmblem = new RoomSpriteXxX(sjson, "EMBLEM_2X2", 2);
		/**
		 * TILES
		 * Now we'll create a tile with the sprite and give it some properties
		 */
		
		final FurnisherItemTile tt = new FurnisherItemTile(this, Primary, AVAILABILITY.SOLID, false);
		final FurnisherItemTile dd = new FurnisherItemTile(this, TempleEmblem, AVAILABILITY.ROOM, false);
		
		/**
		 * ITEM from tiles
		 */
		new FurnisherItem(new FurnisherItemTile[][] {
			{tt},
		}, 4, 2);
		
		flush(3);
		
		new FurnisherItem(new FurnisherItemTile[][] {
			{dd,dd},
			{dd,dd},
		}, 1);
		
		flush(3);
	}

	@Override
	public boolean usesArea() {
		return true;
	}

	@Override
	public Room create(TmpArea area, RoomInit init) {
		return new WineInstance(blue, area, init);
	}
	
	@Override
	public boolean mustBeIndoors() {
		return true;
	}

	@Override
	public RoomBlueprintImp blue() {
		return blue;
	}


}
