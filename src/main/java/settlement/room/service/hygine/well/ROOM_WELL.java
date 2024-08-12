package settlement.room.service.hygine.well;

import java.io.IOException;

import init.type.TERRAINS;
import settlement.misc.util.FSERVICE;
import settlement.path.finder.SFinderRoomService;
import settlement.room.industry.module.INDUSTRY_HASER;
import settlement.room.industry.module.Industry;
import settlement.room.main.BonusExp.RoomExperienceBonus;
import settlement.room.main.RoomBlueprintIns;
import settlement.room.main.category.RoomCategorySub;
import settlement.room.main.furnisher.Furnisher;
import settlement.room.main.util.RoomInitData;
import settlement.room.service.module.RoomServiceNeed;
import settlement.room.service.module.RoomServiceNeed.ROOM_SERVICE_NEED_HASER;
import snake2d.util.file.FileGetter;
import snake2d.util.file.FilePutter;
import snake2d.util.misc.CLAMP;
import snake2d.util.sets.ArrayList;
import snake2d.util.sets.LIST;
import snake2d.util.sets.LISTE;
import view.sett.ui.room.UIRoomModule;
import world.map.regions.Region;

public final class ROOM_WELL extends RoomBlueprintIns<WellInstance> implements ROOM_SERVICE_NEED_HASER, INDUSTRY_HASER {

    final RoomServiceNeed data;
    final Constructor constructor;
    final Wash bed;

    final Industry productionData;
    final Job job;
    final LIST<Industry> indus;

    public ROOM_WELL(String key, int index, RoomInitData init, RoomCategorySub block) throws IOException {
        super(index, init, key, block);
        bed = new Wash(this);
        data = new RoomServiceNeed(this, init) {

            @Override
            public FSERVICE service(int tx, int ty) {
                return bed.get(tx, ty);
            }

        };

        constructor = new Constructor(this, init);
        pushBo(init.data(), null, false);
        productionData = new Industry(this, init.data(), new Industry.RoomBoost[] {constructor.efficiency}, bonus()) {
            @Override
            public double getRegionBonus(Region reg) {
                return CLAMP.d(0.2 + reg.info.terrain(TERRAINS.NONE())*3, 0, 1);
            }
        };
        indus = new ArrayList<>(productionData);
        job = new Job(this);

        new RoomExperienceBonus(this, init.data(), bonus());
    }

    @Override
    protected void update(float ds) {

    }

    public Wash bed(int tx, int ty) {
        return bed.get(tx, ty);
    }

    @Override
    public Furnisher constructor() {
        return constructor;
    }

    @Override
    public SFinderRoomService service(int tx, int ty) {
        return data.finder;
    }

    @Override
    protected void saveP(FilePutter saveFile){
        data.saver.save(saveFile);
        productionData.save(saveFile);
    }

    @Override
    protected void loadP(FileGetter saveFile) throws IOException{
        data.saver.load(saveFile);
        productionData.load(saveFile);
    }

    @Override
    protected void clearP() {
        data.saver.clear();
        productionData.clear();
    }

    @Override
    public RoomServiceNeed service() {
        return data;
    }

    @Override
    public boolean makesDudesDirty() {
        return true;
    }

    @Override
    public void appendView(LISTE<UIRoomModule> mm) {

    }

    @Override
    public LIST<Industry> industries() {
        return indus;
    }

}
