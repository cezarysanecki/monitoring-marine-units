import {Injectable} from "@angular/core";
import * as L from 'leaflet';
import {LatLngTuple} from 'leaflet';
import {AppMarkers, CurrentMapParameters, MapState} from "../type/map.type";
import {BehaviorSubject, Observable, Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MapService {

  private initialCenter: LatLngTuple = [60, 10.5];
  private initialZoom = 4;

  private mapStateSubject: BehaviorSubject<MapState> = new BehaviorSubject<MapState>(MapState.Idle);
  public mapState$: Observable<MapState> = this.mapStateSubject.asObservable();

  private mapMoveEndSubject!: Subject<CurrentMapParameters>;
  public mapMoveEnd$!: Observable<CurrentMapParameters>;

  map!: L.Map;
  markers: L.CircleMarker[] = [];

  polylines: L.Polyline[] = [];
  points: L.CircleMarker[] = [];

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

  changeState(mapState: MapState) {
    switch (mapState) {
      case MapState.AppMode:
        this.markers.forEach(marker => this.map.removeLayer(marker));
        this.markers = [];
        break;
      case MapState.PublicMode:
        this.polylines.forEach(polyline => this.map.removeLayer(polyline));
        this.polylines = [];
        this.points.forEach(point => this.map.removeLayer(point));
        this.points = [];
        break;
    }
    this.mapStateSubject.next(mapState);
  }

  refreshMap() {
    setTimeout(() => {
      this.map.invalidateSize(true)
    }, 200);
    this.map.closePopup();
  }

  attachMarkersOnMap(markers: L.CircleMarker[]) {
    this.markers.forEach(marker => this.map.removeLayer(marker));
    this.markers = markers;

    this.markers.forEach(mapMarker => {
      mapMarker.addTo(this.map);
    });

    this.refreshMap();
  }

  attachLinesOnMap(appMarkers: AppMarkers) {
    this.polylines.forEach(polyline => this.map.removeLayer(polyline));
    this.polylines = appMarkers.polylines;

    this.polylines.forEach(mapPolyline => {
      mapPolyline.addTo(this.map);
    });

    this.points.forEach(point => this.map.removeLayer(point));
    this.points = appMarkers.points;

    this.points.forEach(point => {
      point.addTo(this.map);
    });

    this.refreshMap();
  }

  centerOnInitialPlace() {
    this.map.flyTo(this.initialCenter, this.initialZoom);
  }

  centerOn(latitude: number, longitude: number, zoom: number = 10) {
    this.map.flyTo([latitude, longitude], zoom);
  }

  private getCurrentMapParameters(): CurrentMapParameters {
    const bounds = this.map.getBounds();

    return {
      bounds: {
        northWestLongitude: bounds.getNorthWest().lng,
        northWestLatitude: bounds.getNorthWest().lat,
        southEastLongitude: bounds.getSouthEast().lng,
        southEastLatitude: bounds.getSouthEast().lat
      },
      zoom: this.map.getZoom()
    }
  }
}
