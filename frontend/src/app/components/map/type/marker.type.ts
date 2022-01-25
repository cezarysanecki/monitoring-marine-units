import {VesselRegistry} from "../../../vessels/model/vessel.type";

export type VesselMarker = {
  data: VesselData,
  markerOptions: MarkerOptions,
  popupTemplate: string
}

export type VesselData = {
  vessels: VesselRegistry[],
  timestamp: string | null,
  latitude: number,
  longitude: number
}

export type MarkerOptions = {
  color: string;
  radius: number,
  fillOpacity?: number
}
