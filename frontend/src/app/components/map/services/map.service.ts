import {Injectable} from "@angular/core";
import * as L from 'leaflet';
import {CurrentMapParameters, MapState} from "../type/map.type";
import {BehaviorSubject, Observable} from "rxjs";
import {LatLngExpression, LatLngTuple, point} from "leaflet";
import {MonitoredVessel} from "../../../vessels/model/vessel.type";

@Injectable({
  providedIn: 'root'
})
export class MapService {

  private initialCenter: LatLngTuple = [60, 10.5];
  private initialZoom = 4;

  private mapStateSubject: BehaviorSubject<MapState> = new BehaviorSubject<MapState>(MapState.Idle);
  public mapState$: Observable<MapState> = this.mapStateSubject.asObservable();

  private mapMoveEndSubject!: BehaviorSubject<CurrentMapParameters>;
  public mapMoveEnd$!: Observable<CurrentMapParameters>;

  map!: L.Map;
  markers: L.CircleMarker[] = [];

  constructor() {
  }

  initMap() {
    this.map = L.map('map', {
      center: this.initialCenter,
      zoom: this.initialZoom,
      maxBounds: [[85, -50], [20, 85]]
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 3,
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);

    this.mapMoveEndSubject = new BehaviorSubject<CurrentMapParameters>(this.getCurrentMapParameters());
    this.mapMoveEnd$ = this.mapMoveEndSubject.asObservable();

    this.map.on('moveend', () => {
      this.mapMoveEndSubject.next(this.getCurrentMapParameters())
    });

    this.mapStateSubject.next(MapState.Ready);
  }

  foo(monitoredVessels: MonitoredVessel[]) {
    monitoredVessels.forEach(monitoredVessel => {
      let polylineObjects: PolylineObject[] = monitoredVessel.tracks.filter(track => track.pointsInTime.length > 0)
        .map(track => {
        let firstPoint = track.pointsInTime[0];
        let lastPoint = track.pointsInTime[track.pointsInTime.length - 1];

        let polylinePoints = track.pointsInTime.map(point => {
          return {
            latitude: point.coordinates.latitude,
            longitude: point.coordinates.longitude
          }
        });

        const polylineObject: PolylineObject = {
          firstPoint: {
            latitude: firstPoint.coordinates.latitude,
            longitude: firstPoint.coordinates.longitude
          },
          lastPoint: {
            latitude: lastPoint.coordinates.latitude,
            longitude: lastPoint.coordinates.longitude
          },
          polylinePoints: polylinePoints
        }

        return polylineObject;
      });

      polylineObjects.forEach(polylineObject => {
        new L.Polyline(polylineObject.polylinePoints.map(point => [point.latitude, point.longitude]) as LatLngExpression[])
          .addTo(this.map);
      })

      for (let i = 0; i < polylineObjects.length - 1; i++) {
        let previousPoint = polylineObjects[i];
        let nextPoint = polylineObjects[i + 1];

        new L.Polyline([[previousPoint.lastPoint.latitude, previousPoint.lastPoint.longitude],
          [nextPoint.firstPoint.latitude, nextPoint.firstPoint.longitude]] as LatLngExpression[], {
          color: 'red',
          dashArray: '5,10'
        }).addTo(this.map);
      }

      let lastPolylineObject = polylineObjects[polylineObjects.length - 1];

      new L.CircleMarker([lastPolylineObject.lastPoint.latitude, lastPolylineObject.lastPoint.longitude],
        {
          radius: 2,
          color: 'green'
        }).addTo(this.map);
    });
  }

  attachMarkersOnMap(markers: L.CircleMarker[]) {
    this.markers.forEach(marker => this.map.removeLayer(marker));
    this.markers = markers;

    this.markers.forEach(mapMarker => {
      mapMarker.addTo(this.map);
    });

    this.refreshMap();
  }

  refreshMap() {
    setTimeout(() => {
      this.map.invalidateSize(true)
    }, 200);
  }

  centerOnInitialPlace() {
    this.map.flyTo(this.initialCenter, this.initialZoom);
  }

  centerOn(latitude: number, longitude: number, zoom: number = 10) {
    this.map.flyTo([latitude, longitude], zoom);
  }

  changeState(mapState: MapState) {
    switch (mapState) {
      case MapState.AppMode:
        this.markers.forEach(marker => this.map.removeLayer(marker));
        this.markers = [];
        break;
    }
    this.mapStateSubject.next(mapState);
  }

  private getCurrentMapParameters(): CurrentMapParameters {
    const bounds = this.map.getBounds();

    return {
      bounds: {
        northWestLongitude: bounds.getNorthWest().lng,
        northWestLatitude: bounds.getNorthWest().lat,
        southEastLongitude: bounds.getSouthEast().lng,
        southEastLatitude: bounds.getSouthEast().lng
      },
      zoom: this.map.getZoom()
    }
  }
}

type PolylineObject = {
  polylinePoints: {
    latitude: number,
    longitude: number
  }[],
  firstPoint: {
    latitude: number,
    longitude: number
  },
  lastPoint: {
    latitude: number,
    longitude: number
  }
}
