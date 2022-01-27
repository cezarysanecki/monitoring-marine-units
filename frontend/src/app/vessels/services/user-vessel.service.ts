import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {MonitoredVessel} from "../model/vessel.type";

@Injectable({
  providedIn: 'root'
})
export class UserVesselService {

  private userVesselsSubject: BehaviorSubject<MonitoredVessel[]> = new BehaviorSubject<MonitoredVessel[]>([]);
  public userVessels$: Observable<MonitoredVessel[]> = this.userVesselsSubject.asObservable();

  constructor() {
  }

  pushUserVessels(monitoredVessels: MonitoredVessel[]) {
    this.userVesselsSubject.next(monitoredVessels);
  }
}
