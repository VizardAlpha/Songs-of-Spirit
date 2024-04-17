package vizardalpha.songsofspirit.room;

import settlement.room.main.*;
import settlement.room.main.util.RoomInit;

class ModRoomInstance extends RoomInstance{

	/**
	 * sadly, rooms are serializable, one of the few things there are. Take heed not to keep
	 * any fields that are not.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Just pass along these
	 * @param blueprint f
	 * @param area f
	 * @param init f
	 */
	protected ModRoomInstance(RoomBlueprintIns<? extends RoomInstance> blueprint, TmpArea area, RoomInit init) {
		super(blueprint, area, init);
	}

	/**
	 * 
	 * Must be implemented. Just cast it to the actual blueprint every time.
	 */
	@Override
	public RoomBlueprintIns<? extends RoomInstance> blueprintI() {
		return (ROOM_MOD) blueprint();
	}

	/**
	 * When the room is finished, or when it goes from unusable -> usable
	 */
	@Override
	protected void activateAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deactivateAction() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * When the room is removed
	 */
	@Override
	protected void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
