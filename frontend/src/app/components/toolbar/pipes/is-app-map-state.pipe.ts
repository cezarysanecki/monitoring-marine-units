import {Pipe, PipeTransform} from '@angular/core';
import {MapState} from "../../map/type/map.type";

@Pipe({
  name: 'hideFilter'
})
export class HideFilterPipe implements PipeTransform {

  transform(mapState: MapState): boolean {
    return [MapState.AppMode, MapState.RefreshUserVessels].includes(mapState);
  }
}
