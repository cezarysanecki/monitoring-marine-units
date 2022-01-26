import {Injectable} from "@angular/core";
import {MonitoredVessel} from "../../../vessels/model/vessel.type";
import * as L from "leaflet";
import {LatLngExpression} from "leaflet";
import {AppMarkers} from "../type/map.type";

@Injectable({
  providedIn: 'root'
})
export class PolylineMarkerService {

  constructor() {
  }

  convertToAppMarkers(monitoredVessels: MonitoredVessel[]): AppMarkers {
    let polylines: L.Polyline[] = [];
    let points: L.CircleMarker[] = [];

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
        polylines.push(new L.Polyline(polylineObject.polylinePoints.map(point => [point.latitude, point.longitude]) as LatLngExpression[], {
          color: 'blue'
        }));
      })

      for (let i = 0; i < polylineObjects.length - 1; i++) {
        let previousPoint = polylineObjects[i];
        let nextPoint = polylineObjects[i + 1];

        polylines.push(new L.Polyline([[previousPoint.lastPoint.latitude, previousPoint.lastPoint.longitude],
          [nextPoint.firstPoint.latitude, nextPoint.firstPoint.longitude]] as LatLngExpression[], {
          color: 'red',
          dashArray: '5,10'
        }));
      }

      let lastPolylineObject = polylineObjects[polylineObjects.length - 1];

      points.push(new L.CircleMarker([lastPolylineObject.lastPoint.latitude, lastPolylineObject.lastPoint.longitude],
        {
          radius: 2,
          color: 'blue'
        }));
    });

    return {
      polylines: polylines,
      points: points
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
