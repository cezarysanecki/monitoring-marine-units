import {ComponentFactoryResolver, ComponentRef, Injectable, Injector} from '@angular/core';
import * as L from 'leaflet';
import {GroupMarker, MarkerOptions, SingleMarker} from "../type/map.type";
import {PopupComponent} from "../../popup/popup.component";

@Injectable({
  providedIn: 'root'
})
export class MarkerService {

  popupComponentRef: ComponentRef<PopupComponent>;

  constructor(private injector: Injector,
              private resolver: ComponentFactoryResolver) {
    const compFactory = this.resolver.resolveComponentFactory(PopupComponent);
    this.popupComponentRef = compFactory.create(this.injector);
  }

  convertToMapCircleMarkers(preparedMarkers: GroupMarker[] | SingleMarker[]): L.CircleMarker[] {
    return preparedMarkers.map(marker => {
      return this.isSingleMarker(marker) ?
        this.convertSingleMarker(marker as SingleMarker) :
        this.convertGroupMarker(marker as GroupMarker)
    });
  }

  private isSingleMarker(preparedMarker: GroupMarker | SingleMarker): preparedMarker is SingleMarker {
    return (preparedMarker as SingleMarker).mmsi !== undefined;
  }

  private convertSingleMarker(marker: SingleMarker): L.CircleMarker {
    let mapMarker = L.circleMarker(
      [marker.latitude, marker.longitude],
      {
        ...this.getSingleMarkerOptions(2, marker.active ? 'grey' : 'green', 1)
      }
    );

    const component = this.resolver.resolveComponentFactory(PopupComponent).create(this.injector);
    component.instance.singleMarker = marker;
    component.changeDetectorRef.detectChanges();

    mapMarker.bindPopup(component.location.nativeElement);

    return mapMarker;
  }

  private convertGroupMarker(marker: GroupMarker): L.CircleMarker {
    let mapMarker = L.circleMarker(
      [marker.latitude, marker.longitude],
      {
        ...this.getGroupMarkerOptions(marker.mmsis)
      }
    );

    const component = this.resolver.resolveComponentFactory(PopupComponent).create(this.injector);
    component.instance.groupMarker = marker;
    component.changeDetectorRef.detectChanges();

    mapMarker.bindPopup(component.location.nativeElement);

    return mapMarker;
  }

  private getGroupMarkerOptions(mmsis: number[]): MarkerOptions {
    let numberOfMmsis = mmsis.length;
    switch (true) {
      case (numberOfMmsis <= 5):
        return this.getSingleMarkerOptions(3, 'purple', 1);
      case (numberOfMmsis <= 10):
        return this.getSingleMarkerOptions(5, 'red');
      case (numberOfMmsis <= 20):
        return this.getSingleMarkerOptions(8, 'yellow');
      default:
        return this.getSingleMarkerOptions(10, 'blue');
    }
  }

  private getSingleMarkerOptions(radius: number, color: string, opacity: number = 0.3): MarkerOptions {
    return {
      radius: radius,
      color: color,
      fillOpacity: opacity
    };
  }
}
