package vizardalpha.songsofspirit.room.wine;

import java.io.IOException;
import settlement.path.finder.SFinderFindable;
import settlement.room.main.RoomBlueprintIns;
import settlement.room.main.category.RoomCategorySub;
import settlement.room.main.furnisher.Furnisher;
import settlement.room.main.util.RoomInitData;
import snake2d.util.file.FileGetter;
import snake2d.util.file.FilePutter;

/**
 * The main class regarding this room. Only thing that's actually needed. When implementing the class, one will notice one also 
 * needs a "furnisher" and a "roomInstance" for it to work.
 */
public class ROOM_WINE extends RoomBlueprintIns<WineInstance> {

	private final WineConstructor furnisher;
	
	/**
	 * @param data helpful init data that we don't need to care too much about. It automates reading of the .txt files
	 * @throws IOException 
	 */
	public ROOM_WINE(RoomInitData data, RoomCategorySub block) throws IOException {
		super(0, data, "PREP_VINIFICATION", data.m.CATS.REFINERS);
		
		furnisher = new WineConstructor(this, data);
	}

	@Override
	protected void saveP(FilePutter saveFile) {

	}

	@Override
	protected void loadP(FileGetter saveFile) throws IOException {

	}

	@Override
	protected void clearP() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SFinderFindable service(int tx, int ty) {
		return null;
	}

	@Override
	public Furnisher constructor() {
		return furnisher;
	}

	@Override
	protected void update(float ds) {
		
	}

}
