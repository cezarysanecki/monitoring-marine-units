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
