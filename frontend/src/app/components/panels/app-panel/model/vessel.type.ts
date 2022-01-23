export type Vessel = {
  mmsi: number,
  tracks: Track[]
}

export type Track = {
  coordinates: Coordinate[]
}

export type Coordinate = {
  timestamp: string,
  latitude: number,
  longitude: number
}
