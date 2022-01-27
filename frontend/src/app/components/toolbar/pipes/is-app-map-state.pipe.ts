import {Pipe, PipeTransform} from '@angular/core';
import {MapState} from "../../map/type/map.type";

@Pipe({
  name: 'IsAppMapStatePipe',
  pure: true // by default
})
export class IsAppMapStatePipePipe implements PipeTransform {

  transform(mapState: MapState): boolean {
    return mapState === MapState.AppMode;
  }
}
