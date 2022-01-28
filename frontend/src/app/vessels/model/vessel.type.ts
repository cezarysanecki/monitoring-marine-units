export type VesselRegistry = {
  mmsi: number,
  name: string,
  shipType: string,
  destination: string,
  pointInTime: {
    timestamp: string,
    coordinates: {
      latitude: number,
      longitude: number
    }
  }
}

export type VesselToTrack = {
  mmsi: number,
  name: string,
  shipType: string
}

export type CheckedVesselRegistry = {
  active: boolean
} & VesselRegistry;

export type MonitoredVessel = {
  mmsi: number,
  isSuspended: boolean,
  name: string,
  shipType: string,
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
