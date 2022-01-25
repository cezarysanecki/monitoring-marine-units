export type VesselRegistry = {
  mmsi: number,
  pointInTime: {
    timestamp: string,
    coordinates: {
      latitude: number,
      longitude: number
    }
  }
}

export type MonitoredVessel = {
  mmsi: number,
  tracks: Track[]
}

export type Track = {
  pointsInTime: PointsInTime[]
}

export type PointsInTime = {
  timestamp: string,
  coordinates: {
    latitude: number,
    longitude: number
  }
}
