import {ComponentFactoryResolver, ComponentRef, ElementRef, Injectable, Injector} from '@angular/core';
import * as L from 'leaflet';
import {VesselService} from "../../../vessels/services/vessel.service";
import * as moment from "moment";
import {VesselRegistry} from "../../../vessels/model/vessel.type";
import {MarkerOptions, VesselData, VesselMarker} from "../type/marker.type";
import {PopupComponent} from "../../popup/popup.component";

@Injectable({
  providedIn: 'root'
})
export class MarkerService {

  markers: L.CircleMarker[] = [];
  compRef: ComponentRef<PopupComponent>;

  constructor(private injector: Injector,
              private vesselService: VesselService,
              private resolver: ComponentFactoryResolver) {
    const compFactory = this.resolver.resolveComponentFactory(PopupComponent);
    this.compRef = compFactory.create(this.injector);
  }

  foo(vesselsRegistries: VesselRegistry[]): L.Marker[] {


    return [];
  }

  makeVesselsMarkers(map: L.Map) {
    this.vesselService.fetchVesselsPositions(map)
      .subscribe((registries: VesselRegistry[]) => {
        this.markers.forEach(marker => map.removeLayer(marker));
        this.markers = [];

        let vesselMarkers = this.prepareMarkers(map, registries);
        let mapMarkers = vesselMarkers.map(vesselMarker => {
          const lat = vesselMarker.data.latitude;
          const lon = vesselMarker.data.longitude;

          const marker = L.circleMarker(
            [lat, lon],
            {
              ...vesselMarker.markerOptions
            });
          marker.bindPopup(vesselMarker.popupTemplate);

          return marker;
        });

        mapMarkers.forEach(mapMarker => {
          mapMarker.addTo(map);
          this.markers.push(mapMarker);
        })
      });
  }

  private prepareMarkers(map: L.Map, registries: VesselRegistry[]): VesselMarker[] {
    if (map.getZoom() < 9) {
      return this.groupRegistriesMarkers(map.getBounds(), registries);
    }
    return this.mapToRegistriesMarkers(registries);
  }

  private groupRegistriesMarkers(bounds: L.LatLngBounds, registries: VesselRegistry[]): VesselMarker[] {
    let vesselMarkers: VesselData[] = [];

    let northWest = bounds.getNorthWest();
    let southEast = bounds.getSouthEast();

    let heightPart = Math.abs(northWest.lat - southEast.lat) / 20;
    let widthPart = Math.abs(northWest.lng - southEast.lng) / 20;

    registries.forEach(registry => {
      let closestMarker = vesselMarkers.filter(marker =>
        Math.abs(marker.latitude - registry.pointInTime.coordinates.latitude) <= widthPart &&
        Math.abs(marker.longitude - registry.pointInTime.coordinates.longitude) <= heightPart);

      if (closestMarker.length > 0) {
        closestMarker[0].vessels.push(registry);
      } else {
        vesselMarkers.push({
          vessels: [registry],
          timestamp: null,
          latitude: registry.pointInTime.coordinates.latitude,
          longitude: registry.pointInTime.coordinates.longitude
        });
      }
    });

    return vesselMarkers.map(marker => {
      const component = this.resolver.resolveComponentFactory(PopupComponent).create(this.injector);
      component.instance.registries = marker.vessels;
      component.changeDetectorRef.detectChanges();

      return {
        data: marker,
        markerOptions: {
          ...this.prepareMarkerOptions(marker.vessels)
        },
        popupTemplate: component.location.nativeElement
      }
    });
  }

  private prepareMarkerOptions(registries: VesselRegistry[]): MarkerOptions {
    let numberOfVessels = registries.length;
    switch (true) {
      case (numberOfVessels <= 5):
        return this.prepareMarker(3, 'purple', 1);
      case (numberOfVessels <= 10):
        return this.prepareMarker(5, 'red');
      case (numberOfVessels <= 20):
        return this.prepareMarker(8, 'yellow');
      default:
        return this.prepareMarker(10, 'blue');
    }
  }

  private prepareMarker(radius: number, color: string, opacity: number = 0.3): MarkerOptions {
    return {
      radius: radius,
      color: color,
      fillOpacity: opacity
    };
  }

  private mapToRegistriesMarkers(registries: VesselRegistry[]): VesselMarker[] {
    const limitTimestamp = moment().subtract(5, 'minutes');

    return registries.map(registry => {
      const registryTimestamp = moment(registry.pointInTime.timestamp);
      const component = this.resolver.resolveComponentFactory(PopupComponent).create(this.injector);
      component.instance.registries = [ registry ];
      component.changeDetectorRef.detectChanges();

      return {
        data: {
          vessels: [registry],
          timestamp: registry.pointInTime.timestamp,
          latitude: registry.pointInTime.coordinates.latitude,
          longitude: registry.pointInTime.coordinates.longitude
        },
        markerOptions: this.prepareMarker(2, (registryTimestamp < limitTimestamp) ? 'grey' : 'green', 1),
        popupTemplate: component.location.nativeElement
      }
    });
  }
}
