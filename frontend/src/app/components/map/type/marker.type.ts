export type GroupMarker = {
  mmsis: number[]
} & MarkerCoordinates

export type SingleMarker = {
  mmsi: number,
  timestamp: string,
  active: boolean
} & MarkerCoordinates

export type MarkerCoordinates = {
  latitude: number,
  longitude: number
}

export type CurrentMapParameters = {
  zoom: number,
  bounds: Bounds
}

export type Bounds = {
  northWestLongitude: number,
  northWestLatitude: number,
  southEastLongitude: number,
  southEastLatitude: number,
}

export type MarkerOptions = {
  color: string;
  radius: number,
  fillOpacity?: number
}
